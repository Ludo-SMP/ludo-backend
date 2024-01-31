package com.ludo.study.studymatchingplatform.study.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.study.service.RecruitmentDeleteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

	private final RecruitmentDeleteService recruitmentCreateService;

	@DeleteMapping("/{studyId}/recruitments")
	public ResponseEntity<Void> deleteRecruitment(@PathVariable Long studyId) {
		recruitmentCreateService.deleteRecruitment(studyId);
		return ResponseEntity.noContent().build();
	}

}
