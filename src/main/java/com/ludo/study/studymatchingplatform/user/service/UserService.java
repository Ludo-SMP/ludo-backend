package com.ludo.study.studymatchingplatform.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	private final UserRepositoryImpl userRepository;

	public void withdraw(final User user) {

		final User foundUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new NotFoundException("가입되지 않은 회원입니다."));

		if (foundUser.isDeleted()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "비활성화 된 회원입니다. 복구하시려면 해당 계정으로 다시 회원 가입을 시도해주세요.");
		}

		user.softDelete();
	}
}
