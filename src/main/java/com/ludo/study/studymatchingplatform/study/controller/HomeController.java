package com.ludo.study.studymatchingplatform.study.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.study.service.PopularRecruitmentsFindService;
import com.ludo.study.studymatchingplatform.study.service.dto.response.PopularRecruitmentsResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

	private final PopularRecruitmentsFindService popularRecruitmentsFindService;

	@GetMapping
	public ResponseEntity<PopularRecruitmentsResponse> readPopularRecruitments() {
		PopularRecruitmentsResponse popularRecruitments = popularRecruitmentsFindService.findPopularRecruitments();

		return ResponseEntity.ok(popularRecruitments);
	}

}
