package com.ludo.study.studymatchingplatform.study.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.applicant.StudyApplicantDecisionRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.StudyUpdateRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplyAcceptResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.recruitment.applicant.StudyApplicantDecisionService;
import com.ludo.study.studymatchingplatform.study.service.study.StudyCreateService;
import com.ludo.study.studymatchingplatform.study.service.study.StudyFetchService;
import com.ludo.study.studymatchingplatform.study.service.study.StudyService;
import com.ludo.study.studymatchingplatform.study.service.study.StudyStatusService;
import com.ludo.study.studymatchingplatform.study.service.study.StudyUpdateService;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

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
	private final StudyUpdateService studyUpdateService;
	private final StudyApplicantDecisionService applicantDecisionService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@DataFieldName("study")
	@Operation(description = "스터디 생성")
	@ApiResponse(description = "스터디 생성 성공", responseCode = "201", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public StudyResponse create(@RequestBody final WriteStudyRequest request,
								@Parameter(hidden = true) @AuthUser final User user) {
		return studyCreateService.create(request, user);
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
		return studyStatusService.changeStatus(studyId, status, user);
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
	public StudyResponse getStudyDetails(@Parameter(hidden = true) @AuthUser final User user,
										 @PathVariable("studyId") final Long studyId) {
		return studyFetchService.getStudyDetails(user, studyId);
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

	@GetMapping("/{studyId}/applicants")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "스터디 지원 지원자 정보")
	@ApiResponse(description = "스터디 지원자 정보 조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public ResponseEntity<BaseApiResponse<ApplicantResponse>> findApplicantsInfo(
			@Parameter(hidden = true) @AuthUser final User user,
			@PathVariable("studyId") final Long studyId) {
		final ApplicantResponse response = studyService.findApplicantsInfo(user, studyId);
		return ResponseEntity.ok(BaseApiResponse.success(response));
	}

	@PutMapping("/{studyId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "스터디 지원 지원자 정보")
	@ApiResponse(description = "스터디 지원자 정보 조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public ResponseEntity<BaseApiResponse<StudyResponse>> update(@Parameter(hidden = true) @AuthUser final User user,
																 @PathVariable final Long studyId,
																 @RequestBody final StudyUpdateRequest request) {
		final StudyResponse response = studyUpdateService.update(user, studyId, request);
		return ResponseEntity.ok(BaseApiResponse.success(response));
	}

}
