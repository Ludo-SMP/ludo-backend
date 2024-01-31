package com.ludo.study.studymatchingplatform.user.repository;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {

	private final UserJpaRepository userJpaRepository;

	public User save(final User user) {
		return userJpaRepository.save(user);
	}

}
