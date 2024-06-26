package com.ludo.study.studymatchingplatform.auth.service.kakao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.repository.InMemoryClientRegistrationAndProviderRepository;
import com.ludo.study.studymatchingplatform.auth.service.kakao.dto.KakaoOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.kakao.dto.KakaoUserProfileDto;
import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.repository.config.GlobalNotificationUserConfigRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.exception.DuplicatedSignUpException;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoSignUpService {

	private final InMemoryClientRegistrationAndProviderRepository clientRegistrationAndProviderRepository;
	private final KakaoOAuthTokenRequestService kakaoOAuthTokenRequestService;
	private final KakaoProfileRequestService kakaoProfileRequestService;
	private final UserRepositoryImpl userRepository;
	private final StudyStatisticsRepositoryImpl studyStatisticsRepository;
	private final ReviewStatisticsRepositoryImpl reviewStatisticsRepository;

	private final GlobalNotificationUserConfigRepositoryImpl notificationUserConfigRepository;

	@Transactional
	public User kakaoSignUp(final String authorizationCode) {
		final String kakaoSignUpRedirectUri =
				clientRegistrationAndProviderRepository.findSignupRedirectUri(Social.KAKAO);
		final KakaoOAuthToken kakaoOAuthToken =
				kakaoOAuthTokenRequestService.createOAuthToken(authorizationCode, kakaoSignUpRedirectUri);
		final KakaoUserProfileDto kakaoUserProfileDto = kakaoProfileRequestService.createKakaoProfile(kakaoOAuthToken);

		validateAlreadySignUp(kakaoUserProfileDto);

		return sinup(kakaoUserProfileDto);
	}

	private void validateAlreadySignUp(final KakaoUserProfileDto userInfo) {
		userRepository.findByEmail(userInfo.getEmail())
				.ifPresent(user -> {
					throw new DuplicatedSignUpException("이미 가입되어 있는 회원입니다.");
				});
	}

	private User sinup(KakaoUserProfileDto kakaoUserProfileDto) {
		final User user = userRepository.save(kakaoUserProfileDto.toUser());
		user.setInitialDefaultNickname();
		notificationUserConfigRepository.save(GlobalNotificationUserConfig.ofNewSignUpUser(user));
		createStudyStatistics(user);
		createReviewStatistics(user);
		return user;
	}

	private void createStudyStatistics(final User user) {
		final StudyStatistics statistics = StudyStatistics.of(user);
		studyStatisticsRepository.save(statistics);
	}

	private void createReviewStatistics(final User user) {
		final ReviewStatistics reviewStatistics = ReviewStatistics.of(user);
		reviewStatisticsRepository.save(reviewStatistics);
	}

}
