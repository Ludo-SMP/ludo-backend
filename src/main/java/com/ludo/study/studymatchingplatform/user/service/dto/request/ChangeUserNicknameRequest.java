package com.ludo.study.studymatchingplatform.user.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record ChangeUserNicknameRequest(
		@NotBlank
		@NotEmpty
		@Pattern(regexp = "^\\S(.*\\S)?$", message = "맨 앞, 맨 뒤는 공백일 수 없습니다.")
		String changeNickname
) {
}
