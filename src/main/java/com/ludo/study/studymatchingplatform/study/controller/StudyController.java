package com.ludo.study.studymatchingplatform.study.controller;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.service.StudyApplicantDecisionService;
import com.ludo.study.studymatchingplatform.study.service.StudyCreateService;
import com.ludo.study.studymatchingplatform.study.service.StudyFetchService;
import com.ludo.study.studymatchingplatform.study.service.StudyService;
import com.ludo.study.studymatchingplatform.study.service.StudyStatusService;
import com.ludo.study.studymatchingplatform.study.service.dto.request.StudyApplicantDecisionRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.ApplyAcceptResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.StudyResponse;
import com.ludo.study.studymatchingplatform.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

	private final StudyCreateService studyCreateService;
	private final StudyFetchService studyFetchService;
	private final StudyStatusService studyStatusService;
	private final StudyService studyService;
	private final StudyApplicantDecisionService applicantDecisionService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@DataFieldName("study")
	@Operation(description = "스터디 생성")
	@ApiResponse(description = "스터디 생성 성공", responseCode = "201", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public StudyResponse create(@RequestBody final WriteStudyRequest request,
								@Parameter(hidden = true) @AuthUser final User user) {
		final Study study = studyCreateService.create(request, user);
		return StudyResponse.from(study);
	}

	@Transactional
	@PatchMapping("/{studyId}")
	@DataFieldName("study")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "스터디 상태 변경")
	@ApiResponse(description = "스터디 상태 변경 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public StudyResponse changeStatus(@PathVariable final Long studyId,
									  @RequestParam("status") final StudyStatus status,
									  @Parameter(hidden = true) @AuthUser final User user) {
		final Study study = studyStatusService.changeStatus(studyId, status, user);
		return StudyResponse.from(study);
	}

	@DeleteMapping("/{studyId}/participants")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "스터디 탈퇴")
	@ApiResponse(description = "스터디 탈퇴 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public void leave(@Parameter(hidden = true) @AuthUser final User user,
					  @PathVariable("studyId") final Long studyId) {
		studyService.leave(user, studyId);
	}

	@GetMapping("/{studyId}")
	@ResponseStatus(HttpStatus.OK)
	@DataFieldName("study")
	@Operation(description = "스터디 상세 정보 조회")
	@ApiResponse(description = "스터디 조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public StudyResponse getStudyDetails(
			@Parameter(hidden = true) @AuthUser final User user,
			@PathVariable("studyId") final Long studyId) {
		final Study study = studyFetchService.getStudyDetails(user, studyId);
		return StudyResponse.from(study);
	}

	@PostMapping("/{studyId}/apply-accept/{applicantUserId}")
	@ResponseStatus(HttpStatus.OK)
	@DataFieldName("participant")
	@Operation(description = "스터디 지원 수락")
	@ApiResponse(description = "스터디 지원 수락 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public ApplyAcceptResponse applicantAccept(@Parameter(hidden = true) @AuthUser final User user,
											   @PathVariable final Long studyId,
											   @PathVariable Long applicantUserId) {

		final StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(studyId,
				applicantUserId);
		return applicantDecisionService.applicantAccept(user,
				studyApplicantDecisionRequest);
	}

	@PostMapping("/{studyId}/apply-refuse/{applicantUserId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "스터디 지원 거절")
	@ApiResponse(description = "스터디 지원 거절 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public void applicantRefuse(@Parameter(hidden = true) @AuthUser final User user,
								@PathVariable final Long studyId,
								@PathVariable Long applicantUserId) {

		final StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(studyId,
				applicantUserId);
		applicantDecisionService.applicantReject(user, studyApplicantDecisionRequest);
	}

}
