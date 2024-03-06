package com.ludo.study.studymatchingplatform.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.domain.exception.CurrentNicknameEqualsException;
import com.ludo.study.studymatchingplatform.user.domain.exception.DuplicateNicknameException;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.service.dto.response.ChangeUserNicknameResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChangeNicknameService {

	private final UserRepositoryImpl userRepository;

	@Transactional
	public ChangeUserNicknameResponse changeUserNickname(final User user, final String changeNickname) {
		validateChangeNickname(user, changeNickname);
		user.changeNickname(changeNickname);

		return new ChangeUserNicknameResponse(user);
	}

	private void validateChangeNickname(final User user, final String nickname) {
		validateNotEqualsCurrentNickname(user, nickname);
		validateDuplicateNickname(nickname);
	}

	private void validateNotEqualsCurrentNickname(final User user, final String nickname) {
		if (user.equalsNickname(nickname)) {
			throw new CurrentNicknameEqualsException();
		}
	}

	private void validateDuplicateNickname(final String nickname) {
		if (userRepository.existsByNickname(nickname)) {
			throw new DuplicateNicknameException();
		}
	}
}
