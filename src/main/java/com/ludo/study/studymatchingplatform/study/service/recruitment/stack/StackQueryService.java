package com.ludo.study.studymatchingplatform.study.service.recruitment.stack;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StackQueryService {

	private final StackRepositoryImpl stackRepository;

	public Stack readStack(final Long stackId) {
		return stackRepository.findById(stackId)
				.orElseThrow(() -> new IllegalArgumentException(String.format("id=%d는 존재하지 않는 기술스택입니다.", stackId)));
	}
	
}
