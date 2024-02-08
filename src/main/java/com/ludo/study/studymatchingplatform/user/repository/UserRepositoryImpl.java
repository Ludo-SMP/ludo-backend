package com.ludo.study.studymatchingplatform.user.repository;

import static com.ludo.study.studymatchingplatform.user.domain.QUser.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.jpa.UserJpaRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {

	private final JPAQueryFactory queryFactory;
	private final UserJpaRepositoryImpl userJpaRepository;

	public User save(final User user) {
		return userJpaRepository.save(user);
	}

	public Optional<User> findByEmail(final String email) {
		return Optional.ofNullable(
			queryFactory.select(user)
				.from(user)
				.where(user.email.eq(email))
				.fetchOne());
	}

}