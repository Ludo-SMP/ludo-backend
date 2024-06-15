package com.ludo.study.studymatchingplatform.user.repository.user;

import static com.ludo.study.studymatchingplatform.user.domain.user.QDetails.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.user.domain.user.Details;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DetailsRepositoryImpl {

	private final JPAQueryFactory q;
	private final DetailsJpaRepository detailsJpaRepository;

	public Details save(final Details details) {
		return detailsJpaRepository.save(details);
	}

	public Optional<Details> findByUserId(final Long userId) {
		return Optional.ofNullable(q.selectFrom(details)
				.where(details.user.id.eq(userId))
				.fetchOne());
	}

}
