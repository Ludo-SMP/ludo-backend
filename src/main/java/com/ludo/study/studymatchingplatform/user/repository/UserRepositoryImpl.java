package com.ludo.study.studymatchingplatform.user.repository;

import static com.ludo.study.studymatchingplatform.user.domain.QUser.*;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {

	private final JPAQueryFactory q;

	private final UserJpaRepository userJpaRepository;

	public User save(final User user) {
		return userJpaRepository.save(user);
	}

	/**
	 * may need to add platformId into table
	 */
	public boolean existsByEmailForGoogle(final String email) {
		final Long id = q.select(user.id)
			.from(user)
			.where(
				user.social.eq(Social.GOOGLE),
				user.email.eq(email)
			)
			.fetchFirst();

		return id != null;
	}

}
