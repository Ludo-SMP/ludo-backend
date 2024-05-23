package com.ludo.study.studymatchingplatform.notification.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationEventType {

	STUDY_APPLICANT(
			"스터디 지원 현황 알림",
			"[스터디원 모집] %s 스터디에 새로운 지원자가 생겼어요.",
			"해당 지원자를 보려면 클릭해주세요."
	),

	STUDY_APPLICANT_ACCEPT(
			"스터디 지원 수락 알림",
			"[스터디 지원] 지원한 %s 스터디에 합류됐습니다.",
			"""
					지원하신 %s 스터디 지원에 승인되셨습니다.
					해당 알림을 클릭하면 스터디로 이동합니다."""
	),

	STUDY_APPLICANT_REJECT(
			"스터디 지원 거절 알림",
			"[스터디 지원] 지원한 %s 스터디에서 거절됐습니다.",
			"""
					지원하신 %s 스터디 지원에 거절되셨습니다.
					스터디에 지원해주셔서 감사합니다."""
	),

	STUDY_END_DATE(
			"스터디 종료 기간 알림",
			"[스터디 마감 임박] %s 스터디 마감 기한이 %d일 남았습니다!",
			"""
					5일 이후에도 스터디를 진행해야할 시, 팀장은 스터디 수정을 통해 마감 기한을 늘려주시기 바랍니다.
					해당 알림을 클릭하면 해당 스터디 페이지로 이동합니다."""
	),

	STUDY_PARTICIPANT_LEAVE(
			"스터디 탈퇴자 알림",
			"[스터디원 탈퇴] ‘스터디명' 스터디에서 %s이 탈퇴했습니다.",
			"%s 스터디에서 %s 이 탈퇴했습니다."
	),

	STUDY_PARTICIPANT_LEAVE_APPLY(
			"스터디 탈퇴 요청 알림",
			"스터디 탈퇴 요청 알림 제목 - 임시",
			"스터디 탈퇴 요청 알림 내용 - 임시"
	),

	RECRUITMENT(
			"관심 모집 공고 알림",
			"[루도가 알려요] 관심 항목으로 선택한 %s 모집 공고가 나왔습니다.",
			"해당 항목으로 검색된 스터디원 모집 공고를 확인하시려면 클릭헤주세요."
	),

	REVIEW_START(
			"리뷰 시작 알림",
			"리뷰 시작 알림 제목 - 임시",
			"리뷰 시작 알림 내용 - 임시"
	),

	REVIEW_RECEIVE(
			"리뷰 받음 알림",
			"리뷰 받음 알림 제목 - 임시",
			"리뷰 받음 알림 내용 - 임시"
	),

	REVIEW_PEER_FINISH(
			"상호 리뷰 완료 알림",
			"상호 리뷰 완료 알림 제목 - 임시",
			"상호 리뷰 완료 알림 내용 - 임시"
	);

	private final String description;
	private final String title;
	private final String content;

	NotificationEventType(String description, String title, String content) {
		this.description = description;
		this.title = title;
		this.content = content;
	}
	
}
