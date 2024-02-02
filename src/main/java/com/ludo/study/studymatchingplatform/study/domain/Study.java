package com.ludo.study.studymatchingplatform.study.domain;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.user.domain.User;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Study extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "study_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(
		nullable = false,
		columnDefinition = "char(10)"
	)
	private StudyStatus status;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "category_id",
		nullable = false
	)
	private Category category;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "owner_id",
		nullable = false
	)
	private User owner;

	@Column(
		nullable = false,
		length = 50
	)
	private String title;

	@OneToOne(
		mappedBy = "study",
		fetch = LAZY
	)
	private Recruitment recruitment;

	@OneToMany(
		mappedBy = "study",
		fetch = LAZY
	)
	private List<Participant> participants = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(
		nullable = false,
		columnDefinition = "char(10)"
	)
	private Way way;

	@Column(nullable = false)
	private int participantLimit;

	@Column(nullable = false)
	private LocalDateTime startDateTime;

	@Column(nullable = false)
	private LocalDateTime endDateTime;

	public void registerRecruitment(final Recruitment recruitment) {
		this.recruitment = recruitment;
		this.recruitment.connectToStudy(this);
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

	public String getOwnerNickname() {
		return owner.getNickname();
	}

}
