package com.ludo.study.studymatchingplatform.user.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {

	private final UserJpaRepository userJpaRepository;

	public void save(final User user) {
		userJpaRepository.save(user);
	}

	public Optional<User> findById(final Long userId) {
		return userJpaRepository.findById(userId);
	}

}
