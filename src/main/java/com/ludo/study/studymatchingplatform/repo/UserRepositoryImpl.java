package com.ludo.study.studymatchingplatform.repo;

import static com.ludo.study.studymatchingplatform.user.domain.QUser.*;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private UserJpaRepository userJpaRepository;

	private JPAQueryFactory q;

	@Override
	public User save(User user1) {
		return q.
			selectFrom(user)
			.fetchFirst();
	}
}
