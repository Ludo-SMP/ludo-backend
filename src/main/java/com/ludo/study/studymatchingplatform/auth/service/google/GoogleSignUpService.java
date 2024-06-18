package com.ludo.study.studymatchingplatform.auth.service.google;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.service.google.vo.GoogleOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.google.vo.GoogleUserProfile;
import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.repository.config.GlobalNotificationUserConfigRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.exception.DuplicatedSignUpException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleSignUpService {

	private final GoogleOAuthTokenRequestService googleOAuthTokenRequestService;
	private final GoogleProfileRequestService googleProfileRequestService;
	private final UserRepositoryImpl userRepository;
	private final StudyStatisticsRepositoryImpl studyStatisticsRepository;
	private final ReviewStatisticsRepositoryImpl reviewStatisticsRepository;

	private final GlobalNotificationUserConfigRepositoryImpl notificationUserConfigRepository;

	@Transactional
	public User googleSignUp(final String authorizationCode) {
		final GoogleOAuthToken oAuthToken = googleOAuthTokenRequestService.createOAuthToken(authorizationCode, true);
		final GoogleUserProfile userInfo = googleProfileRequestService.createGoogleUserInfo(
				oAuthToken.getAccessToken());

		validateAlreadySignUp(userInfo);

		return signup(userInfo);
	}

	private void validateAlreadySignUp(final GoogleUserProfile userInfo) {
		userRepository.findByEmail(userInfo.getEmail())
				.ifPresent(user -> {
					throw new DuplicatedSignUpException("이미 가입되어 있는 회원입니다.");
				});
	}

	private User signup(final GoogleUserProfile userInfo) {
		final User user = userRepository.save(userInfo.toUser());
		user.setInitialDefaultNickname();
		createStudyStatistics(user);
		createReviewStatistics(user);
		notificationUserConfigRepository.save(GlobalNotificationUserConfig.ofNewSignUpUser(user));
		return user;
	}

	private void createStudyStatistics(final User user) {
		final StudyStatistics studyStatistics = StudyStatistics.of(user);
		studyStatisticsRepository.save(studyStatistics);
	}

	private void createReviewStatistics(final User user) {
		final ReviewStatistics reviewStatistics = ReviewStatistics.of(user);
		reviewStatisticsRepository.save(reviewStatistics);
	}

}
