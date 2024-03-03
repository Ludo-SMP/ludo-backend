package com.ludo.study.studymatchingplatform.study.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.repository.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.StackResponses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StackController {

	private final StackRepositoryImpl stackRepository;
	private final StackCategoryRepositoryImpl stackCategoryRepository;

	@GetMapping("/stacks")
	public ResponseEntity<BaseApiResponse<StackResponses>> getStacks() {
		List<StackCategory> stackCategories = stackCategoryRepository.findAll();
		List<Stack> stacks = stackRepository.findAll();
		StackResponses stackResponses = new StackResponses(stacks, stackCategories);
		return ResponseEntity.ok(BaseApiResponse.success("기술 스택 조회 성공", stackResponses));
	}

}
