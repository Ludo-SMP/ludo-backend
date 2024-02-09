package com.ludo.study.studymatchingplatform.study.repository.recruitment;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentRepositoryImpl {

	private final RecruitmentJpaRepository recruitmentJpaRepository;

	public Optional<Recruitment> findById(final Long recruitmentId) {
		return recruitmentJpaRepository.findById(recruitmentId);
	}

	public Recruitment save(Recruitment recruitment) {
		recruitmentJpaRepository.save(recruitment);
		return recruitment;
	}

	public void delete(final Recruitment recruitment) {
		recruitmentJpaRepository.delete(recruitment);
	}

}
