package com.ludo.study.studymatchingplatform.study.domain.recruitment;

import static jakarta.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE recruitment SET deleted_date_time = NOW() WHERE recruitment_id = ?")
@SQLRestriction("deleted_date_time is null")
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

	@Column(nullable = false)
	@Size(max = 2048)
	private String callUrl;

	@Column(nullable = false)
	@Size(max = 50)
	private String title;

	@Column(nullable = false)
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
	private int recruitmentCount;

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

}
