package com.ludo.study.studymatchingplatform.auth.service.naver;

import com.ludo.study.studymatchingplatform.auth.service.naver.vo.NaverOAuthToken;
import com.ludo.study.studymatchingplatform.auth.service.naver.vo.NaverUserProfile;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverLoginService {

    private final NaverOAuthTokenRequestService naverOAuthTokenRequestService;
    private final NaverProfileRequestService naverProfileRequestService;
    private final UserRepositoryImpl userRepository;

    public User login(final String authorizationCode) {
        final NaverOAuthToken oAuthToken = naverOAuthTokenRequestService.createOAuthToken(authorizationCode);
        final NaverUserProfile profileResponse = naverProfileRequestService.createNaverProfile(oAuthToken);

        return validateNotSignUp(profileResponse);
    }

    private User validateNotSignUp(final NaverUserProfile profileResponse) {
        return userRepository.findByEmail(profileResponse.getEmail())
                .orElseThrow(() -> new SocialAccountNotFoundException("가입되어 있지 않은 회원입니다."));
    }

}
