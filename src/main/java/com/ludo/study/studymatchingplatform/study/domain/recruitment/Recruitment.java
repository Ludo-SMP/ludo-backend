package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.id.ApplicantId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
// @SQLDelete(sql = "UPDATE recruitment SET deleted_date_time = NOW() WHERE recruitment_id = ?")
// @SQLRestriction("deleted_date_time is null")
@ToString(of = {"id", "title", "modifiedDateTime"}, callSuper = true)
@Slf4j
public class Recruitment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recruitment_id")
	private Long id;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "study_id", nullable = false)
	private Study study;

	@OneToMany(fetch = LAZY, mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Applicant> applicants = new ArrayList<>();

	@OneToMany(fetch = LAZY, mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<RecruitmentStack> recruitmentStacks = new ArrayList<>();

	@OneToMany(fetch = LAZY, mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<RecruitmentPosition> recruitmentPositions = new ArrayList<>();

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime modifiedDateTime;

	// contact 추가 (연결 방법)
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "char(20)")
	private Contact contact;

	@Column(nullable = false, length = 2048)
	@Size(max = 2048)
	private String callUrl;

	@Column(nullable = false, length = 50)
	@Size(max = 50)
	private String title;

	@Column(nullable = false, length = 2000)
	@Size(max = 2000)
	private String content;

	@Column(nullable = false)
	@PositiveOrZero
	@Builder.Default
	private Integer hits = 0;

	@Column(nullable = false)
	private LocalDateTime recruitmentEndDateTime;

	@Column(nullable = false)
	@PositiveOrZero
	private Integer applicantCount;

	public static Recruitment of(
			final String title,
			final String content,
			final Contact contact,
			final String callUrl,
			final Integer hits,
			final Integer applicantCount,
			final LocalDateTime recruitmentEndDateTime,
			final Study study
	) {
		return Recruitment.builder()
				.title(title)
				.content(content)
				.contact(contact)
				.callUrl(callUrl)
				.hits(hits)
				.applicantCount(applicantCount)
				.recruitmentEndDateTime(recruitmentEndDateTime)
				.study(study)
				.build();
	}

	@Deprecated
	public void connectToStudy(Study study) {
		this.study = study;
	}

	/***
	 *
	 * @param recruitmentStack
	 * 어플리케이션 레벨에서 데이터 정합성 유지
	 */
	public void addRecruitmentStack(RecruitmentStack recruitmentStack) {
		this.recruitmentStacks
				.add(recruitmentStack);
	}

	public void addPosition(RecruitmentPosition recruitmentPosition) {
		this.recruitmentPositions
				.add(recruitmentPosition);
	}

	public void upHit() {
		hits++;
	}

	public List<String> getStackNames() {
		return recruitmentStacks.stream()
				.map(recruitmentStack -> recruitmentStack.getStack().getName())
				.toList();
	}

	public List<String> getPositionNames() {
		return recruitmentPositions.stream()
				.map(recruitmentPosition -> recruitmentPosition.getPosition().getName())
				.toList();
	}

	public void addRecruitmentStacks(final List<RecruitmentStack> recruitmentStacks) {
		this.recruitmentStacks.addAll(recruitmentStacks);
	}

	public void addRecruitmentPositions(final List<RecruitmentPosition> recruitmentPositions) {
		this.recruitmentPositions.addAll(recruitmentPositions);
	}

	public void edit(
			final String title,
			final Contact contact,
			final String callUrl,
			final Integer applicantCount,
			final LocalDateTime recruitmentEndDateTime,
			final String content
	) {
		if (title != null) {
			this.title = title;
		}
		if (contact != null) {
			this.contact = contact;
		}
		if (callUrl != null) {
			this.callUrl = callUrl;
		}
		if (applicantCount != null) {
			this.applicantCount = applicantCount;
		}
		if (recruitmentEndDateTime != null) {
			this.recruitmentEndDateTime = recruitmentEndDateTime;
		}
		if (content != null) {
			this.content = content;
		}
		if (modifiedDateTime != null) {
			this.modifiedDateTime = LocalDateTime.now();
		}
	}

	public Optional<RecruitmentStack> getRecruitmentStack(final Stack stack) {
		return recruitmentStacks.stream()
				.filter(r -> r.getStack().equals(stack))
				.findFirst();
	}

	public Optional<RecruitmentPosition> getRecruitmentPosition(final Position position) {
		return recruitmentPositions.stream()
				.filter(r -> r.getPosition().equals(position))
				.findFirst();
	}

	public void removeRecruitmentStack(final RecruitmentStack recruitmentStack) {
		recruitmentStacks.remove(recruitmentStack);
	}

	public void removeRecruitmentPosition(final RecruitmentPosition recruitmentPosition) {
		recruitmentPositions.remove(recruitmentPosition);
	}

	public boolean hasStacks(final Stack stack) {
		return recruitmentStacks.stream()
				.anyMatch(r -> r.getStack().equals(stack));
	}

	public boolean hasPosition(final Position position) {
		return recruitmentPositions.stream()
				.anyMatch(r -> r.getPosition().equals(position));
	}

	public void ensureEditable(final User user) {
		if (!isOwner(user)) {
			throw new IllegalArgumentException("모집 공고를 작성할 권한이 없습니다.");
		}
	}

	public void ensureApplicable(final User user) {
		if (study.isOwner(user)) {
			throw new IllegalArgumentException("스터디장은 자신의 스터디에 지원할 수 없습니다.");
		}
		applicants.stream()
				.filter(a -> a.equals(user))
				.findFirst()
				.ifPresent(Applicant::ensureApplicable);
	}

	public Optional<Applicant> findApplicant(final User user) {
		return applicants.stream()
				.filter(a -> a.getUser().equals(user))
				.findFirst();
	}

	public boolean isOwner(final User user) {
		return study.isOwner(user);
	}

	/** helper method */
	public void addApplicant(final Applicant applicant) {
		applicant.connectToRecruitment(this);
		applicants.add(applicant);
	}

	public void ensureRecruiting() {
		study.ensureRecruiting();
	}

	public List<Stack> getAddedStacks(final Set<Stack> nextStacks) {
		final List<Stack> stacks = getStacks();

		return nextStacks.stream()
				.filter(nextStack -> !stacks.contains(nextStack))
				.toList();
	}

	public List<Position> getAddedPositions(final Set<Position> nextPositions) {
		final List<Position> prevPositions = getPositions();

		return nextPositions.stream()
				.filter(nextPosition -> !prevPositions.contains(nextPosition))
				.toList();
	}

	public List<Position> getPositions() {
		return recruitmentPositions.stream()
				.map(RecruitmentPosition::getPosition)
				.toList();
	}

	public void removeRecruitmentPositionsNotIn(final Set<Position> nextPositions) {
		final List<Position> prevPositions = getPositions();

		for (final Position prevPosition : prevPositions) {
			if (!nextPositions.contains(prevPosition)) {
				recruitmentPositions.removeIf(recruitmentPosition -> recruitmentPosition.hasPosition(prevPosition));
			}
		}
	}

	public void removeRecruitmentStacksNotIn(final Set<Stack> nextStacks) {
		final List<Stack> prevStacks = getStacks();

		for (final Stack prevStack : prevStacks) {
			if (!nextStacks.contains(prevStack)) {
				recruitmentStacks.removeIf(recruitmentStack -> recruitmentStack.hasStack(prevStack));
			}
		}
	}

	// public List<Stack> removeRecruitmentStacksNotIn(final Set<Stack> nextStacks) {
	// 	final List<Stack> prevStacks = getStacks();
	//
	// 	return prevStacks.stream()
	// 			.filter(nextStacks::contains)
	// 			.toList();
	// }

	public List<Stack> getStacks() {
		return recruitmentStacks.stream()
				.map(RecruitmentStack::getStack)
				.toList();
	}

	public void ensureCorrectApplicantUser(final User applicantUser) {
		if (!containsApplicantUser(applicantUser)) {
			throw new IllegalStateException("지원자 목록에 존재하지 않는 사용자입니다.");
		}
	}

	private boolean containsApplicantUser(final User applicantUser) {
		return applicants.stream()
				.anyMatch(applicant -> applicant.getUser().equals(applicantUser));
	}

	public void acceptApplicant(final User applicantUser) {
		final Applicant applicant = getApplicant(applicantUser);
		applicant.changeStatus(ApplicantStatus.ACCEPTED);
		// 지원자 수락시 소프트 딜리트 추가
		applicant.softDelete();
	}

	public void rejectApplicant(final User applicantUser) {
		final Applicant applicant = getApplicant(applicantUser);
		applicant.changeStatus(ApplicantStatus.REFUSED);
		// 지원자 거절시 소프트 딜리트 제거
		// applicant.softDelete();
	}

	public Applicant getApplicant(final User applicantUser) {
		ApplicantId applicantId = new ApplicantId(id, applicantUser.getId());

		return applicants.stream()
				.filter(applicant -> applicant.getId().equals(applicantId))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("지원하지 않은 사용자입니다."));
	}

	public boolean isIdEquals(final Long recruitmentId) {
		return Objects.equals(this.id, recruitmentId);
	}

	public void validateDuplicatePositions(final List<Position> positions) {
		final boolean isDuplicatePosition = this.recruitmentPositions.stream()
				.anyMatch(r -> positions.contains(r.getPosition()));
		if (isDuplicatePosition) {
			throw new IllegalArgumentException("이미 존재하는 포지션입니다.");
		}
	}

	public void validateDuplicateStacks(final List<Stack> stacks) {
		final boolean isDuplicateStack = this.recruitmentStacks.stream()
				.anyMatch(r -> stacks.contains(r.getStack()));
		if (isDuplicateStack) {
			throw new IllegalArgumentException("이미 존재하는 스택입니다.");
		}
	}

	public int getApplicantsCount() {
		return (int)applicants.stream()
				.filter(Applicant::isActive)
				.count();
	}

}
