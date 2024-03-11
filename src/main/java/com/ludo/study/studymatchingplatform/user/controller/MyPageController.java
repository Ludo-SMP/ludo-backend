package com.ludo.study.studymatchingplatform.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.IsAuthenticated;
import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentService;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.service.MyPageService;
import com.ludo.study.studymatchingplatform.user.service.dto.response.MyPageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageService;
	private final RecruitmentService recruitmentService;

	@IsAuthenticated
	@GetMapping("/users/mypage")
	public ResponseEntity<BaseApiResponse<MyPageResponse>> retrieveMyPage(
			@CookieValue(name = "Authorization") final String auth,
			@AuthUser final User user) {
		final MyPageResponse response = myPageService.retrieveMyPage(user);
		return ResponseEntity.ok(BaseApiResponse.success(response));
	}

	@DeleteMapping("/users/recruitments/{recruitmentId}/apply-history")
	public ResponseEntity<BaseApiResponse<Void>> deleteApplyHistory(@PathVariable Long recruitmentId,
																	@AuthUser final User user) {
		recruitmentService.deleteApplyHistory(user, recruitmentId);
		return ResponseEntity.ok(BaseApiResponse.success(null));
	}

}
