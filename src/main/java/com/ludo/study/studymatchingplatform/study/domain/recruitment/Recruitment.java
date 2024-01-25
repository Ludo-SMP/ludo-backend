package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.Study;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Recruitment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recruitment_id")
	private Long id;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "study_id")
	private Study study;

	@OneToMany(
		fetch = LAZY,
		mappedBy = "recruitment"
	)
	private List<Applicant> applicants = new ArrayList<>();

	public Integer getApplicantCount() {
		return applicants.size();
	}

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

	private String callUrl;
	private String title;
	private String content;

	private int hits = 0;
	private LocalDateTime recruitmentEndDateTime;
	private int recruitmentLimit;

	public void connectToStudy(Study study) {
		this.study = study;
	}

	public void addStack(RecruitmentStack recruitmentStack) {
		this.recruitmentStacks.add(recruitmentStack);
	}

	public void addPosition(RecruitmentPosition recruitmentPosition) {
		this.recruitmentPositions.add(recruitmentPosition);
	}

	public void upHit() {
		hits++;
	}

}
