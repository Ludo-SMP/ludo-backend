package com.ludo.study.studymatchingplatform.study.service.recruitment.stack;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.RecruitmentStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentStackService {

	private final RecruitmentStackRepositoryImpl recruitmentStackRepository;
	private final StackService stackService;
	private final StackRepositoryImpl stackRepository;

	public void addMany(final Recruitment recruitment, final Set<Long> stackIds) {
		final List<Stack> stacks = stackService.findAllByIdsOrThrow(stackIds);
		addRecruitmentStacksIn(recruitment, stacks);
	}

	public void update(final Recruitment recruitment, final Set<Long> stackIds) {
		final Set<Stack> nextStacks = stackRepository.findByIdIn(stackIds);
		final List<Stack> addedStacks = recruitment.getAddedStacks(nextStacks);

		addRecruitmentStacksIn(recruitment, addedStacks);
		recruitment.removeRecruitmentStacksNotIn(nextStacks);
	}

	private void addRecruitmentStacksIn(final Recruitment recruitment, final List<Stack> stacks) {
		recruitment.validateDuplicateStacks(stacks);

		final List<RecruitmentStack> recruitmentStacks = stacks.stream()
				.map(stack -> RecruitmentStack.from(recruitment, stack))
				.toList();

		recruitment.addRecruitmentStacks(recruitmentStackRepository.saveAll(recruitmentStacks));
	}

}
