package com.ludo.study.studymatchingplatform.notification.repository.config;

import static com.ludo.study.studymatchingplatform.notification.domain.config.QGlobalNotificationUserConfig.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.domain.config.NotificationConfigGroup;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GlobalNotificationUserConfigRepositoryImpl {

	private final JPAQueryFactory q;

	private final GlobalNotificationUserConfigJpaRepository globalNotificationUserConfigJpaRepository;

	public GlobalNotificationUserConfig save(final GlobalNotificationUserConfig globalNotificationUserConfig) {
		return globalNotificationUserConfigJpaRepository.save(globalNotificationUserConfig);
	}

	public Optional<GlobalNotificationUserConfig> findByUserId(final Long userId) {
		return Optional.ofNullable(
				q.selectFrom(globalNotificationUserConfig)
						.where(globalNotificationUserConfig.user.id.eq(userId))
						.fetchOne()
		);
	}

	public boolean isUserNotificationConfigIsTrue(final Long userId,
												  final NotificationConfigGroup notificationConfigGroup) {
		return q.selectOne()
				.from(globalNotificationUserConfig)
				.where(globalNotificationUserConfig.user.id.eq(userId),
						isConfigTrue(notificationConfigGroup))
				.fetchOne() != null;
	}

	private BooleanExpression isConfigTrue(final NotificationConfigGroup notificationConfigGroup) {
		return switch (notificationConfigGroup) {
			case ALL_CONFIG -> globalNotificationUserConfig.allConfig.isTrue();
			case RECRUITMENT_CONFIG -> globalNotificationUserConfig.recruitmentConfig.isTrue();
			case STUDY_APPLICANT_CONFIG -> globalNotificationUserConfig.studyApplicantConfig.isTrue();
			case STUDY_APPLICANT_RESULT_CONFIG -> globalNotificationUserConfig.studyApplicantResultConfig.isTrue();
			case STUDY_END_DATE_CONFIG -> globalNotificationUserConfig.studyEndDateConfig.isTrue();
			case STUDY_PARTICIPANT_LEAVE_CONFIG -> globalNotificationUserConfig.studyParticipantLeaveConfig.isTrue();
			case REVIEW_CONFIG -> globalNotificationUserConfig.reviewConfig.isTrue();
		};
	}

}
