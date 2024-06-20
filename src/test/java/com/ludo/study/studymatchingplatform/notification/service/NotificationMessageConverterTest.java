package com.ludo.study.studymatchingplatform.notification.service;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyEndDateNotifyCond;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.message.NotificationMessage;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

class NotificationMessageConverterTest {

	static final String STUDY_TITLE = "스프링 프로젝트 만들기";

	NotificationMessageConverter notificationMessageConverter = new NotificationMessageConverter();

	@Test
	@DisplayName("관심 모집공고 알림 메시지")
	void recruitmentMessage() {
		// given
		RecruitmentNotification notification = RecruitmentNotification.of(RECRUITMENT, null, null, null);
		String expectedTitle = "[루도가 알려요] 관심 항목에 해당하는 모집공고가 나왔습니다.";
		String expectedContent = "해당 항목으로 검색된 스터디원 모집 공고를 확인하시려면 클릭헤주세요.";

		// when
		NotificationMessage notificationMessage = notificationMessageConverter.convertNotifyMessage(notification);

		// then
		assertEqualsNotificationMessage(notificationMessage, expectedTitle, expectedContent);
	}

	@Test
	@DisplayName("스터디 지원 현황 알림 메시지")
	void studyApplicantMessage() {
		// given
		Study study = Study.builder().title(STUDY_TITLE).build();
		User user = User.builder().build();
		StudyNotification notification = StudyNotification.of(STUDY_APPLICANT, null, study, user);

		String expectedTitle = "[스터디원 모집] %s 스터디에 새로운 지원자가 생겼어요.".formatted(STUDY_TITLE);
		String expectedContent = "해당 지원자를 보려면 클릭해주세요.";

		// when
		NotificationMessage notificationMessage = notificationMessageConverter.convertNotifyMessage(notification);

		// then
		assertEqualsNotificationMessage(notificationMessage, expectedTitle, expectedContent);
	}

	@Test
	@DisplayName("스터디 지원 수락 알림 메시지")
	void studyApplicantAcceptMessage() {
		// given
		Study study = Study.builder().title(STUDY_TITLE).build();
		StudyNotification notification = StudyNotification.of(STUDY_APPLICANT_ACCEPT, null, study, null);

		String expectedTitle = "[스터디 지원] 지원한 %s 스터디에 합류됐습니다.".formatted(STUDY_TITLE);
		String expectedContent = """
				지원하신 %s 스터디 지원에 승인되셨습니다.
				해당 알림을 클릭하면 스터디로 이동합니다.""".formatted(STUDY_TITLE);

		// when
		NotificationMessage notificationMessage = notificationMessageConverter.convertNotifyMessage(notification);

		// then
		assertEqualsNotificationMessage(notificationMessage, expectedTitle, expectedContent);
	}

	@Test
	@DisplayName("스터디 지원 거절 알림 메시지")
	void studyApplicantRejectMessage() {
		// given
		Study study = Study.builder().title(STUDY_TITLE).build();
		StudyNotification notification = StudyNotification.of(STUDY_APPLICANT_REJECT, null, study, null);

		String expectedTitle = "[스터디 지원] 지원한 %s 스터디에서 거절됐습니다.".formatted(STUDY_TITLE);
		String expectedContent = """
				지원하신 %s 스터디 지원에 거절되셨습니다.
				스터디에 지원해주셔서 감사합니다.""".formatted(STUDY_TITLE);

		// when
		NotificationMessage notificationMessage = notificationMessageConverter.convertNotifyMessage(notification);

		// then
		assertEqualsNotificationMessage(notificationMessage, expectedTitle, expectedContent);
	}

	@Test
	@DisplayName("스터디 종료 기간 알림")
	void studyEndDateMessage() {
		// given
		Study study = Study.builder().title(STUDY_TITLE).build();
		StudyNotification notification = StudyNotification.of(STUDY_END_DATE, null, study, null);
		Long period = StudyEndDateNotifyCond.remainingPeriod.getPeriod();

		String expectedTitle = String.format("[스터디 마감 임박] %s 스터디 마감 기한이 %d일 남았습니다!", STUDY_TITLE,
				period);
		String expectedContent = """
				%d일 이후에도 스터디를 진행해야할 시, 팀장은 스터디 수정을 통해 마감 기한을 늘려주시기 바랍니다.
				해당 알림을 클릭하면 해당 스터디 페이지로 이동합니다.""".formatted(period);

		// when
		NotificationMessage notificationMessage = notificationMessageConverter.convertNotifyMessage(notification);

		// then
		assertEqualsNotificationMessage(notificationMessage, expectedTitle, expectedContent);
	}

