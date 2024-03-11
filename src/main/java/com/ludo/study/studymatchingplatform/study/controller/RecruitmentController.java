package com.ludo.study.studymatchingplatform.study.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.IsAuthenticated;
import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCond;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCursor;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.EditRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.applicant.ApplyRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.applicant.StudyApplicantDecisionRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.EditRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.PopularRecruitmentsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentDetailsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentPreviewResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentPreviewResponses;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.WriteRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.WriteRecruitmentStudyInfoResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplyAcceptResponse;
import com.ludo.study.studymatchingplatform.study.service.recruitment.PopularRecruitmentsFindService;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentDetailsFindService;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentService;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentsFindService;
import com.ludo.study.studymatchingplatform.study.service.study.participant.ParticipantDecisionService;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RecruitmentController {

	private final RecruitmentDetailsFindService recruitmentDetailsFindService;
	private final RecruitmentsFindService recruitmentsFindService;
	private final PopularRecruitmentsFindService popularRecruitmentsFindService;
	private final RecruitmentService recruitmentService;
	private final ParticipantDecisionService applicantDecisionService;

	@GetMapping("/recruitments")
	public ResponseEntity<BaseApiResponse<RecruitmentPreviewResponses>> readRecruitments(
			@RequestParam(required = false) Long last,
			@RequestParam Integer count,
			@RequestParam(required = false) Long category,
			@RequestParam(required = false) String way,
			@RequestParam(required = false) Long position,
			@RequestParam(required = false) List<Long> stacks) {

		final RecruitmentFindCursor recruitmentFindCursor = new RecruitmentFindCursor(last, count);
		final RecruitmentFindCond recruitmentFindCond = new RecruitmentFindCond(category, stacks, position, way);

		final List<RecruitmentPreviewResponse> recruitments = recruitmentsFindService.findRecruitments(
				recruitmentFindCursor, recruitmentFindCond);
		final RecruitmentPreviewResponses recruitmentPreviewResponses = new RecruitmentPreviewResponses(recruitments);

		return ResponseEntity.ok(BaseApiResponse.success(recruitmentPreviewResponses));
	}

	@GetMapping("/recruitments/{recruitmentId}")
	public ResponseEntity<BaseApiResponse<RecruitmentDetailsResponse>> readRecruitmentDetails(
			@PathVariable("recruitmentId") final Long recruitmentId
	) {
		try {
			final RecruitmentDetailsResponse recruitmentDetails = recruitmentDetailsFindService.findRecruitmentDetails(
					recruitmentId);

			return ResponseEntity.ok(BaseApiResponse.success(recruitmentDetails));
		} catch (IllegalArgumentException exception) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/recruitments/popular")
	public ResponseEntity<BaseApiResponse<PopularRecruitmentsResponse>> readPopularRecruitments() {
		PopularRecruitmentsResponse popularRecruitments = popularRecruitmentsFindService.findPopularRecruitments();

		return ResponseEntity.ok(BaseApiResponse.success(popularRecruitments));
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseApiResponse<WriteRecruitmentResponse>> write(@PathVariable("studyId") final Long studyId,
																		   @RequestBody final WriteRecruitmentRequest request,
																		   @AuthUser final User user) {
		final Recruitment recruitment = recruitmentService.write(user, request, studyId);
		final WriteRecruitmentResponse response = WriteRecruitmentResponse.from(recruitment);

		return ResponseEntity.ok(BaseApiResponse.success(response));
	}

	@GetMapping("/studies/{studyId}/recruitments")
	public ResponseEntity<BaseApiResponse<WriteRecruitmentStudyInfoResponse>> write(
			@PathVariable("studyId") final Long studyId,
			@AuthUser final User user) {
		final WriteRecruitmentStudyInfoResponse response = recruitmentService.getStudyInfo(user, studyId);
		return ResponseEntity.ok(BaseApiResponse.success(response));
	}

	@IsAuthenticated
	@PutMapping("/studies/{studyId}/recruitments")
	public ResponseEntity<BaseApiResponse<EditRecruitmentResponse>> edit(@AuthUser final User user,
																		 @PathVariable("studyId") final Long studyId,
																		 @RequestBody final EditRecruitmentRequest request) {
		final Recruitment recruitment = recruitmentService.edit(user, studyId, request);
		return ResponseEntity.ok(BaseApiResponse.success(EditRecruitmentResponse.from(recruitment)));
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/apply")
	public ResponseEntity<BaseApiResponse<Void>> apply(@PathVariable("recruitmentId") final Long recruitmentId,
													   @RequestBody final ApplyRecruitmentRequest request,
													   @AuthUser final User user) {
		recruitmentService.apply(user, recruitmentId, request);
		return ResponseEntity.ok(BaseApiResponse.success(null));
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/apply-accept/{applicantUserId}")
	public ResponseEntity<BaseApiResponse<ApplyAcceptResponse>> applicantAccept(@AuthUser final User user,
																				@PathVariable final Long studyId,
																				@PathVariable Long recruitmentId,
																				@PathVariable Long applicantUserId) {

		final StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(studyId,
				recruitmentId, applicantUserId);
		ApplyAcceptResponse applyAcceptResponse = applicantDecisionService.applicantAccept(user,
				studyApplicantDecisionRequest);

		return ResponseEntity.ok(BaseApiResponse.success(applyAcceptResponse));
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/apply-refuse/{applicantUserId}")
	public ResponseEntity<BaseApiResponse<Void>> applicantRefuse(@AuthUser final User user,
																 @PathVariable final Long studyId,
																 @PathVariable Long recruitmentId,
																 @PathVariable Long applicantUserId) {

		final StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(studyId,
				recruitmentId, applicantUserId);
		applicantDecisionService.applicantReject(user, studyApplicantDecisionRequest);

		return ResponseEntity.ok(BaseApiResponse.success(null));
	}

	@DeleteMapping("/studies/{studyId}/recruitments")
	public ResponseEntity<BaseApiResponse<Void>> delete(@PathVariable Long studyId,
														@AuthUser final User user) {
		recruitmentService.delete(user, studyId);
		return ResponseEntity.ok(BaseApiResponse.success(null));
	}

}
