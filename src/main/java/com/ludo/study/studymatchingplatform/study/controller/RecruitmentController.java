package com.ludo.study.studymatchingplatform.study.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.IsAuthenticated;
import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.PopularRecruitmentCond;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCond;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCursor;
import com.ludo.study.studymatchingplatform.study.service.PopularRecruitmentsFindService;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentDetailsFindService;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentService;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentsFindService;
import com.ludo.study.studymatchingplatform.study.service.dto.request.ApplyRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.EditRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.ApplyRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.DeleteRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.EditRecruitmentResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.PopularRecruitmentsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentDetailsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponses;
import com.ludo.study.studymatchingplatform.study.service.dto.response.WriteRecruitmentResponse;
import com.ludo.study.studymatchingplatform.user.domain.User;

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
	public ResponseEntity<BaseApiResponse<PopularRecruitmentsResponse>> readPopularRecruitments(
			@ModelAttribute PopularRecruitmentCond request) {
		PopularRecruitmentsResponse popularRecruitments = popularRecruitmentsFindService.findPopularRecruitments(
				request);

		return ResponseEntity.ok(BaseApiResponse.success("인기 모집 공고 목록 조회 성공", popularRecruitments));
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseApiResponse<WriteRecruitmentResponse>> write(
			@RequestBody final WriteRecruitmentRequest request,
			@AuthUser final User user) {
		final Recruitment recruitment = recruitmentService.write(user, request);
		final WriteRecruitmentResponse response = WriteRecruitmentResponse.from(recruitment);

		return ResponseEntity.ok(BaseApiResponse.success("모집 공고 작성 성공", response));
	}

	@IsAuthenticated
	@PutMapping("/studies/{studyId}/recruitments")
	public ResponseEntity<BaseApiResponse<EditRecruitmentResponse>> edit(@AuthUser final User user,
																		 @PathVariable("studyId") final Long studyId,
																		 @PathVariable("recruitmentId") final Long recruitmentId,
																		 @RequestBody final EditRecruitmentRequest request) {
		final Recruitment recruitment = recruitmentService.edit(user, recruitmentId, request);
		return ResponseEntity.ok(BaseApiResponse.success("모집 공고 수정", EditRecruitmentResponse.from(recruitment)));
	}

	@IsAuthenticated
	@PostMapping("/studies/{studyId}/recruitments/{recruitmentId}/apply")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BaseApiResponse<ApplyRecruitmentResponse>> apply(
			@PathVariable("recruitmentId") final Long recruitmentId,
			@RequestBody final ApplyRecruitmentRequest request,
			@AuthUser final User user) {
		final Applicant applicant = recruitmentService.apply(user, recruitmentId, request);
		return ResponseEntity.ok(BaseApiResponse.success("지원 성공", ApplyRecruitmentResponse.from(applicant)));
	}

	@DeleteMapping("/studies/{studyId}/recruitments")
	public ResponseEntity<BaseApiResponse<DeleteRecruitmentResponse>> delete(@PathVariable Long studyId,
																			 @AuthUser final User user) {
		recruitmentService.delete(user, studyId);
		return ResponseEntity.ok(BaseApiResponse.success("모집 공고가 비활성화 되었습니다.", null));
	}

}
