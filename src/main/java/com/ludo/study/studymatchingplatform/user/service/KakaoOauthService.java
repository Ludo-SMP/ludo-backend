package com.ludo.study.studymatchingplatform.user.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.service.dto.KakaoOauthTokenDto;
import com.ludo.study.studymatchingplatform.user.service.dto.OauthUserJoinDto;
import com.ludo.study.studymatchingplatform.user.service.dto.response.AuthenticationResponse;
import com.ludo.study.studymatchingplatform.user.service.dto.response.KakaoOauthRedirectResponse;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoOauthService {

	private static final String BEARER = "Bearer ";

	private final UserRepositoryImpl userRepository;
	private final AuthService authService;
	private final UserJoinService userJoinService;
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
		final String body = getKakaoUserProfileDto(kakaoOauthTokenDto.accessToken());
		final JsonElement element = JsonParser.parseString(body.toString());
		final JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
		final String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
		final Optional<User> optionalUser = userRepository.findByEmail(email);

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
		final String body = getKakaoUserProfileDto(kakaoOauthTokenDto.accessToken());
		JsonElement element = JsonParser.parseString(body.toString());
		JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
		JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
		final String nickname = properties.getAsJsonObject().get("nickname").getAsString();
		final String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
		final Optional<User> optionalUser = userRepository.findByEmail(email);

		if (optionalUser.isPresent()) {
			throw new AuthenticationException("이미 가입된 사용자 입니다.");
		}

		// 가입되지 않은 사용자인 경우 회원가입 진행
		return userJoinService.oauthJoin(
				new OauthUserJoinDto(
						Social.KAKAO,
						nickname, email,
						kakaoOauthTokenDto.refreshToken(), kakaoOauthTokenDto.accessToken()
				)
		);
	}

	private String getKakaoUserProfileDto(final String accessToken) {
		final Map<String, String> headers = Map.of(
				HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8",
				HttpHeaders.AUTHORIZATION, BEARER + accessToken);
		return oauthNetworkService.requestUserInfo(String.class, headers).getBody();
	}

}
