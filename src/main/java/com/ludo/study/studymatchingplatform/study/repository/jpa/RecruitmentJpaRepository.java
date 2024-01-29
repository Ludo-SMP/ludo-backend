package com.ludo.study.studymatchingplatform.study.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

public interface RecruitmentJpaRepository extends JpaRepository<Recruitment, Long> {
	@Query("select r from Recruitment r where r.callUrl = NULL")
	List<Recruitment> findAllByApplicantCount(int applicantCount);

}
