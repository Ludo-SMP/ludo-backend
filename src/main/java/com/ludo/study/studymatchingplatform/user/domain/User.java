package com.ludo.study.studymatchingplatform.user.domain;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.user.domain.exception.CurrentNicknameEqualsException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "`user`")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Slf4j
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false, columnDefinition = "char(10)")
	private Social social;

	@Column(nullable = false)
	@Size(min = 1, max = 20)
	private String nickname;

	@Column(nullable = false, length = 320)
	@Email
	private String email;

	public User(final Social social, final String nickname, final String email) {
		this.social = social;
		this.nickname = nickname;
		this.email = email;
	}

	public void changeNickname(final String nickname) {
		validateNotEqualsCurrentNickname(nickname);
		this.nickname = nickname;
	}

	public void validateNotEqualsCurrentNickname(final String nickname) {
		if (this.nickname.equals(nickname)) {
			throw new CurrentNicknameEqualsException();
		}
	}

}

