package com.ludo.study.studymatchingplatform.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.IsAuthenticated;
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
	public ResponseEntity<MyPageResponse> retrieveMyPage(@CookieValue(name = "Authorization") final String auth,
														 @AuthUser final User user) {
		final List<Participant> participants = myPageService.retrieveParticipantStudies(user);
		final List<Applicant> applicants = myPageService.retrieveApplicantRecruitment(user);
		final List<Participant> completedStudies = myPageService.retrieveCompletedStudy(user);
		return ResponseEntity.status(HttpStatus.OK)
				.body(MyPageResponse.from(participants, applicants, completedStudies));
	}

}
