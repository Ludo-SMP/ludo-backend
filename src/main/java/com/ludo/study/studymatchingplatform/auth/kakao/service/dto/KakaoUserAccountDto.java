package com.ludo.study.studymatchingplatform.auth.kakao.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.LowerCaseStrategy.class)
public record KakaoUserAccountDto(

		String email

) {
}
