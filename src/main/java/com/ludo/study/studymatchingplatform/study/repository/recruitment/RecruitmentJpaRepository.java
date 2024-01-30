package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

public interface RecruitmentJpaRepository extends JpaRepository<Recruitment, Long> {

	void deleteById(Long id);

	List<Recruitment> findAll();

	Optional<Recruitment> findById(long id);

	Recruitment save(Recruitment recruitment);

	@Query("select r from Recruitment r where r.callUrl = NULL")
	List<Recruitment> findAllByApplicantCount(int applicantCount);

}
