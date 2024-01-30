package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl {

	private final RecruitmentJpaRepository recruitmentJpaRepository;

	public Recruitment save(Recruitment recruitment) {
		recruitmentJpaRepository.save(recruitment);
		return recruitment;
	}

	public Optional<Recruitment> findById(final Long id) {
		return recruitmentJpaRepository.findById(id);
	}

	public List<Recruitment> findPopularRecruitments(final String categoryName, final PageRequest pageRequest) {
		return recruitmentJpaRepository.findPopularRecruitments(categoryName, pageRequest);
	}

}
