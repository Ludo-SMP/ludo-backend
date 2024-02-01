package com.ludo.study.studymatchingplatform.study.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.study.service.RecruitmentDeleteService;
import com.ludo.study.studymatchingplatform.study.service.StudyCreateService;
import com.ludo.study.studymatchingplatform.study.service.dto.request.StudyCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

	private final StudyCreateService studyCreateService;
	private final RecruitmentDeleteService recruitmentCreateService;

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody final StudyCreateRequest request) {
		studyCreateService.create(request);
		return ResponseEntity.created(URI.create("/api")).build();
	}

	@DeleteMapping("/{studyId}/recruitments")
	public ResponseEntity<Void> deleteRecruitment(@PathVariable Long studyId) {
		recruitmentCreateService.deleteRecruitment(studyId);
		return ResponseEntity.noContent().build();
	}

}
