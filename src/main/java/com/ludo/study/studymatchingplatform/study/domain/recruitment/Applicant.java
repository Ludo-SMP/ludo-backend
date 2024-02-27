package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
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

	@ManyToOne(fetch = LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = LAZY)
	@MapsId("recruitmentId")
	@JoinColumn(name = "recruitment_id")
	private Recruitment recruitment;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, columnDefinition = "char(10)")
	private ApplicantStatus applicantStatus;

	public static Applicant of(final Recruitment recruitment, final User user) {
		return Applicant.builder()
				.recruitment(recruitment)
				.user(user)
				.applicantStatus(ApplicantStatus.UNCHECKED)
				.build();
	}

	public void ensureApplicable() {
		if (recruitment.isOwner(user)) {
			throw new IllegalArgumentException("스터디장은 자신의 스터디에 지원할 수 없습니다.");
		}
		if (applicantStatus == ApplicantStatus.REJECTED) {
			throw new IllegalArgumentException("이미 거절된 모집 공고입니다.");
		}
		if (applicantStatus == ApplicantStatus.ACCEPTED) {
			throw new IllegalArgumentException("이미 수락된 모집 공고입니다.");
		}
		if (applicantStatus == ApplicantStatus.UNCHECKED) {
			throw new IllegalArgumentException("이미 지원한 모집 공고입니다.");
		}
	}

	public void apply() {
		applicantStatus = ApplicantStatus.UNCHECKED;
	}

	public void connectToRecruitment(final Recruitment recruitment) {
		System.out.println("현재 recruitment = " + recruitment);
		this.recruitment = recruitment;
	}

	public void changeStatus(final ApplicantStatus applicantStatus) {
		this.applicantStatus = applicantStatus;
	}

	public void reapply() {
		applicantStatus = ApplicantStatus.UNCHECKED;
	}

	public void ensureCancellable(final StudyStatus studyStatus) {
		if (studyStatus != StudyStatus.RECRUITING) {
			throw new IllegalStateException("이미 모집이 종료된 공고입니다.");
		}
		if (applicantStatus == ApplicantStatus.REJECTED) {
			throw new IllegalStateException("이미 거절된 모집 공고입니다.");
		}
		if (applicantStatus == ApplicantStatus.CANCELLED) {
			throw new IllegalStateException("이미 지원 취소된 모집 공고입니다.");
		}
	}

	public void accepted() {
		applicantStatus = ApplicantStatus.ACCEPTED;
	}

}
