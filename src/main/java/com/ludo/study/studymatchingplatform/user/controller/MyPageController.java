package com.ludo.study.studymatchingplatform.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.user.domain.User;
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

	@GetMapping("/users/mypage")
	@ResponseStatus(HttpStatus.CREATED)
	@DataFieldName("user")
	@Operation(description = "로그인 된 사용자 정보 조회")
	@ApiResponse(description = "조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public MyPageResponse retrieveMyPage(
			@CookieValue(name = "Authorization") final String auth,
			@Parameter(hidden = true) @AuthUser final User user) {
		final List<Participant> participants = myPageService.retrieveParticipantStudies(user);
		final List<Applicant> applicants = myPageService.retrieveApplicantRecruitment(user);
		final List<Participant> completedStudies = myPageService.retrieveCompletedStudy(user);
		return MyPageResponse.from(participants, applicants, completedStudies);
	}

}
