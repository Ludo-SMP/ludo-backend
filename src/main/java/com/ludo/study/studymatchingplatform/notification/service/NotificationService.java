package com.ludo.study.studymatchingplatform.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.repository.dto.RecruitmentNotifierCondition;
import com.ludo.study.studymatchingplatform.notification.repository.notification.RecruitmentNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.ReviewNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.StudyNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
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
		final RecruitmentNotifierCondition recruitmentNotifierCondition = new RecruitmentNotifierCondition(
				recruitment.getCategory(),
				recruitment.getPositions(),
				recruitment.getStacks());

		List<User> recruitmentNotifiers = userRepository.findRecruitmentNotifiers(recruitmentNotifierCondition);
		log.info("recruitmentNotifiers = {}", recruitmentNotifiers);

		// TODO: 알림 테이블에 저장 테스트
		final List<RecruitmentNotification> recruitmentNotifications = recruitmentNotificationRepository.saveAll(
				recruitmentNotifiers
						.stream()
						.map(notifier -> RecruitmentNotification.of(
								NotificationEventType.RECRUITMENT, LocalDateTime.now(), recruitment, notifier))
						.toList());
		// TODO: 알림 대상자에게 SSE 실시간 알림 전송로직 추가

		return recruitmentNotifiers;
	}

	public void studyApplicantNotice(final Study study) {
		final List<User> studyParticipantUsers = userRepository.findParticipantUsersByStudyId(study.getId());

		// TODO: 알림 테이블에 저장 테스트
		final List<StudyNotification> studyNotifications = studyParticipantUsers
				.stream()
				.map(notifier -> new StudyNotification(
						NotificationEventType.STUDY_APPLICANT, LocalDateTime.now(), study, notifier))
				.toList();
		studyNotificationRepository.saveAll(studyNotifications);

		// TODO: 알림 대상자에게 SSE 실시간 알림 전송로직 추가
	}

}
