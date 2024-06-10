package com.ludo.study.studymatchingplatform.notification.domain.config;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType.*;

import java.util.List;

import com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType;

import lombok.Getter;

@Getter
public enum NotificationConfigGroup {

	ALL_CONFIG(List.of(NotificationEventType.values())),
	RECRUITMENT_CONFIG(List.of(RECRUITMENT)),
	STUDY_APPLICANT_CONFIG(List.of(STUDY_APPLICANT)),
	STUDY_APPLICANT_RESULT_CONFIG(List.of(STUDY_APPLICANT_ACCEPT, STUDY_APPLICANT_REJECT)),
	STUDY_END_DATE_CONFIG(List.of(STUDY_END_DATE)),
	STUDY_PARTICIPANT_LEAVE_CONFIG(List.of(STUDY_PARTICIPANT_LEAVE, STUDY_PARTICIPANT_LEAVE_APPLY)),
	REVIEW_CONFIG(List.of(STUDY_REVIEW_START, REVIEW_RECEIVE, REVIEW_PEER_FINISH));

	private final List<NotificationEventType> notificationEventTypes;

	NotificationConfigGroup(List<NotificationEventType> notificationEventTypes) {
		this.notificationEventTypes = notificationEventTypes;
	}

}
