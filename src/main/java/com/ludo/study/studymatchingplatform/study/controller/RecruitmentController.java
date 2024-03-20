package com.ludo.study.studymatchingplatform.study.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.PopularRecruitmentCond;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCond;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCursor;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.EditRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.applicant.ApplyRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.EditRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.PopularRecruitmentsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentDetailsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentPreviewResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentPreviewResponses;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.WriteRecruitmentStudyInfoResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplyRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.recruitment.PopularRecruitmentsFindService;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentDetailsFindService;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentService;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentsFindService;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

	@GetMapping("/recruitments")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "여러 모집 공고 조회")
	@ApiResponse(description = "조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public RecruitmentPreviewResponses readRecruitments(
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
		return new RecruitmentPreviewResponses(recruitments);
	}

	@GetMapping("/recruitments/{recruitmentId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "특정 모집 공고 조회")
	@ApiResponse(description = "조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public RecruitmentDetailsResponse readRecruitmentDetails(@PathVariable("recruitmentId") final Long recruitmentId) {
		return recruitmentDetailsFindService.findRecruitmentDetails(recruitmentId);
	}

	@GetMapping("/recruitments/popular")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "인기 있는 다수의 모집 공고 조회")
	@ApiResponse(description = "조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public PopularRecruitmentsResponse readPopularRecruitments(@ModelAttribute PopularRecruitmentCond request) {
		return popularRecruitmentsFindService.findPopularRecruitments(request);
	}

	@PostMapping("/studies/{studyId}/recruitments")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(description = "모집 공고 작성")
	@ApiResponse(description = "모집 공고 작성 성공", responseCode = "201", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public RecruitmentDetailsResponse write(@PathVariable("studyId") final Long studyId,
											@RequestBody final WriteRecruitmentRequest request,
											@Parameter(hidden = true) @AuthUser final User user) {
		final Recruitment recruitment = recruitmentService.write(user, request, studyId);
		return new RecruitmentDetailsResponse(recruitment, recruitment.getStudy());
	}

	@GetMapping("/studies/{studyId}/recruitments")
	@ResponseStatus(HttpStatus.OK)
	@DataFieldName("study")
	@Operation(description = "모집 공고 생성시 필요한 간략한 스터디 정보")
	@ApiResponse(description = "스터디 정보 조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public WriteRecruitmentStudyInfoResponse write(
			@PathVariable("studyId") final Long studyId,
			@Parameter(hidden = true) @AuthUser final User user) {
		return recruitmentService.getStudyInfo(user, studyId);
	}

	@PutMapping("/studies/{studyId}/recruitments")
	@ResponseStatus(HttpStatus.OK)
	@DataFieldName("recruitment")
	@Operation(description = "모집 공고 수정")
	@ApiResponse(description = "모집 공고 수정 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	@Parameters({
			@Parameter(name = "studyId", description = "모집 공고에 대한 스터디 id", required = true),
			@Parameter(name = "recruitmentId", description = "지원할 모집 공고 id", required = true)
	})
	public EditRecruitmentResponse edit(@Parameter(hidden = true) @AuthUser final User user,
										@PathVariable("studyId") final Long studyId,
										@PathVariable("recruitmentId") final Long recruitmentId,
										@RequestBody final EditRecruitmentRequest request) {
		return EditRecruitmentResponse.from(recruitmentService.edit(user, recruitmentId, request));

	}

	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/apply")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "모집 공고 지원")
	@ApiResponse(description = "모집 공고 지원 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public ApplyRecruitmentResponse apply(@PathVariable("recruitmentId") final Long recruitmentId,
										  @RequestBody final ApplyRecruitmentRequest request,
										  @Parameter(hidden = true) @AuthUser final User user) {
		return ApplyRecruitmentResponse.from(recruitmentService.apply(user, recruitmentId, request));
	}

	@DeleteMapping("/studies/{studyId}/recruitments")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "모집 공고 삭제")
	@ApiResponse(description = "모집 공고 삭제 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public void delete(@PathVariable Long studyId,
					   @Parameter(hidden = true) @AuthUser final User user) {
		recruitmentService.delete(user, studyId);
	}

	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/cancel")
	@ResponseStatus(HttpStatus.OK)
	@Operation(description = "모집 공고 지원 취소")
	@ApiResponse(description = "지원 취소", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public void cancel(
			@Parameter(name = "studyId", description = "스터디 id", required = true) @PathVariable("studyId") final Long studyId,
			@Parameter(name = "recruitmentId", description = "모집 공고 id", required = true) @PathVariable("recruitmentId") final Long recruitmentId,
			@Parameter(hidden = true) @AuthUser final User user) {
		recruitmentService.cancel(user, recruitmentId);
	}

}
