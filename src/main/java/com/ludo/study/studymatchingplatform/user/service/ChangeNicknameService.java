package com.ludo.study.studymatchingplatform.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.service.dto.response.ChangeUserNicknameResponse;
import com.ludo.study.studymatchingplatform.user.service.exception.DuplicateNicknameException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChangeNicknameService {

	private final UserRepositoryImpl userRepository;

	@Transactional
	public ChangeUserNicknameResponse changeUserNickname(final User user, final String changeNickname) {
		user.validateNotEqualsCurrentNickname(changeNickname);
		validateDuplicateNickname(changeNickname);
		user.changeNickname(changeNickname);

		return new ChangeUserNicknameResponse(user);
	}

	private void validateDuplicateNickname(final String nickname) {
		if (userRepository.existsByNickname(nickname)) {
			throw new DuplicateNicknameException();
		}
	}
}
