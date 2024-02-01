package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.id.RecruitmentStackId;

public interface RecruitmentStackJpaRepository extends JpaRepository<RecruitmentStack, RecruitmentStackId> {

}
