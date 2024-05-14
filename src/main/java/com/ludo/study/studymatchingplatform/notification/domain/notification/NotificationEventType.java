package com.ludo.study.studymatchingplatform.notification.domain.notification;

public enum NotificationEventType {

	STUDY_APPLICANT("스터디 지원 현황 알림"),

	STUDY_APPLICANT_RESULT("스터디 지원 결과 알림"),

	STUDY_END_DATE("스터디 종료 기간 알림"),

	STUDY_PARTICIPANT_LEAVE("스터디 탈퇴자 알림"),

	RECRUITMENT("모집 공고 알림"),

	REVIEW("리뷰 평가 알림");

	private final String description;

	NotificationEventType(String description) {
		this.description = description;
	}
	
}
