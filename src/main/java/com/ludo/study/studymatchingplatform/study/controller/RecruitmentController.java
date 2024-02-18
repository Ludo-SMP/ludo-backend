package com.ludo.study.studymatchingplatform.study.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.IsAuthenticated;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentDetailsFindService;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentService;
import com.ludo.study.studymatchingplatform.study.service.dto.EditRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.ApplyRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.EditRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentDetailsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.WriteRecruitmentResponse;
import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping
@RequiredArgsConstructor
public class RecruitmentController {

	private final RecruitmentDetailsFindService recruitmentDetailsFindService;

	private final RecruitmentService recruitmentService;

	@GetMapping("/recruitments/{recruitmentId}")
	public ResponseEntity<RecruitmentDetailsResponse> readRecruitmentDetails(
			@PathVariable("recruitment-id") final Long recruitmentId,
			@AuthUser AuthUserPayload payload
	) {
		System.out.println(payload);
		try {
			RecruitmentDetailsResponse recruitmentDetails = recruitmentDetailsFindService.findRecruitmentDetails(
					recruitmentId);

			return ResponseEntity.ok(recruitmentDetails);
		} catch (IllegalArgumentException exception) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments")
	public ResponseEntity<WriteRecruitmentResponse> write(@RequestBody final WriteRecruitmentRequest request,
														  @AuthUser final User user) {
		final Recruitment recruitment = recruitmentService.write(user, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(WriteRecruitmentResponse.from(recruitment));
	}

	@Profile("test")
	@IsAuthenticated
	@PutMapping("/studies/{studyId}/recruitments")
	public ResponseEntity<EditRecruitmentResponse> edit(@AuthUser final User user,
														@PathVariable("studyId") final Long studyId,
														@PathVariable("recruitmentId") final Long recruitmentId,
														@RequestBody final EditRecruitmentRequest request) {
		// TODO: need to append authorization guard
		final Recruitment recruitment = recruitmentService.edit(user, recruitmentId, request);

		return ResponseEntity.status(HttpStatus.OK).body(EditRecruitmentResponse.from(recruitment));
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/apply")
	public ResponseEntity<ApplyRecruitmentResponse> apply(@PathVariable("recruitmentId") final Long recruitmentId) {
		// TODO: need to append authorization guard
		final Applicant applicant = recruitmentService.apply(null, recruitmentId);

		return ResponseEntity.status(HttpStatus.CREATED).body(ApplyRecruitmentResponse.from(applicant));
	}

}
