package com.ludo.study.studymatchingplatform.study.repository.recruitment.stack;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.id.RecruitmentStackId;

public interface RecruitmentStackJpaRepository extends JpaRepository<RecruitmentStack, RecruitmentStackId> {

}
