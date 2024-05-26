package com.ludo.study.studymatchingplatform.auth.service.google;

import com.ludo.study.studymatchingplatform.auth.service.google.vo.GoogleOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.google.vo.GoogleUserProfile;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleLoginService {

    private final GoogleOAuthTokenRequestService googleOAuthTokenRequestService;
    private final GoogleProfileRequestService googleProfileRequestService;
    private final UserRepositoryImpl userRepository;

    public User login(final String authorizationCode) {
        final GoogleOAuthToken oAuthToken = googleOAuthTokenRequestService.createOAuthToken(authorizationCode, false);
        final GoogleUserProfile userInfo = googleProfileRequestService.createGoogleUserInfo(
                oAuthToken.getAccessToken());

        final User user = validateNotSignUp(userInfo);

        return user;
    }

    private User validateNotSignUp(final GoogleUserProfile userInfo) {
        return userRepository.findByEmail(userInfo.getEmail())
                .orElseThrow(() -> new SocialAccountNotFoundException("가입되어 있지 않은 회원입니다."));
    }

}
