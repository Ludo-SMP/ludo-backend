package com.ludo.study.studymatchingplatform.study.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.ludo.study.studymatchingplatform.auth.common.IsAuthenticated;
import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.study.StudyCreateService;
import com.ludo.study.studymatchingplatform.study.service.study.StudyFetchService;
import com.ludo.study.studymatchingplatform.study.service.study.StudyService;
import com.ludo.study.studymatchingplatform.study.service.study.StudyStatusService;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

	private final StudyCreateService studyCreateService;
	private final StudyFetchService studyFetchService;
	private final StudyStatusService studyStatusService;
	private final StudyService studyService;

	@IsAuthenticated
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseApiResponse<StudyResponse>> create(@RequestBody final WriteStudyRequest request,
																 @AuthUser final User user) {
		final Study study = studyCreateService.create(request, user);
		final StudyResponse response = StudyResponse.from(study);
		return ResponseEntity.ok(BaseApiResponse.success(response));
	}

	@IsAuthenticated
	@Transactional
	@PatchMapping("/{studyId}")
	public ResponseEntity<BaseApiResponse<StudyResponse>> changeStatus(@PathVariable final Long studyId,
																	   @RequestParam("status") final StudyStatus status,
																	   @AuthUser final User user) {
		final Study study = studyStatusService.changeStatus(studyId, status, user);
		final StudyResponse response = StudyResponse.from(study);
		return ResponseEntity.ok(BaseApiResponse.success(response));
	}

	@DeleteMapping("/{studyId}/participants")
	public ResponseEntity<BaseApiResponse<Void>> leave(@AuthUser final User user,
													   @PathVariable("studyId") final Long studyId) {
		studyService.leave(user, studyId);
		return ResponseEntity.ok(BaseApiResponse.success(null));
	}

	@GetMapping("/{studyId}")
	public ResponseEntity<BaseApiResponse<StudyResponse>> getStudyDetails(@AuthUser final User user,
																		  @PathVariable("studyId") final Long studyId) {
		final Study study = studyFetchService.getStudyDetails(user, studyId);
		final StudyResponse response = StudyResponse.from(study);
		return ResponseEntity.ok(BaseApiResponse.success(response));
	}

	@GetMapping("/{studyId}/applicants")
	public ResponseEntity<BaseApiResponse<ApplicantResponse>> findApplicantsInfo(@AuthUser final User user,
																				 @PathVariable("studyId") final Long studyId) {
		final ApplicantResponse response = studyService.findApplicantsInfo(user, studyId);
		return ResponseEntity.ok(BaseApiResponse.success(response));
	}

}
