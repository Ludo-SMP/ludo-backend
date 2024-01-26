package com.ludo.study.studymatchingplatform.study.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.id.RecruitmentStackId;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentStackRepository;

public interface RecruitmentStackJpaRepository
	extends RecruitmentStackRepository, JpaRepository<RecruitmentStack, RecruitmentStackId> {

	// RecruitmentStack save(Recruitment recruitment, Stack stack);
	//
	// Set<RecruitmentStack> saveAll(Set<RecruitmentStack> recruitmentStacks);
	//
	// Set<RecruitmentStack> createMany(Recruitment recruitment, List<Stack> stacks);
	//
	// Set<RecruitmentStack> createMany(Recruitment recruitment, Set<Long> stackIds);
	//
	// void deleteManyByIdIn(Collection<RecruitmentStackId> id);
	//
	// void deleteMany(Set<RecruitmentStack> recruitmentStacks);

}
