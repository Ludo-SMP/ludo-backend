package com.ludo.study.studymatchingplatform.study.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentRepository;

public interface RecruitmentJpaRepository extends RecruitmentRepository, JpaRepository<Recruitment, Long> {

	void deleteById(Long id);

	List<Recruitment> findAll();

	Optional<Recruitment> findById(long id);

	Recruitment save(Recruitment recruitment);

	// Recruitment save(Recruitment recruitment, Stack stack);

	// List<Recruitment> saveAll(Set<RecruitmentStack> recruitmentStacks);

	// List<Recruitment> findAllByCallUrlIsNull(int applicantCount);

	@Query("select r from Recruitment r where r.callUrl = NULL")
	List<Recruitment> findAllByApplicantCount(int applicantCount);

}
