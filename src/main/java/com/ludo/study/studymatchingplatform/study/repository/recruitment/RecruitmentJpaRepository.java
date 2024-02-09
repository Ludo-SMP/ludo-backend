package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

public interface RecruitmentJpaRepository extends JpaRepository<Recruitment, Long> {

	void deleteById(Long id);

	List<Recruitment> findAll();

	Optional<Recruitment> findById(long id);

	Recruitment save(Recruitment recruitment);

}
