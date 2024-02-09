package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentStackRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentStackService {

	private final RecruitmentStackRepositoryImpl recruitmentStackRepository;
	private final StackService stackService;

	public List<RecruitmentStack> createMany(
		final Recruitment recruitment,
		final List<Long> stackIds
	) {
		final List<Stack> stacks = stackService.findAllByIdsOrThrow(stackIds);

		final List<RecruitmentStack> recruitmentStacks = stacks.stream()
			.filter(stack -> !recruitment.hasStack(stack))
			.map(stack -> RecruitmentStack.from(recruitment, stack))
			.toList();

		return recruitmentStackRepository.saveAll(recruitmentStacks);
	}

	public void update(
		final Recruitment recruitment,
		final List<Long> stackIds
	) {
	}
}
