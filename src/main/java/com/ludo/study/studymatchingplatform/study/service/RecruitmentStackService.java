package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.stack.StackRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentStackService {

	private final RecruitmentStackRepositoryImpl recruitmentStackRepository;
	private final StackService stackService;
	private final StackRepositoryImpl stackRepository;

	public void addMany(final Recruitment recruitment, final Set<Long> stackIds) {
		final List<Stack> stacks = stackService.findAllByIdsOrThrow(stackIds);

		final List<RecruitmentStack> recruitmentStacks = stacks.stream()
				.map(stack -> RecruitmentStack.from(recruitment, stack))
				.toList();

		recruitment.addRecruitmentStacks(recruitmentStackRepository.saveAll(recruitmentStacks));
	}

	public void update(final Recruitment recruitment, final Set<Long> stackIds) {
		final Set<Stack> nextStacks = stackRepository.findByIdIn(stackIds);
		recruitment.updateStacks(nextStacks);
		// List<Stack> notExistStacks = recruitment.getRemovedStacks(nextStacks);
		// List<Stack> notExistStacks = recruitment.getNotExistStacks(nextStacks);
	}

}
