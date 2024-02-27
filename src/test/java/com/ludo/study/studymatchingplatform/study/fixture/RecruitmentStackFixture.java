package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;

public class RecruitmentStackFixture {

	public static RecruitmentStack createRecruitmentStack(Stack stack) {
		return RecruitmentStack.builder()
				.stack(stack)
				.build();
	}

	public static RecruitmentStack createRecruitmentStack(Recruitment recruitment, Stack stack) {
		return RecruitmentStack.builder()
				.recruitment(recruitment)
				.stack(stack)
				.build();
	}
}
