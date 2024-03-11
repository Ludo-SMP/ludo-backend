package com.ludo.study.studymatchingplatform.study.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.study.controller.dto.response.BaseApiResponse;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.repository.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.StackResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StackController {

	private final StackRepositoryImpl stackRepository;
	private final StackCategoryRepositoryImpl stackCategoryRepository;

	@GetMapping("/stacks")
	@ResponseStatus(HttpStatus.OK)
	@DataFieldName("stacks")
	@Operation(description = "기술 스택 조회")
	@ApiResponse(description = "기술 스택 조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
	public StackResponses getStacks() {
		List<StackCategory> stackCategories = stackCategoryRepository.findAll();
		List<Stack> stacks = stackRepository.findAll();
		return new StackResponses(stacks, stackCategories);
	}

}
