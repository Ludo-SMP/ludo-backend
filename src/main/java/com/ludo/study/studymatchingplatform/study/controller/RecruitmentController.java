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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.IsAuthenticated;
import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCond;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCursor;
import com.ludo.study.studymatchingplatform.study.service.PopularRecruitmentsFindService;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentDetailsFindService;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentService;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentsFindService;
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
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponses;
import com.ludo.study.studymatchingplatform.study.service.dto.response.WriteRecruitmentResponse;
import com.ludo.study.studymatchingplatform.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class RecruitmentController {

	private final RecruitmentDetailsFindService recruitmentDetailsFindService;
	private final RecruitmentsFindService recruitmentsFindService;
	private final PopularRecruitmentsFindService popularRecruitmentsFindService;
	private final RecruitmentService recruitmentService;
	private final StudyApplicantDecisionService applicantDecisionService;

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

		return ResponseEntity.ok(BaseApiResponse.success("모집 공고 목록 조회 성공", recruitmentPreviewResponses));
	}

	@GetMapping("/recruitments/{recruitmentId}")
	public ResponseEntity<BaseApiResponse<RecruitmentDetailsResponse>> readRecruitmentDetails(
			@PathVariable("recruitmentId") final Long recruitmentId
	) {
		try {
			final RecruitmentDetailsResponse recruitmentDetails = recruitmentDetailsFindService.findRecruitmentDetails(
					recruitmentId);

			return ResponseEntity.ok(BaseApiResponse.success("모집 공고 상세 조회 성공", recruitmentDetails));
		} catch (IllegalArgumentException exception) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/recruitments/popular")
	public ResponseEntity<BaseApiResponse<PopularRecruitmentsResponse>> readPopularRecruitments() {
		PopularRecruitmentsResponse popularRecruitments = popularRecruitmentsFindService.findPopularRecruitments();

		return ResponseEntity.ok(BaseApiResponse.success("인기 모집 공고 목록 조회 성공", popularRecruitments));
	}

	@Operation(description = "모집 공고 작성")
	@ApiResponse(description = "모집 공고 작성 성공", responseCode = "201", useReturnTypeSchema = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = WriteRecruitmentResponse.class)))
	@PostMapping("/studies/{studyId}/recruitments")
	public ResponseEntity<BaseApiResponse<WriteRecruitmentResponse>> write(
			@RequestBody final WriteRecruitmentRequest request, @Parameter(hidden = true) @AuthUser final User user) {
		final Recruitment recruitment = recruitmentService.write(user, request);
		final WriteRecruitmentResponse response = WriteRecruitmentResponse.from(recruitment);

		return new ResponseEntity<>(BaseApiResponse.success("모집 공고 작성 성공", response), HttpStatus.CREATED);
	}

	@PutMapping("/studies/{studyId}/recruitments/{recruitmentId}")
	@Operation(description = "모집 공고 수정")
	@ApiResponse(description = "모집 공고 수정 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditRecruitmentResponse.class)))
	@Parameters({
			@Parameter(name = "studyId", description = "모집 공고에 대한 스터디 id", required = true),
			@Parameter(name = "recruitmentId", description = "지원할 모집 공고 id", required = true)
	})
	public ResponseEntity<BaseApiResponse<EditRecruitmentResponse>> edit(
			@Parameter(hidden = true) @AuthUser final User user,
			@PathVariable("studyId") final Long studyId,
			@PathVariable("recruitmentId") final Long recruitmentId,
			@RequestBody final EditRecruitmentRequest request) {
		final Recruitment recruitment = recruitmentService.edit(user, recruitmentId, request);
		return ResponseEntity.ok(BaseApiResponse.success("모집 공고 수정", EditRecruitmentResponse.from(recruitment)));
	}

	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/apply")
	@Operation(description = "모집 공고 지원")
	@ApiResponse(description = "모집 공고 지원 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplyRecruitmentResponse.class)))
	public ResponseEntity<BaseApiResponse<ApplyRecruitmentResponse>> apply(
			@Parameter(description = "지원할 모집 공고 id", required = true) @PathVariable("recruitmentId") final Long recruitmentId,
			@RequestBody final ApplyRecruitmentRequest request,
			@Parameter(hidden = true) @AuthUser final User user) {
		final Applicant applicant = recruitmentService.apply(user, recruitmentId, request);
		return ResponseEntity.ok(BaseApiResponse.success("지원 성공", ApplyRecruitmentResponse.from(applicant)));
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/apply-accept/{applicantUserId}")
	public ResponseEntity<BaseApiResponse<ParticipantResponse>> applicantAccept(@AuthUser final User user,
																				@PathVariable final Long studyId,
																				@PathVariable Long recruitmentId,
																				@PathVariable Long applicantUserId) {

		final StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(studyId,
				recruitmentId, applicantUserId);
		ParticipantResponse participantResponse = applicantDecisionService.applicantAccept(user,
				studyApplicantDecisionRequest);

		return ResponseEntity.ok(BaseApiResponse.success("지원자 수락 성공", participantResponse));
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

		return ResponseEntity.ok(new BaseApiResponse<>(true, "지원자 거절 성공", null));
	}

	@DeleteMapping("/studies/{studyId}/recruitments/{recruitmentid}")
	@ApiResponse(description = "모집 공고 삭제", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteRecruitmentResponse.class)))
	public ResponseEntity<BaseApiResponse<DeleteRecruitmentResponse>> delete(
			@Parameter(name = "studyId", description = "모집 공고를 삭제 할 스터디 id", required = true) @PathVariable("studyId") Long studyId,
			@Parameter(hidden = true) @AuthUser final User user) {
		recruitmentService.delete(user, studyId);
		return ResponseEntity.ok(BaseApiResponse.success("모집 공고가 삭제 되었습니다.", null));
	}

	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/cancel")
	@ApiResponse(description = "지원 취소", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseApiResponse.class)))
	public ResponseEntity<BaseApiResponse<Void>> cancel(
			@Parameter(name = "studyId", description = "스터디 id", required = true) @PathVariable("studyId") final Long studyId,
			@Parameter(name = "recruitmentId", description = "모집 공고 id", required = true) @PathVariable("recruitmentId") final Long recruitmentId,
			@Parameter(hidden = true) @AuthUser final User user) {
		recruitmentService.cancel(user, recruitmentId);
		return ResponseEntity.ok(BaseApiResponse.success("지원이 정상적으로 취소 되었습니다.", null));
	}
}
