package com.ludo.study.studymatchingplatform.study.domain;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Slf4j
public class Study extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "study_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "char(10)")
	private StudyStatus status;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "char(20)")
	private Platform platform;

	@Column(nullable = false, length = 50)
	private String title;

	@OneToOne(mappedBy = "study", fetch = LAZY)
	private Recruitment recruitment;

	@OneToMany(mappedBy = "study", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Participant> participants = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "char(10)")
	private Way way;

	@Column(nullable = false)
	private Integer participantLimit;

	// null 제거 필요
	@Column(nullable = true)
	private Integer participantCount;

	@Column(nullable = false)
	private LocalDateTime startDateTime;

	@Column(nullable = false)
	private LocalDateTime endDateTime;

	public void addParticipant(final Participant participant) {
		this.participants.add(participant);
		this.participantCount = this.participants.size();
	}

	public void registerRecruitment(final Recruitment recruitment) {
		this.recruitment = recruitment;
		this.recruitment.connectToStudy(this);

	}

	public void changeStatus(final StudyStatus status) {
		this.status = status;
	}

	public void addRecruitment(Recruitment recruitment) {
		this.recruitment = recruitment;
	}

	public Integer getParticipantCount() {
		return participants.size();
	}

	public String getCategoryByName() {
		return category.getName();
	}

	public Long getCategoryId() {
		return category.getId();
	}

	public Long getOwnerId() {
		return owner.getId();
	}

	public String getOwnerNickname() {
		return owner.getNickname();
	}

	public String getOwnerEmail() {
		return owner.getEmail();
	}

	public String getCategoryName() {
		return category.getName();
	}

	public void ensureRecruitmentWritableBy(final User user) {
		if (recruitment != null && !recruitment.isDeleted()) {
			throw new IllegalArgumentException("이미 작성된 모집 공고가 존재합니다.");
		}

		if (!isOwner(user)) {
			throw new IllegalArgumentException("모집 공고를 작성할 권한이 없습니다.");
		}
	}

	public void ensureRecruitmentEditable(final User user) {
		if (owner != user) {
			throw new IllegalArgumentException("모집 공고를 수정할 권한이 없습니다.");
		}
		if (recruitment == null || recruitment.isDeleted()) {
			throw new IllegalArgumentException("존재하지 않는 모집 공고입니다.");
		}
	}

	public void ensureStudyEditable(final User user) {
		if (owner.getId() != user.getId()) {
			throw new IllegalArgumentException("스터디를 수정할 권한이 없습니다.");
		}
	}

	public void ensureRecruitmentDeletable(final User user) {
		if (owner.getId() != user.getId()) {
			throw new IllegalArgumentException("모집 공고를 삭제할 권한이 없습니다.");
		}
		if (recruitment.isDeleted()) {
			throw new IllegalArgumentException("존재하지 않는 모집 공고입니다.");
		}
	}

	public boolean isOwner(final User user) {
		return Objects.equals(owner.getId(), user.getId());
	}

	public boolean isOwner(final Participant participant) {
		return participant.matchesUser(owner);
	}

	public void ensureRecruiting() {
		if (status != StudyStatus.RECRUITING) {
			throw new IllegalStateException("현재 모집 중인 스터디가 아닙니다.");
		}
	}

	public void acceptApplicant(final User owner, final User applicantUser) {
		ensureAcceptApplicant(owner, applicantUser);
		accept(applicantUser);
		if (isMaxParticipantCount()) {
			changeStatus(StudyStatus.RECRUITED);
		}
	}

	public void rejectApplicant(final User owner, final User applicantUser) {
		ensureRejectApplicant(owner, applicantUser);
		recruitment.rejectApplicant(applicantUser);
	}

	private void ensureRejectApplicant(final User owner, final User applicantUser) {
		ensureCorrectOwner(owner);
		ensureApplicantUserIsNotOwner(owner, applicantUser);
		recruitment.ensureCorrectApplicantUser(applicantUser);
	}

	private void ensureApplicantUserIsNotOwner(final User owner, final User applicantUser) {
		if (owner.equals(applicantUser)) {
			throw new IllegalArgumentException("스터디 장과 지원자가 같습니다.");
		}
	}

	private void ensureAcceptApplicant(final User owner, final User applicantUser) {
		ensureCorrectOwner(owner);
		ensureApplicantUserIsNotOwner(owner, applicantUser);
		ensureRecruiting();
		ensureRemainParticipantLimit();
		recruitment.ensureCorrectApplicantUser(applicantUser);
	}

	private void ensureCorrectOwner(final User owner) {
		log.info("스터디장 = {}", this.owner.getId());
		log.info("파라미터 = {}", owner.getId());
		if (!isOwner(owner)) {
			throw new IllegalStateException(
					String.format("스터디 장이 아닙니다. 스터디 장 id = %s, 잘못된 id = %s", this.owner, owner));
		}
	}

	private void ensureRemainParticipantLimit() {
		if (Objects.equals(getParticipantCount(), participantLimit)) {
			throw new IllegalStateException("남아있는 자리가 없습니다.");
		}
	}

	private void accept(final User applicantUser) {
		recruitment.acceptApplicant(applicantUser);
		final Applicant applicant = recruitment.getApplicant(applicantUser);
		addParticipant(Participant.from(this, applicantUser, applicant.getPosition(), Role.MEMBER));
	}

	private boolean isMaxParticipantCount() {
		return Objects.equals(participantCount, participantLimit);
	}

	public boolean isParticipating(final User user) {
		return participants.stream()
				.anyMatch(p -> p.matchesUser(user));
	}

	public int getDday() {
		return Period.between(startDateTime.toLocalDate(), endDateTime.toLocalDate()).getDays();
	}

	public Participant getParticipant(final User user) {
		return participants.stream()
				.filter(p -> p.matchesUser(user))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("현재 참여 중인 스터디원이 아닙니다."));
	}

	public void removeParticipant(final Participant participant) {
		participants.removeIf(p -> Objects.equals(p, participant));
	}

	public void edit(final StudyStatus status) {
		if (status != null) {
			this.status = status;
		}
	}

}
