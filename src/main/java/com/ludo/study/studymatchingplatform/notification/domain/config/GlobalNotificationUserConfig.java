package com.ludo.study.studymatchingplatform.notification.domain.config;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalNotificationUserConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "global_notification_user_config_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private Boolean allConfig;
	private Boolean recruitmentConfig;
	private Boolean studyApplicantConfig;
	private Boolean studyApplicantResultConfig;
	private Boolean studyEndDateConfig;
	private Boolean studyParticipantLeaveConfig;
	private Boolean reviewConfig;

	private GlobalNotificationUserConfig(User user, Boolean allConfig, Boolean recruitmentConfig,
										 Boolean studyApplicantConfig, Boolean studyApplicantResultConfig,
										 Boolean studyEndDateConfig, Boolean studyParticipantLeaveConfig,
										 Boolean reviewConfig) {
		this.user = user;
		this.allConfig = allConfig;
		this.recruitmentConfig = recruitmentConfig;
		this.studyApplicantConfig = studyApplicantConfig;
		this.studyApplicantResultConfig = studyApplicantResultConfig;
		this.studyEndDateConfig = studyEndDateConfig;
		this.studyParticipantLeaveConfig = studyParticipantLeaveConfig;
		this.reviewConfig = reviewConfig;
	}

	public static GlobalNotificationUserConfig of(final User user,
												  final Boolean allConfig,
												  final Boolean recruitmentConfig,
												  final Boolean studyApplicantConfig,
												  final Boolean studyApplicantResultConfig,
												  final Boolean studyEndDateConfig,
												  final Boolean studyParticipantLeaveConfig,
												  final Boolean reviewConfig
	) {
		return new GlobalNotificationUserConfig(user, allConfig,
				recruitmentConfig, studyApplicantConfig,
				studyApplicantResultConfig, studyEndDateConfig,
				studyParticipantLeaveConfig, reviewConfig);
	}

	public static GlobalNotificationUserConfig ofNewSignUpUser(final User user) {
		GlobalNotificationUserConfig globalNotificationUserConfig = new GlobalNotificationUserConfig();
		globalNotificationUserConfig.user = user;
		globalNotificationUserConfig.onAllConfig();
		return globalNotificationUserConfig;
	}

	public void updateConfig(final NotificationConfigGroup configGroup, final boolean enabled) {
		switch (configGroup) {
			case ALL_CONFIG -> onAllConfig();
			case RECRUITMENT_CONFIG -> updateRecruitmentConfig(enabled);
			case STUDY_APPLICANT_CONFIG -> updateStudyApplicantConfig(enabled);
			case STUDY_APPLICANT_RESULT_CONFIG -> updateStudyApplicantResultConfig(enabled);
			case STUDY_END_DATE_CONFIG -> updateStudyEndDateConfig(enabled);
			case STUDY_PARTICIPANT_LEAVE_CONFIG -> updateStudyParticipantLeaveConfig(enabled);
			case REVIEW_CONFIG -> updateReviewConfig(enabled);
		}
	}

	private void onAllConfig() {
		this.allConfig = true;
		this.recruitmentConfig = true;
		this.studyApplicantConfig = true;
		this.studyApplicantResultConfig = true;
		this.studyEndDateConfig = true;
		this.studyParticipantLeaveConfig = true;
		this.reviewConfig = true;
	}

	private void updateRecruitmentConfig(boolean enabled) {
		this.recruitmentConfig = enabled;
	}

	private void updateStudyApplicantConfig(boolean enabled) {
		this.studyApplicantConfig = enabled;
	}

	private void updateStudyApplicantResultConfig(boolean enabled) {
		this.studyApplicantResultConfig = enabled;
	}

	private void updateStudyEndDateConfig(boolean enabled) {
		this.studyEndDateConfig = enabled;
	}

	private void updateStudyParticipantLeaveConfig(boolean enabled) {
		this.studyParticipantLeaveConfig = enabled;
	}

	private void updateReviewConfig(boolean enabled) {
		this.studyParticipantLeaveConfig = enabled;
	}

}
