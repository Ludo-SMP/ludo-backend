package com.ludo.study.studymatchingplatform.study.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentDeleteService;
import com.ludo.study.studymatchingplatform.study.service.StudyCreateService;
import com.ludo.study.studymatchingplatform.study.service.StudyStatusService;
import com.ludo.study.studymatchingplatform.study.service.dto.request.StudyCreateRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.StudyResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

	private final StudyCreateService studyCreateService;
	private final RecruitmentDeleteService recruitmentCreateService;
	private final StudyStatusService studyStatusService;

	@PostMapping
	public ResponseEntity<Void> create(final StudyCreateRequest request) {
		studyCreateService.create(request);
		return ResponseEntity.created(URI.create("/api")).build();
	}

	@DeleteMapping("/{studyId}/recruitments")
	public ResponseEntity<Void> deleteRecruitment(@PathVariable Long studyId) {
		recruitmentCreateService.deleteRecruitment(studyId);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{studyId}")
	public ResponseEntity<StudyResponse> changeStatus(
			@PathVariable final Long studyId,
			@RequestParam("status") final StudyStatus status
	) {
		final StudyResponse response = studyStatusService.changeStatus(studyId, status);
		return ResponseEntity.ok(response);
	}

}
