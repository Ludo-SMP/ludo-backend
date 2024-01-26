package com.ludo.study.studymatchingplatform.study.repository.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.id.RecruitmentStackId;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentStackRepository;

public interface RecruitmentStackJpaRepository
	extends RecruitmentStackRepository, JpaRepository<RecruitmentStack, RecruitmentStackId> {

	RecruitmentStack save(Recruitment recruitment, Stack stack);

	Set<RecruitmentStack> saveAll(Set<RecruitmentStack> recruitmentStacks);

	Set<RecruitmentStack> createMany(Recruitment recruitment, List<Stack> stacks);

	Set<RecruitmentStack> createMany(Recruitment recruitment, Set<Long> stackIds);

	void deleteManyByIdIn(Set<Long> ids);

	void deleteMany(Set<RecruitmentStack> recruitmentStacks);

}
