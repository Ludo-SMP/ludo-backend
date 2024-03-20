package com.ludo.study.studymatchingplatform.user.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
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
	@Operation(description = "로그인 된 사용자 정보 조회")
	@ApiResponse(description = "조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public MyPageResponse retrieveMyPage(@CookieValue(name = "Authorization") final String auth,
										 @Parameter(hidden = true) @AuthUser final User user) {
		return myPageService.retrieveMyPage(user);
	}

	@DeleteMapping("/users/recruitments/{recruitmentId}/apply-history")
	@Operation(description = "모집 공고 지원 기록 삭제")
	@ApiResponse(description = "조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public void deleteApplyHistory(@PathVariable Long recruitmentId,
								   @Parameter(hidden = true) @AuthUser final User user) {
		recruitmentService.deleteApplyHistory(user, recruitmentId);
	}

}
