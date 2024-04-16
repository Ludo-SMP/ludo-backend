package com.ludo.study.studymatchingplatform.user.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
class ChangeNicknameServiceTest {

	@Autowired
	ChangeNicknameService changeNicknameService;

	@Autowired
	UserRepositoryImpl userRepository;

	@Autowired
	EntityManager em;

	// @Test
	// @DisplayName("[Success] 존재하지 않는 닉네임이면 닉네임 변경 성공")
	// @Transactional
	// void changeNickname() {
	// 	// given
	// 	User user = userRepository.save(
	// 			UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com")
	// 	);
	// 	String changeNickname = "루도";
	//
	// 	// when
	// 	ChangeUserNicknameResponse response = changeNicknameService.changeUserNickname(user,
	// 			changeNickname);
	//
	// 	// then
	// 	Optional<User> findOptionalUser = userRepository.findById(user.getId());
	// 	assertThat(findOptionalUser).isPresent();
	//
	// 	User findUser = findOptionalUser.get();
	// 	assertThat(findUser.getNickname()).isEqualTo(changeNickname);
	// 	assertThat(response.user().nickname()).isEqualTo(changeNickname);
	// }

	@Test
	@DisplayName("[Exception] 현재 닉네임과 동일한 닉네임이면 예외 발생")
	@Transactional
	void equalsCurrentNickname() {
		// given
		User user = userRepository.save(
				UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com"));

		// when, then
		assertThatThrownBy(() -> changeNicknameService.changeUserNickname(user, "archa"))
				.hasMessageContaining("현재 닉네임과 동일한 닉네임입니다.");
	}

	@Test
	@DisplayName("[Exception] 존재하는 닉네임이면 예외 발생")
	@Transactional
	void existNickname() {
		// given
		User user = userRepository.save(
				UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com")
		);
		User other = userRepository.save(
				UserFixture.createUser(Social.NAVER, "other", "archa@naver.com")
		);
		String changeNickname = "other";

		userRepository.save(user);
		userRepository.save(other);

		// when, then
		assertThatThrownBy(() -> changeNicknameService.changeUserNickname(user, changeNickname))
				.hasMessageContaining("이미 존재하는 닉네임입니다.");
	}

}