	@Test
	@DisplayName("스터디 탈퇴자 알림 메시지")
	void studyLeaveMessage() {
		// given
		Study study = Study.builder().title(STUDY_TITLE).build();
		StudyNotification notification = StudyNotification.of(STUDY_PARTICIPANT_LEAVE, null, study, null);
		String leaverName = "열공러";
		User studyLeaver = User.builder().nickname(leaverName).build();

		String expectedTitle = "[스터디원 탈퇴] %s 스터디에서 %s님이 탈퇴했습니다.".formatted(STUDY_TITLE, leaverName);
		String expectedContent = "%s 스터디에서 %s님이 탈퇴했습니다.".formatted(STUDY_TITLE, leaverName);

		// when
		// TODO: 스터디 탈퇴 마무리되면 알림 기능 마무리 예정
		NotificationMessage notificationMessage = notificationMessageConverter.convertNotifyMessage(notification);

		// then
		assertEqualsNotificationMessage(notificationMessage, expectedTitle, expectedContent);
	}

	@Test
	@DisplayName("스터디 탈퇴 요청 알림 메시지")
	void studyLeaveApplyMessage() {
		// given
		Study study = Study.builder().title(STUDY_TITLE).build();
		StudyNotification notification = StudyNotification.of(STUDY_PARTICIPANT_LEAVE_APPLY, null, study, null);
		String leaverName = "열공러";
		User studyLeaveApplier = User.builder().nickname(leaverName).build();

		String expectedTitle = "[스터디 탈퇴 승인] %s 스터디에서 %s님이 탈퇴 요청했습니다!".formatted(STUDY_TITLE, leaverName);
		String expectedContent = "%s님의 탈퇴 요청에 답하시려면 클릭해주세요.".formatted(leaverName);

		// when
		// TODO: 스터디 탈퇴 마무리되면 알림 기능 마무리 예정
		NotificationMessage notificationMessage = notificationMessageConverter.convertNotifyMessage(notification);

		// then
		assertEqualsNotificationMessage(notificationMessage, expectedTitle, expectedContent);
	}

	@Test
	@DisplayName("스터디 리뷰 시작 알림 메시지")
	void studyReviewStartMessage() {
		// given
		Study study = Study.builder().title(STUDY_TITLE).build();
		StudyNotification notification = StudyNotification.of(STUDY_REVIEW_START, null, study, null);

		String expectedTitle = "[스터디원 리뷰] %s 스터디를 완주했습니다! 함께 했던 스터디원들에게 리뷰를 남겨주세요.".formatted(STUDY_TITLE);
		String expectedContent = "해당 스터디원들의 리뷰를 작성하시려면 클릭해주세요.";

		// when
		NotificationMessage notificationMessage = notificationMessageConverter.convertNotifyMessage(notification);

		// then
		assertEqualsNotificationMessage(notificationMessage, expectedTitle, expectedContent);
	}

	@Test
	@DisplayName("리뷰 받음 알림 메시지")
	void reviewReceiveMessage() {
		// given
		String reviewerName = "리뷰어 이름";
		String revieweeName = "리뷰이 이름";
		User reviewer = User.builder().nickname(reviewerName).build();
		User reviewee = User.builder().nickname(revieweeName).build();

		Study study = Study.builder().title(STUDY_TITLE).build();
		Review review = Review.builder().study(study).reviewer(reviewer).reviewee(reviewee).build();
		ReviewNotification notification = ReviewNotification.of(REVIEW_RECEIVE, null, review, reviewee);

		String expectedTitle = "[스터디원 리뷰] 진행 완료된 %s 스터디에서 %s님이 회원님에 대한 리뷰를 작성했습니다.".formatted(STUDY_TITLE,
				reviewerName);
		String expectedContent = "해당 스터디원에 대한 리뷰를 작성하시려면 클릭해주세요.";

		// when
		NotificationMessage notificationMessage = notificationMessageConverter.convertNotifyMessage(notification);

		// then
		assertEqualsNotificationMessage(notificationMessage, expectedTitle, expectedContent);
	}

	@Test
	@DisplayName("상호 리뷰 완료 알림 메시지")
	void reviewPeerFinishMessage() {
		// given
		String reviewerName = "리뷰어 이름";
		String revieweeName = "리뷰이 이름";
		User reviewer = User.builder().nickname(reviewerName).build();
		User reviewee = User.builder().nickname(revieweeName).build();

		Study study = Study.builder().title(STUDY_TITLE).build();
		Review review = Review.builder().study(study).reviewer(reviewer).reviewee(reviewee).build();
		ReviewNotification notification = ReviewNotification.of(REVIEW_PEER_FINISH, null, review, reviewee);

		String expectedTitle = "[스터디원 리뷰] 진행 완료된 %s 스터디에서 %s님과 주고 받은 리뷰가 업로드 되었습니다.".formatted(STUDY_TITLE,
				reviewerName);
		String expectedContent = "해당 리뷰를 보시려면 클릭해주세요.";

		// when
		NotificationMessage notificationMessage = notificationMessageConverter.convertNotifyMessage(notification);

		// then
		assertEqualsNotificationMessage(notificationMessage, expectedTitle, expectedContent);
	}

	private void assertEqualsNotificationMessage(NotificationMessage notificationMessage,
												 String expectedTitle,
												 String expectedContent
	) {
		assertThat(notificationMessage.title()).isEqualTo(expectedTitle);
		assertThat(notificationMessage.content()).isEqualTo(expectedContent);
	}

}