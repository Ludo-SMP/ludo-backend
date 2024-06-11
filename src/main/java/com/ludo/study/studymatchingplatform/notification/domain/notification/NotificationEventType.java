package com.ludo.study.studymatchingplatform.notification.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationEventType {

	RECRUITMENT(
			"관심 모집 공고 알림",
			"[루도가 알려요] 관심 항목에 해당하는 모집공고가 나왔습니다.",
			"해당 항목으로 검색된 스터디원 모집 공고를 확인하시려면 클릭헤주세요."
	),

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
					%d일 이후에도 스터디를 진행해야할 시, 팀장은 스터디 수정을 통해 마감 기한을 늘려주시기 바랍니다.
					해당 알림을 클릭하면 해당 스터디 페이지로 이동합니다."""
	),

	STUDY_PARTICIPANT_LEAVE(
			"스터디 탈퇴자 알림",
			"[스터디원 탈퇴] %s 스터디에서 %s님이 탈퇴했습니다.",
			"%s 스터디에서 %s님이 탈퇴했습니다."
	),

	STUDY_PARTICIPANT_LEAVE_APPLY(
			"스터디 탈퇴 요청 알림",
			"[스터디 탈퇴 승인] %s 스터디에서 %s님이 탈퇴 요청했습니다!",
			"%s님의 탈퇴 요청에 답하시려면 클릭해주세요."
	),

	STUDY_REVIEW_START(
			"스터디 리뷰 시작 알림",
			"[스터디원 리뷰] %s 스터디를 완주했습니다! 함께 했던 스터디원들에게 리뷰를 남겨주세요.",
			"해당 스터디원들의 리뷰를 작성하시려면 클릭해주세요."
	),

	REVIEW_RECEIVE(
			"리뷰 받음 알림",
			"[스터디원 리뷰] 진행 완료된 %s 스터디에서 %s님이 회원님에 대한 리뷰를 작성했습니다.",
			"해당 스터디원에 대한 리뷰를 작성하시려면 클릭해주세요."
	),

	REVIEW_PEER_FINISH(
			"상호 리뷰 완료 알림",
			"[스터디원 리뷰] 진행 완료된 %s 스터디에서 %s님과 주고 받은 리뷰가 업로드 되었습니다.",
			"해당 리뷰를 보시려면 클릭해주세요."
	);

	private final String description;
	private final String titleFormat;
	private final String contentFormat;

	NotificationEventType(String description, String titleFormat, String contentFormat) {
		this.description = description;
		this.titleFormat = titleFormat;
		this.contentFormat = contentFormat;
	}

}
