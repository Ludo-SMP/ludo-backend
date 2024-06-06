package com.ludo.study.studymatchingplatform.auth.service.naver;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.service.naver.vo.NaverOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.naver.vo.NaverUserProfile;
import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.repository.config.GlobalNotificationUserConfigRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.exception.DuplicatedSignUpException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NaverSignUpService {

	private final NaverOAuthTokenRequestService naverOAuthTokenRequestService;
	private final NaverProfileRequestService naverProfileRequestService;
	private final UserRepositoryImpl userRepository;

	private final GlobalNotificationUserConfigRepositoryImpl notificationUserConfigRepository;

	@Transactional
	public User naverSignUp(final String authorizationCode) {
		final NaverOAuthToken oAuthToken = naverOAuthTokenRequestService.createOAuthToken(authorizationCode);
		final NaverUserProfile userProfile = naverProfileRequestService.createNaverProfile(oAuthToken);

		validateAlreadySignUp(userProfile);
		return signup(userProfile);
	}

	private void validateAlreadySignUp(final NaverUserProfile userProfile) {
		userRepository.findByEmail(userProfile.getEmail())
				.ifPresent(user -> {
					throw new DuplicatedSignUpException("이미 가입되어 있는 회원입니다.");
				});
	}

	private User signup(final NaverUserProfile userProfile) {
		User user = userRepository.save(userProfile.toUser());
		user.setInitialDefaultNickname();
		notificationUserConfigRepository.save(GlobalNotificationUserConfig.ofNewSignUpUser(user));
		return user;
	}

}
