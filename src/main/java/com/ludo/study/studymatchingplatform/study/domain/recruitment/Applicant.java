package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.Status;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.id.ApplicantId;
import com.ludo.study.studymatchingplatform.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter

public class Applicant extends BaseEntity {

	@EmbeddedId
	private ApplicantId id;

	@OneToOne(fetch = LAZY)
	@JoinColumn(
		name = "user_id",
		insertable = false,
		updatable = false
	)
	private User user;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "recruitment_id",
		insertable = false,
		updatable = false
	)
	private Recruitment recruitment;

	@Enumerated(EnumType.STRING)
	@Column(
		nullable = false,
		columnDefinition = "char(10)"
	)
	private Status status;

}
