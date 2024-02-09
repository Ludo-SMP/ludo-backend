package com.ludo.study.studymatchingplatform.auth.kakao.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoOauthTokenDto;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoUserAccountDto;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoUserProfileDto;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.KakaoUserPropertiesDto;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.OauthUserSignupDto;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.AuthenticationResponse;
import com.ludo.study.studymatchingplatform.auth.kakao.service.dto.response.KakaoOauthRedirectResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoOauthService {

	private static final String BEARER = "Bearer ";

	private final UserRepositoryImpl userRepository;
	private final AuthService authService;
	private final OauthNetworkService oauthNetworkService;

	public KakaoOauthRedirectResponse makeSignupOauthUrl() {
		return oauthNetworkService.makeSignupOauthUrl();
	}

	public KakaoOauthRedirectResponse makeLoginOauthUrl() {
		return oauthNetworkService.makeLoginOauthUrl();
	}

	@Transactional
	public AuthenticationResponse login(final Map<String, String> queryParams) {
		final KakaoOauthTokenDto kakaoOauthTokenDto = oauthNetworkService.requestLoginToken(KakaoOauthTokenDto.class,
				queryParams).getBody();
		final KakaoUserProfileDto kakaoUserProfileDto = getKakaoUserProfileDto(kakaoOauthTokenDto.accessToken());
		final KakaoUserAccountDto kakaoUserAccountDto = kakaoUserProfileDto.kakao_account();

		final Optional<User> optionalUser = userRepository.findByEmail(kakaoUserAccountDto.email());

		if (optionalUser.isEmpty()) {
			throw new NotFoundException("존재하지 않는 사용자 입니다.");
		}

		final User user = optionalUser.get();
		return authService.oauthLogin(user);
	}

	@Transactional
	public AuthenticationResponse signup(final Map<String, String> queryParams) {
		final KakaoOauthTokenDto kakaoOauthTokenDto = oauthNetworkService.requestSignupToken(KakaoOauthTokenDto.class,
				queryParams).getBody();
		final KakaoUserProfileDto kakaoUserProfileDto = getKakaoUserProfileDto(kakaoOauthTokenDto.accessToken());
		final KakaoUserPropertiesDto kakaoUserPropertiesDto = kakaoUserProfileDto.properties();
		final KakaoUserAccountDto kakaoUserAccountDto = kakaoUserProfileDto.kakao_account();
		final Optional<User> optionalUser = userRepository.findByEmail(kakaoUserAccountDto.email());

		if (optionalUser.isPresent()) {
			throw new AuthenticationException("이미 가입된 사용자 입니다.");
		}

		// 가입되지 않은 사용자인 경우 회원가입 진행
		return authService.oauthJoin(new OauthUserSignupDto(Social.KAKAO,
				kakaoUserPropertiesDto.nickname(), kakaoUserAccountDto.email()));
	}

	private KakaoUserProfileDto getKakaoUserProfileDto(final String accessToken) {
		final Map<String, String> headers = Map.of(
				HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8",
				HttpHeaders.AUTHORIZATION, BEARER + accessToken);
		return oauthNetworkService.requestUserInfo(KakaoUserProfileDto.class, headers).getBody();
	}

}
