package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.id.ApplicantId;
import com.ludo.study.studymatchingplatform.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Applicant extends BaseEntity {

	@EmbeddedId
	@Builder.Default
	private ApplicantId id = new ApplicantId();

	@OneToOne(fetch = LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = LAZY)
	@MapsId("recruitmentId")
	@JoinColumn(name = "recruitment_id")
	private Recruitment recruitment;

	@Enumerated(EnumType.STRING)
	@Column(
		name = "status",
		nullable = false,
		columnDefinition = "char(10)"
	)
	private ApplicantStatus applicantStatus;

	public static Applicant of(final Recruitment recruitment, final User user) {
		return Applicant.builder()
			.recruitment(recruitment)
			.user(user)
			.build();
	}

	public void applyOrThrow() {
		if (recruitment.isOwner(user)) {
			throw new IllegalArgumentException("스터디장은 자신의 스터디에 지원할 수 없습니다.");
		}
		applicantStatus.ensureReapplicable();
		applicantStatus = ApplicantStatus.UNCHECKED;
	}

}
