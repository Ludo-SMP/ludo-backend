package com.ludo.study.studymatchingplatform.study.repository.recruitment.stack;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.id.RecruitmentStackId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;

public interface RecruitmentStackJpaRepository extends JpaRepository<RecruitmentStack, RecruitmentStackId> {

}
