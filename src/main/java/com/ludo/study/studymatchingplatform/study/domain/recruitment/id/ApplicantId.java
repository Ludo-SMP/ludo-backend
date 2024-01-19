package com.ludo.study.studymatchingplatform.study.domain.recruitment.id;

import java.io.Serializable;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ApplicantId implements Serializable {

	@Column(name = "recruitment_id")
	private Long recruitmentId;

	@Column(name = "user_id")
	private Long userId;

	public ApplicantId(Recruitment recruitment, User user) {
		this.recruitmentId = recruitment.getId();
		this.userId = user.getId();
	}

}
