package com.ludo.study.studymatchingplatform.study.repository;

import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;

public interface RecruitmentStackRepository {

	List<RecruitmentStack> saveAll(List<RecruitmentStack> recruitmentStacks);

}
