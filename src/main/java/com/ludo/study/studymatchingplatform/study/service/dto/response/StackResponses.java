package com.ludo.study.studymatchingplatform.study.service.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;

import lombok.Getter;

@Getter
public class StackResponses {

	@JsonProperty(value = "stacks")
	private final List<StackResponse> stackResponses;

	public StackResponses(List<Stack> stacks) {
		stackResponses = stacks
				.stream()
				.map(StackResponse::from)
				.toList();
	}

}
