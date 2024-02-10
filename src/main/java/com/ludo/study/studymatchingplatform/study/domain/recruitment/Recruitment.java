package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE recruitment SET deleted_date_time = NOW() WHERE recruitment_id = ?")
@SQLRestriction("deleted_date_time is null")
public class Recruitment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recruitment_id")
	private Long id;

	@OneToOne(fetch = LAZY)
	@JoinColumn(
		name = "study_id",
		nullable = false
	)
	private Study study;

	@OneToMany(
		fetch = LAZY,
		mappedBy = "recruitment"
	)
	private List<Applicant> applicants = new ArrayList<>();

	@OneToMany(
		fetch = LAZY,
		mappedBy = "recruitment"
	)
	private List<RecruitmentStack> recruitmentStacks = new ArrayList<>();

	@OneToMany(
		fetch = LAZY,
		mappedBy = "recruitment"
	)
	private List<RecruitmentPosition> recruitmentPositions = new ArrayList<>();

	@Column(
		nullable = false,
		length = 2048
	)
	@Size(max = 2048)
	private String callUrl;

	@Column(
		nullable = false,
		length = 50
	)
	@Size(max = 50)
	private String title;

	@Column(
		nullable = false,
		length = 2000
	)
	@Size(max = 2000)
	private String content;

	@Column(nullable = false)
	@PositiveOrZero
	private int hits = 0;

	@Column(nullable = false)
	@FutureOrPresent
	private LocalDateTime recruitmentEndDateTime;

	@Column(nullable = false)
	@PositiveOrZero
	private int recruitmentLimit;

	public static Recruitment of(
		final String title,
		final String content,
		final int recruitmentLimit,
		final LocalDateTime recruitmentEndDateTime,
		final Study study
	) {
		return Recruitment.builder()
			.title(title)
			.content(content)
			.recruitmentLimit(recruitmentLimit)
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
	public void addStack(RecruitmentStack recruitmentStack) {
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
		List<String> stacks = new ArrayList<>();
		for (RecruitmentStack recruitmentStack : recruitmentStacks) {
			stacks.add(recruitmentStack.getStack().getName());
		}
		return stacks;
	}

	public List<String> getPositionNames() {
		List<String> positions = new ArrayList<>();
		for (RecruitmentPosition recruitmentPosition : recruitmentPositions) {
			positions.add(recruitmentPosition.getPosition().getName());
		}
		return positions;
	}

	public void addRecruitmentStacks(final List<RecruitmentStack> recruitmentStacks) {
		this.recruitmentStacks.addAll(recruitmentStacks);
	}

	public void addRecruitmentPositions(final List<RecruitmentPosition> recruitmentPositions) {
		this.recruitmentPositions.addAll(recruitmentPositions);
	}

	public void edit(
		final String title,
		final String content,
		final String callUrl,
		final int hits,
		final int recruitmentLimit,
		final LocalDateTime recruitmentEndDateTime
	) {
		this.title = title;
		this.content = content;
		this.callUrl = callUrl;
		this.hits = hits;
		this.recruitmentLimit = recruitmentLimit;
		this.recruitmentEndDateTime = recruitmentEndDateTime;
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

	public boolean hasStack(final Stack stack) {
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
			.ifPresent(Applicant::applyOrThrow);
	}

	public Optional<Applicant> findApplicant(final User user) {
		return applicants.stream()
			.filter(a -> a.equals(user))
			.findFirst();
	}

	public boolean isOwner(final User user) {
		return study.isOwner(user);
	}
}
