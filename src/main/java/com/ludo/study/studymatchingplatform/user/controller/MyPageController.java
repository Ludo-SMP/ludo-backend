package com.ludo.study.studymatchingplatform.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentService;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.service.MyPageService;
import com.ludo.study.studymatchingplatform.user.service.dto.response.MyPageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageService;
	private final RecruitmentService recruitmentService;

	@GetMapping("/users/mypage")
	@DataFieldName("user")
	@Operation(description = "로그인 된 사용자 정보 조회")
	@ApiResponse(description = "조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public MyPageResponse retrieveMyPage(
			@CookieValue(name = "Authorization") final String auth,
			@Parameter(hidden = true) @AuthUser final User user) {
		return myPageService.retrieveMyPage(user);
	}

	@DeleteMapping("/users/recruitments/{recruitmentId}/apply-history")
	public ResponseEntity<BaseApiResponse<Void>> deleteApplyHistory(@PathVariable Long recruitmentId,
																	@AuthUser final User user) {
		recruitmentService.deleteApplyHistory(user, recruitmentId);
		return ResponseEntity.ok(BaseApiResponse.success(null));
	}

}
