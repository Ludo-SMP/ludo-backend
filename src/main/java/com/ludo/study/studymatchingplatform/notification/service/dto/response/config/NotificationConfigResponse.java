package com.ludo.study.studymatchingplatform.notification.service.dto.response.config;

import static com.ludo.study.studymatchingplatform.notification.domain.config.NotificationConfigGroup.*;

import java.util.List;

import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;

public record NotificationConfigResponse(AllConfigResponse allConfig,

										 StudyApplicantConfigResponse studyApplicantConfig,

										 StudyApplicantResultConfigResponse studyApplicantResultConfig,

										 StudyEndDateConfigResponse studyEndDateConfig,

										 StudyParticipantLeaveConfigResponse studyParticipantLeaveConfig,

										 ReviewConfigResponse reviewConfig,

										 RecruitmentConfigResponse recruitmentConfig
) {

	public static NotificationConfigResponse fromUserConfig(final GlobalNotificationUserConfig userConfig,
															final List<NotificationKeywordCategory> notificationKeywordCategories,
															final List<NotificationKeywordPosition> notificationKeywordPositions,
															final List<NotificationKeywordStack> notificationKeywordStacks
	) {

		final AllConfigResponse allConfigResponse = new AllConfigResponse(
				ALL_CONFIG.name(), userConfig.getAllConfig());

		final StudyApplicantConfigResponse studyApplicantConfigResponse = new StudyApplicantConfigResponse(
				STUDY_APPLICANT_CONFIG.name(), userConfig.getStudyApplicantConfig());

		final StudyApplicantResultConfigResponse studyApplicantResultConfigResponse = new StudyApplicantResultConfigResponse(
				STUDY_APPLICANT_RESULT_CONFIG.name(), userConfig.getStudyApplicantResultConfig());

		final StudyEndDateConfigResponse studyEndDateConfig = new StudyEndDateConfigResponse(
				STUDY_END_DATE_CONFIG.name(), userConfig.getStudyEndDateConfig());

		final StudyParticipantLeaveConfigResponse studyParticipantLeaveConfig = new StudyParticipantLeaveConfigResponse(
				STUDY_PARTICIPANT_LEAVE_CONFIG.name(), userConfig.getStudyParticipantLeaveConfig());

		final ReviewConfigResponse reviewConfig = new ReviewConfigResponse(
				REVIEW_CONFIG.name(), userConfig.getReviewConfig());

		final RecruitmentConfigResponse recruitmentConfig = new RecruitmentConfigResponse(
				RECRUITMENT_CONFIG.name(), userConfig.getRecruitmentConfig(),
				mapToCategoryKeyword(notificationKeywordCategories),
				mapToPositionKeyword(notificationKeywordPositions),
				mapToStackKeyword(notificationKeywordStacks));

		return new NotificationConfigResponse(
				allConfigResponse,
				studyApplicantConfigResponse,
				studyApplicantResultConfigResponse,
				studyEndDateConfig,
				studyParticipantLeaveConfig,
				reviewConfig,
				recruitmentConfig);
	}

	private static List<RecruitmentConfigResponse.CategoryKeyword> mapToCategoryKeyword(
			List<NotificationKeywordCategory> notificationKeywordCategories
	) {
		return notificationKeywordCategories
				.stream()
				.map(notificationKeywordCategory -> {
					final Category category = notificationKeywordCategory.getCategory();
					return new RecruitmentConfigResponse.CategoryKeyword(category.getId(), category.getName());
				})
				.toList();
	}

	private static List<RecruitmentConfigResponse.PositionKeyword> mapToPositionKeyword(
			List<NotificationKeywordPosition> notificationKeywordPositions
	) {
		return notificationKeywordPositions
				.stream()
				.map(notificationKeywordPosition -> {
					final Position position = notificationKeywordPosition.getPosition();
					return new RecruitmentConfigResponse.PositionKeyword(position.getId(), position.getName());
				})
				.toList();
	}

	private static List<RecruitmentConfigResponse.StackKeyword> mapToStackKeyword(
			List<NotificationKeywordStack> notificationKeywordStacks
	) {
		return notificationKeywordStacks
				.stream()
				.map(notificationKeywordStack -> {
					final Stack stack = notificationKeywordStack.getStack();
					return new RecruitmentConfigResponse.StackKeyword(stack.getId(), stack.getName());
				})
				.toList();
	}
}
