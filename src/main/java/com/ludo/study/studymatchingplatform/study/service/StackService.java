package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.repository.stack.StackRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StackService {

	private final StackRepositoryImpl stackRepository;

	public List<Stack> findAllByIdsOrThrow(final Set<Long> stackIds) {
		final List<Stack> stacks = stackRepository.findAllByIds(stackIds);
		if (stacks.size() != stackIds.size()) {
			throw new IllegalArgumentException("존재하지 않는 Stack을 입력 하였습니다.");
		}
		return stacks;
	}
}
