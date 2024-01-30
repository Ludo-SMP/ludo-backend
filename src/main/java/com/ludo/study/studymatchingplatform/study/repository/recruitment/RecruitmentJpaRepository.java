package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

public interface RecruitmentJpaRepository extends JpaRepository<Recruitment, Long> {

	void deleteById(Long id);

	List<Recruitment> findAll();

	Optional<Recruitment> findById(long id);

	Recruitment save(Recruitment recruitment);

	@Query("select r from Recruitment r where r.callUrl = NULL")
	List<Recruitment> findAllByApplicantCount(int applicantCount);

	@Query("select r from Recruitment r join Study s on r.study.id = s.id join Category c on s.category.id = c.id where c.name = :category")
	List<Recruitment> findPopularRecruitments(@Param("category") String categoryName, Pageable pageable);

}
