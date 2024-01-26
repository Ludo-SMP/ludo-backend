package com.ludo.study.studymatchingplatform.study.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentRepository;

public interface RecruitmentJpaRepository extends RecruitmentRepository, JpaRepository<Recruitment, Long> {

	void deleteById(Long id);

	List<Recruitment> findAll();

}
