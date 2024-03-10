package com.ludo.study.studymatchingplatform.auth.service.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.LowerCaseStrategy.class)
public record KakaoUserPropertiesDto(

		String nickname

) {
}
