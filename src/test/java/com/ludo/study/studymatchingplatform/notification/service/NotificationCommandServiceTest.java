package com.ludo.study.studymatchingplatform.notification.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.notification.domain.notification.Notification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.repository.notification.NotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.study.FixedUtcDateTimePicker;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import jakarta.persistence.EntityManager;

@SpringBootTest
@ActiveProfiles("test")
class NotificationCommandServiceTest {

	@Autowired
	NotificationCommandService notificationCommandService;

	@Autowired
	NotificationRepositoryImpl notificationRepository;

	@Autowired
	UserRepositoryImpl userRepository;

	User user1;
	User user2;
	User user3;
	User user4;

	@BeforeEach
	void setup() {
		user1 = userRepository.save(UserFixture.USER1());
		user2 = userRepository.save(UserFixture.USER2());
		user3 = userRepository.save(UserFixture.USER3());
		user4 = userRepository.save(UserFixture.USER4());
	}

	@Nested
	@DisplayName("알림 읽음 처리 메서드는")
	class Describe_CheckNotificationsAsRead {

		UtcDateTimePicker utcDateTimePicker = new FixedUtcDateTimePicker();

		@Autowired
		StudyRepositoryImpl studyRepository;

		@Autowired
		RecruitmentRepositoryImpl recruitmentRepository;

		@Autowired
		ReviewRepositoryImpl reviewRepository;

		@Autowired
		EntityManager em;

		@Test
		@Transactional
		@DisplayName("특정 사용자의 notification_id에 해당하는 알림을 읽음처리 한다.")
		void notificationReadUpdateToTrue() {
			// given
			LocalDateTime now = utcDateTimePicker.now();
			LocalDateTime nowPlus = utcDateTimePicker.now().plusDays(5L);

			Study study = studyRepository.save(StudyFixture.STUDY1(user1, now, nowPlus));
			Recruitment recruitment = recruitmentRepository.save(createRecruitment(study));
			study.addRecruitment(recruitment);

			Review review = Review.builder()
					.study(study)
					.reviewer(user1)
					.reviewee(user2)
					.build();
			reviewRepository.save(review);

			List<Notification> recruitmentNotifications = setupNotificationEvents(now, recruitment, user1);
			List<Notification> studyNotifications = setupStudyNotificationEvents(now, study, user1);
			List<Notification> reviewNotifications = setupReviewNotificationEvents(now, review, user1);

			// when
			notificationCommandService.updateNotificationsAsRead(user1, mapToNotificationIds(recruitmentNotifications));
			notificationCommandService.updateNotificationsAsRead(user1, mapToNotificationIds(studyNotifications));
			notificationCommandService.updateNotificationsAsRead(user1, mapToNotificationIds(reviewNotifications));

			// then
			List<Notification> notifications = notificationRepository.findAllByUserId(user1.getId());
			assertThat(notifications)
					.hasSize(10)
					.allMatch(Notification::isRead);
		}

		private Recruitment createRecruitment(Study study) {
			return Recruitment.builder()
					.study(study)
					.contact(Contact.KAKAO)
					.callUrl("callUrl")
					.title("모집공고1 제목")
					.content("모집공고1 내용")
					.applicantCount(5)
					.recruitmentEndDateTime(LocalDateTime.now().plusDays(10).truncatedTo(ChronoUnit.MICROS))
					.hits(1)
					.build();
		}

		private List<Long> mapToNotificationIds(List<Notification> notifications) {
			return notifications.stream()
					.map(Notification::getId)
					.toList();
		}

		private List<Notification> setupNotificationEvents(LocalDateTime now, Recruitment actor, User notifier) {
			return notificationRepository.saveAll(
					List.of(RecruitmentNotification.of(NotificationEventType.RECRUITMENT, now, actor, notifier)));
		}

		private List<Notification> setupStudyNotificationEvents(LocalDateTime now, Study actor, User notifier) {
			return notificationRepository.saveAll(List.of(
					StudyNotification.of(NotificationEventType.STUDY_APPLICANT, now, actor, notifier),
					StudyNotification.of(NotificationEventType.STUDY_END_DATE, now, actor, notifier),
					StudyNotification.of(NotificationEventType.STUDY_APPLICANT_ACCEPT, now, actor, notifier),
					StudyNotification.of(NotificationEventType.STUDY_PARTICIPANT_LEAVE, now, actor, notifier),
					StudyNotification.of(NotificationEventType.STUDY_PARTICIPANT_LEAVE_APPLY, now, actor, notifier),
					StudyNotification.of(NotificationEventType.STUDY_APPLICANT, now, actor, notifier),
					StudyNotification.of(NotificationEventType.STUDY_REVIEW_START, now, actor, notifier)
			));
		}

		private List<Notification> setupReviewNotificationEvents(LocalDateTime now, Review actor, User notifier) {
			return notificationRepository.saveAll(List.of(
					ReviewNotification.of(NotificationEventType.REVIEW_RECEIVE, now, actor, notifier),
					ReviewNotification.of(NotificationEventType.REVIEW_PEER_FINISH, now, actor, notifier)));
		}

	}
}