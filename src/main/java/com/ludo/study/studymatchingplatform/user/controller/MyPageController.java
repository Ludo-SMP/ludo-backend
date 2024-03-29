package com.ludo.study.studymatchingplatform.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.IsAuthenticated;
import com.ludo.study.studymatchingplatform.study.controller.dto.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.service.MyPageService;
import com.ludo.study.studymatchingplatform.user.service.dto.response.MyPageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageService;

	@IsAuthenticated
	@GetMapping("/users/mypage")
	public ResponseEntity<BaseApiResponse<MyPageResponse>> retrieveMyPage(
			@CookieValue(name = "Authorization") final String auth,
			@AuthUser final User user) {
		final List<Participant> participants = myPageService.retrieveParticipantStudies(user);
		final List<Applicant> applicants = myPageService.retrieveApplicantRecruitment(user);
		final List<Participant> completedStudies = myPageService.retrieveCompletedStudy(user);
		final MyPageResponse response = MyPageResponse.from(participants, applicants, completedStudies);
		return ResponseEntity.ok(BaseApiResponse.success("마이페이지 조회가 완료되었습니다.", response));
	}

}
