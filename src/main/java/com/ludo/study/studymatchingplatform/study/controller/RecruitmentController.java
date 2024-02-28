package com.ludo.study.studymatchingplatform.study.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.IsAuthenticated;
import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.service.PopularRecruitmentsFindService;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentDetailsFindService;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentService;
import com.ludo.study.studymatchingplatform.study.service.StudyApplicantDecisionService;
import com.ludo.study.studymatchingplatform.study.service.dto.request.ApplyRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.EditRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.StudyApplicantDecisionRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.ApplyRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.DeleteRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.EditRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.ParticipantResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.PopularRecruitmentsResponse;
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
	private final PopularRecruitmentsFindService popularRecruitmentsFindService;

	private final RecruitmentService recruitmentService;

	private final StudyApplicantDecisionService applicantDecisionService;

	@GetMapping("/recruitments/{recruitmentId}")
	public ResponseEntity<RecruitmentDetailsResponse> readRecruitmentDetails(
			@PathVariable("recruitment-id") final Long recruitmentId
	) {
		try {
			RecruitmentDetailsResponse recruitmentDetails = recruitmentDetailsFindService.findRecruitmentDetails(
					recruitmentId);

			return ResponseEntity.ok(recruitmentDetails);
		} catch (IllegalArgumentException exception) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/popular")
	public ResponseEntity<BaseApiResponse<PopularRecruitmentsResponse>> readPopularRecruitments() {
		PopularRecruitmentsResponse popularRecruitments = popularRecruitmentsFindService.findPopularRecruitments();

		return ResponseEntity.ok(BaseApiResponse.success("인기 모집 공고 목록 조회 성공", popularRecruitments));
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
	public ResponseEntity<ApplyRecruitmentResponse> apply(@PathVariable("recruitmentId") final Long recruitmentId,
														  @RequestBody final ApplyRecruitmentRequest request,
														  @AuthUser final User user) {
		// TODO: need to append authorization guard

		final Applicant applicant = recruitmentService.apply(user, recruitmentId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApplyRecruitmentResponse.from(applicant));
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/apply-accept/{applicantUserId}")
	public ResponseEntity<BaseApiResponse<ParticipantResponse>> applicantAccept(@AuthUser final User user,
																				@PathVariable final Long studyId,
																				@PathVariable Long recruitmentId,
																				@PathVariable Long applicantUserId,
																				@RequestBody Long positionId) {

		final StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(studyId,
				recruitmentId, applicantUserId);
		ParticipantResponse participantResponse = applicantDecisionService.applicantAccept(user,
				studyApplicantDecisionRequest);

		return ResponseEntity.ok(BaseApiResponse.success("지원자 수락 성공", participantResponse));
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/apply-refuse/{applicantUserId}")
	public ResponseEntity<BaseApiResponse> applicantRefuse(@AuthUser final User user,
														   @PathVariable final Long studyId,
														   @PathVariable Long recruitmentId,
														   @PathVariable Long applicantUserId) {

		final StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(studyId,
				recruitmentId, applicantUserId);
		applicantDecisionService.applicantReject(user, studyApplicantDecisionRequest);

		return ResponseEntity.ok(new BaseApiResponse(true, "지원자 거절 성공", null));
	}

	@DeleteMapping("/studies/{studyId}/recruitments")
	public ResponseEntity<DeleteRecruitmentResponse> delete(@PathVariable Long studyId, @AuthUser final User user) {
		recruitmentService.delete(user, studyId);

		return ResponseEntity.status(HttpStatus.OK).body(
				DeleteRecruitmentResponse.from("모집 공고가 비활성화 되었습니다."));
	}

}
