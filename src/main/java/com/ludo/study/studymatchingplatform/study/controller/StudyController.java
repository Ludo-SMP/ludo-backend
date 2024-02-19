package com.ludo.study.studymatchingplatform.study.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.service.StudyCreateService;
import com.ludo.study.studymatchingplatform.study.service.StudyStatusService;
import com.ludo.study.studymatchingplatform.study.service.dto.request.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.WriteStudyResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

	private final StudyCreateService studyCreateService;
	private final StudyStatusService studyStatusService;

	@PostMapping
	public ResponseEntity<WriteStudyResponse> create(@RequestBody final WriteStudyRequest request,
													 @RequestParam final String email) {
		final Study study = studyCreateService.create(request, email);
		return ResponseEntity.status(HttpStatus.CREATED).body(WriteStudyResponse.from(study));
	}

	@PatchMapping("/{studyId}")
	@Transactional
	public ResponseEntity<WriteStudyResponse> changeStatus(
			@PathVariable final Long studyId,
			@RequestParam("status") final StudyStatus status
	) {
		final WriteStudyResponse response = studyStatusService.changeStatus(studyId, status);
		return ResponseEntity.ok(response);
	}

}
