package com.ludo.study.studymatchingplatform.notification.service;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.repository.dto.RecruitmentNotifierCondition;
import com.ludo.study.studymatchingplatform.notification.repository.notification.RecruitmentNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.ReviewNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.StudyNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationResponse;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

	// for notifier search
	private final UserRepositoryImpl userRepository;
	private final ApplicantRepositoryImpl applicantRepository;

	// for notification save & find
	private final StudyNotificationRepositoryImpl studyNotificationRepository;
	private final RecruitmentNotificationRepositoryImpl recruitmentNotificationRepository;
	private final ReviewNotificationRepositoryImpl reviewNotificationRepository;

	// for notification server sent events
	private final SseEmitters sseEmitters;

	public List<User> recruitmentNotice(final Recruitment recruitment) {
		// 알림 대상자 조회
		final RecruitmentNotifierCondition recruitmentNotifierCondition = new RecruitmentNotifierCondition(
				recruitment.getCategory(),
				recruitment.getPositions(),
				recruitment.getStacks());
		final List<User> recruitmentNotifiers = userRepository.findRecruitmentNotifiers(recruitmentNotifierCondition);

		// 알림 저장
		final List<RecruitmentNotification> recruitmentNotifications = recruitmentNotificationRepository.saveAll(
				recruitmentNotifiers
						.stream()
						.map(notifier -> RecruitmentNotification.of(
								RECRUITMENT, LocalDateTime.now(), recruitment, notifier))
						.toList()
		);

		// 실시간 알림 전송 요청
		recruitmentNotifications.forEach(recruitmentNotification -> {
			final User notifier = recruitmentNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(recruitmentNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});

		return recruitmentNotifiers;
	}

	public void studyApplicantNotice(final Study study) {
		// 알림 대상자 조회
		final List<User> studyParticipantUsers = userRepository.findParticipantUsersByStudyId(study.getId());

		// 알림 저장
		final List<StudyNotification> studyNotifications = studyNotificationRepository.saveAll(studyParticipantUsers
				.stream()
				.map(notifier -> StudyNotification.of(STUDY_APPLICANT, LocalDateTime.now(),
						study, notifier))
				.toList());

		// 실시간 알림 전송 요청
		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	public void studyApplicantResultNotice(final Recruitment recruitment, final User user) {
		final Applicant applicant = applicantRepository.find(recruitment.getId(), user.getId())
				.orElseThrow(() -> new IllegalStateException("지원하지 않은 사용자입니다."));

		// TODO: 실시간 알림 전송 후 알림 테이블에 저장하는 로직 추가
		if (applicant.statusIsEqualTo(ApplicantStatus.ACCEPTED)) {

		}

		if (applicant.statusIsEqualTo(ApplicantStatus.REFUSED)) {

		}
		// TODO: 알림 대상자에게 SSE 실시간 알림 전송로직 추가

	}

	public void studyParticipantLeaveNotice(final Study study) {
		// 알림 대상자 조회
		final List<User> studyParticipantUsers = userRepository.findParticipantUsersByStudyId(study.getId());

		// 알림 저장
		final List<StudyNotification> studyNotifications = studyNotificationRepository.saveAll(studyParticipantUsers
				.stream()
				.map(notifier -> StudyNotification.of(STUDY_PARTICIPANT_LEAVE, LocalDateTime.now(), study, notifier))
				.toList());

		// 실시간 알림 전송 요청
		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	public void studyParticipantLeaveApplyNotice(final Study study) {
		// 알림 대상자 조회
		final User studyOwner = study.getOwner();

		// 알림 저장
		final StudyNotification studyNotification = studyNotificationRepository.save(
				StudyNotification.of(STUDY_PARTICIPANT_LEAVE_APPLY, LocalDateTime.now(), study, studyOwner));

		// 실시간 알림 전송 요청
		sseEmitters.sendNotification(studyNotification.getNotifier(), new NotificationResponse(studyNotification));
	}

	public void reviewStartNotice(final Study study) {
		final List<User> studyParticipantUsers = userRepository.findParticipantUsersByStudyId(study.getId());

		// TODO: 실시간 알림 전송 후 알림 테이블에 저장하는 로직 추가. 리뷰 기능 완성돼야 진행 가능
		// TODO: 알림 대상자에게 SSE 실시간 알림 전송로직 추가
	}

	/**
	 * TODO: 리뷰 기능 구현 완료 후 진행
	 * @param: maybe Review Domain Entity
	 * 리뷰 테이블 조회 로직이 필요한데, 중복 구현 가능성
	 */
	public void reviewReceiveNotice() {
	}

	/**
	 * TODO: 리뷰 기능 구현 완료 후 진행
	 * @param: maybe Review Domain Entity
	 * 리뷰 테이블 조회 로직이 필요한데, 중복 구현 가능성
	 */
	public void reviewPeerFinishNotice(final Study study) {

	}

	// TODO: @Scheduled 적용
	public void studyEndDateNotice() {
		// 종료 기간까지 N일 남은 스터디 조회
		// 해당 스터디의 방장 조회
	}

	public List<NotificationResponse> findNotifications(final User user) {
		final Long userId = user.getId();
		final List<RecruitmentNotification> recruitmentNotifications = recruitmentNotificationRepository
				.findAllByUserId(userId);
		final List<StudyNotification> studyNotifications = studyNotificationRepository.findAllByUserId(userId);
		final List<ReviewNotification> reviewNotifications = reviewNotificationRepository.findAllByUserId(userId);

		final List<NotificationResponse> notificationResponses = new ArrayList<>();
		recruitmentNotifications.stream()
				.map(NotificationResponse::new)
				.forEach(notificationResponses::add);

		studyNotifications.stream()
				.map(NotificationResponse::new)
				.forEach(notificationResponses::add);

		reviewNotifications.stream()
				.map(NotificationResponse::new)
				.forEach(notificationResponses::add);

		return notificationResponses;
	}

}
