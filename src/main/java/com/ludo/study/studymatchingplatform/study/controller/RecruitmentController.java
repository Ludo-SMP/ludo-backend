package com.ludo.study.studymatchingplatform.study.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.study.service.RecruitmentDetailsFindService;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentsFindService;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentDetailsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/recruitments")
@RequiredArgsConstructor
public class RecruitmentController {

	private final RecruitmentDetailsFindService recruitmentDetailsFindService;
	private final RecruitmentsFindService recruitmentsFindService;

	@GetMapping
	public ResponseEntity<RecruitmentPreviewResponses> readRecruitments(
		@RequestParam(required = false) Long after, @RequestParam Integer count
	) {
		List<RecruitmentPreviewResponse> recruitments = recruitmentsFindService.findRecruitments(after, count);

		return ResponseEntity.ok().body(new RecruitmentPreviewResponses(recruitments));
	}

	@GetMapping("/{recruitment-id}")
	public ResponseEntity<RecruitmentDetailsResponse> readRecruitmentDetails(
		@PathVariable("recruitment-id") final Long recruitmentId
	) {
		try {
			RecruitmentDetailsResponse recruitmentDetails = recruitmentDetailsFindService.findRecruitmentDetails(
				recruitmentId);

			return ResponseEntity.ok(recruitmentDetails);
		} catch (IllegalArgumentException exception) {

			return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.build();
		}
	}
}
