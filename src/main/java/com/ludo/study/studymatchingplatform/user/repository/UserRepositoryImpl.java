package com.ludo.study.studymatchingplatform.user.repository;

import static com.ludo.study.studymatchingplatform.user.domain.QUser.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {

	private final UserJpaRepository userJpaRepository;
	private final JPAQueryFactory qf;

	public void save(final User user) {
		userJpaRepository.save(user);
	}

	public Optional<User> findById(final Long id) {
		return userJpaRepository.findById(id);
	}

	public Optional<User> findByEmail(final String email) {
		return Optional.ofNullable(qf.selectFrom(user)
				.where(user.email.eq(email))
				.fetchOne());
	}

}
