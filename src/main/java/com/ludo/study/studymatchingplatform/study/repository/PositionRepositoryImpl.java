package com.ludo.study.studymatchingplatform.study.repository;

import static com.ludo.study.studymatchingplatform.study.domain.QPosition.*;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PositionRepositoryImpl {

	private final JPAQueryFactory q;

	private final PositionJpaRepository positionJpaRepository;

	public Set<Position> findByIdIn(final List<Long> positionIds) {
		return positionJpaRepository.findByIdIn(positionIds);
	}

	public List<Position> findAllByIds(final List<Long> ids) {
		return q.selectFrom(position)
			.where(position.id.in(ids))
			.fetch();
	}

}
