package com.ludo.study.studymatchingplatform.study.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.id.RecruitmentStackId;

public interface RecruitmentStackJpaRepository extends JpaRepository<RecruitmentStack, RecruitmentStackId> {

	List<RecruitmentStack> saveAll(List<RecruitmentStack> recruitmentStacks);
	// RecruitmentStack save(Recruitment recruitment, Stack stack);
	//
	//
	// Set<RecruitmentStack> createMany(Recruitment recruitment, List<Stack> stacks);
	//
	// Set<RecruitmentStack> createMany(Recruitment recruitment, Set<Long> stackIds);
	//
	// void deleteManyByIdIn(Collection<RecruitmentStackId> id);
	//
	// void deleteMany(Set<RecruitmentStack> recruitmentStacks);

}
