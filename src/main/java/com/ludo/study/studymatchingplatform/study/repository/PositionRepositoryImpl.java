package com.ludo.study.studymatchingplatform.study.repository;

import static com.ludo.study.studymatchingplatform.study.domain.QPosition.*;

import java.util.List;
import java.util.Optional;
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

	public Set<Position> findByIdIn(final Set<Long> positionIds) {
		return positionJpaRepository.findByIdIn(positionIds);
	}

	public List<Position> findAllByIds(final Set<Long> ids) {
		return q.selectFrom(position)
				.where(position.id.in(ids))
				.fetch();
	}

	public Position findById(final Long id) {
		return q.selectFrom(position)
				.where(position.id.eq(id))
				.fetchOne();
	}

	public Optional<Position> findByName(final String name) {
		return Optional.ofNullable(
				q.select(position)
						.from(position)
						.where(position.name.eq(name))
						.fetchOne());
	}

	public Position save(final Position position) {
		return positionJpaRepository.save(position);
	}
}
