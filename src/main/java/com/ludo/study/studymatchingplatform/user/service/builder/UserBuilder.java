package com.ludo.study.studymatchingplatform.user.service.builder;

import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.service.dto.OauthUserJoinDto;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserBuilder {

	public static OauthUserJoinDto convertToUserCreateDto(final String body) {
		JsonElement element = JsonParser.parseString(body.toString());
		JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
		JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

		final String nickname = properties.getAsJsonObject().get("nickname").getAsString();
		final String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

		return OauthUserJoinDto.builder()
				.nickname(nickname)
				.email(email)
				.social(Social.KAKAO)
				.build();
	}
}
