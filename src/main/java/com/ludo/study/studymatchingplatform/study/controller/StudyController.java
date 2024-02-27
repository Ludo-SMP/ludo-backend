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

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.IsAuthenticated;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.service.StudyCreateService;
import com.ludo.study.studymatchingplatform.study.service.StudyService;
import com.ludo.study.studymatchingplatform.study.service.StudyStatusService;
import com.ludo.study.studymatchingplatform.study.service.dto.request.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.WriteStudyResponse;
import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

	private final StudyCreateService studyCreateService;
	private final StudyStatusService studyStatusService;
	private final StudyService studyService;

	@IsAuthenticated
	@PostMapping
	public ResponseEntity<WriteStudyResponse> create(@RequestBody final WriteStudyRequest request,
													 @AuthUser final User user) {
		final Study study = studyCreateService.create(request, user);
		return ResponseEntity.status(HttpStatus.CREATED).body(WriteStudyResponse.from(study));
	}

	@IsAuthenticated
	@Transactional
	@PatchMapping("/{studyId}")
	public ResponseEntity<WriteStudyResponse> changeStatus(@PathVariable final Long studyId,
														   @RequestParam("status") final StudyStatus status,
														   @AuthUser final User user) {
		final Study study = studyStatusService.changeStatus(studyId, status, user);
		return ResponseEntity.status(HttpStatus.OK).body(WriteStudyResponse.from(study));
	}

	@DeleteMapping("/{studyId}/participants")
	public ResponseEntity<Void> leave(@AuthUser final User user, @PathVariable("studyId") final Long studyId) {
		studyService.leave(user, studyId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}