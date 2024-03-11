package com.ludo.study.studymatchingplatform.study.repository.recruitment.position;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.position.QRecruitmentPosition.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.id.RecruitmentPositionId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.RecruitmentPosition;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentPositionRepositoryImpl {

	private final JPAQueryFactory q;

	private final RecruitmentPositionJpaRepository recruitmentPositionJpaRepository;

	public List<RecruitmentPosition> saveAll(final List<RecruitmentPosition> recruitmentPositions) {
		return recruitmentPositionJpaRepository.saveAll(recruitmentPositions);
	}

	public void deleteById(final RecruitmentPositionId id) {
		q.update(recruitmentPosition)
				.set(recruitmentPosition.deletedDateTime, LocalDateTime.now())
				.where(recruitmentPosition.id.eq(id))
				.execute();
	}

	public void save(final RecruitmentPosition recruitmentPosition) {
		recruitmentPositionJpaRepository.save(recruitmentPosition);
	}
}
