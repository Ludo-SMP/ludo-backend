package com.ludo.study.studymatchingplatform.notification.service;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.repository.dto.RecruitmentNotifierCondition;
import com.ludo.study.studymatchingplatform.notification.repository.dto.StudyEndDateNotifyCondition;
import com.ludo.study.studymatchingplatform.notification.repository.notification.RecruitmentNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.ReviewNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.notification.StudyNotificationRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationResponse;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

	// for notifier find
	private final UserRepositoryImpl userRepository;
	private final ParticipantRepositoryImpl participantRepository;
	private final ReviewRepositoryImpl reviewRepository;

	// for notification save
	private final StudyNotificationRepositoryImpl studyNotificationRepository;
	private final RecruitmentNotificationRepositoryImpl recruitmentNotificationRepository;
	private final ReviewNotificationRepositoryImpl reviewNotificationRepository;

	// for notification server sent events
	private final SseEmitters sseEmitters;

	@Transactional
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

	@Transactional
	public void studyApplicantNotice(final Recruitment recruitment) {
		// 알림 대상자 조회
		final Study study = recruitment.getStudy();
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

	@Transactional
	public void studyApplicantAcceptNotice(final Study study, final User applicantUser) {
		// 알림 대상자 조회
		final Applicant applicant = study.getApplicant(applicantUser);
		applicant.ensureApplicantStatus(ApplicantStatus.ACCEPTED);

		// 알림 저장
		final StudyNotification studyNotification = studyNotificationRepository.save(
				StudyNotification.of(STUDY_APPLICANT_ACCEPT, LocalDateTime.now(), study, applicantUser));

		// 실시간 알림 전송 요청
		sseEmitters.sendNotification(applicantUser, new NotificationResponse(studyNotification));
	}

	@Transactional
	public void studyApplicantRejectNotice(final Study study, final User applicantUser) {
		// 알림 대상자 조회
		final Applicant applicant = study.getApplicant(applicantUser);
		applicant.ensureApplicantStatus(ApplicantStatus.REFUSED);

		// 알림 저장
		final StudyNotification studyNotification = studyNotificationRepository.save(
				StudyNotification.of(STUDY_APPLICANT_REJECT, LocalDateTime.now(), study, applicantUser));

		// 실시간 알림 전송 요청
		sseEmitters.sendNotification(applicantUser, new NotificationResponse(studyNotification));
	}

	// TODO: 스터디 탈퇴 기능 구현 완료되면 해당 서비스에서 호출하는 로직
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

	// TODO: 스터디 탈퇴 기능 구현 완료되면 해당 서비스에서 호출하는 로직
	public void studyParticipantLeaveApplyNotice(final Study study) {
		// 알림 대상자 조회
		final User studyOwner = study.getOwner();

		// 알림 저장
		final StudyNotification studyNotification = studyNotificationRepository.save(
				StudyNotification.of(STUDY_PARTICIPANT_LEAVE_APPLY, LocalDateTime.now(), study, studyOwner));

		// 실시간 알림 전송 요청
		sseEmitters.sendNotification(studyNotification.getNotifier(), new NotificationResponse(studyNotification));
	}

	@Scheduled(cron = "0 0 8 * *")
	@Transactional
	public void studyEndDateNotice() {
		// 알림 대상자 조회 - 종료 기간까지 N일 남은 스터디 참가자 중 모든 방장
		final List<Participant> ownersOfStudiesEndingIn = participantRepository.findOwnersOfStudiesEndingIn(
				new StudyEndDateNotifyCondition(5L));

		// 알림 저장
		final List<StudyNotification> studyNotifications = studyNotificationRepository.saveAll(ownersOfStudiesEndingIn
				.stream()
				.map(notifier -> StudyNotification.of(
						STUDY_END_DATE, LocalDateTime.now(), notifier.getStudy(), notifier.getUser()))
				.toList()
		);

		// 실시간 알림 전송 요청
		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	@Scheduled(cron = "0 0 0 * *")
	@Transactional
	public void studyReviewStartNotice(final Study study) {
		// 알림 대상자 조회
		final List<User> studyParticipantUsers = userRepository.findParticipantUsersByStudyId(study.getId());

		// 알림 저장
		final List<StudyNotification> studyNotifications = studyNotificationRepository.saveAll(
				studyParticipantUsers
						.stream()
						.map(notifier -> StudyNotification.of(STUDY_REVIEW_START, LocalDateTime.now(), study, notifier))
						.toList()
		);

		// 실시간 알림 전송 요청
		studyNotifications.forEach(studyNotification -> {
			final User notifier = studyNotification.getNotifier();
			final NotificationResponse notificationResponse = new NotificationResponse(studyNotification);
			sseEmitters.sendNotification(notifier, notificationResponse);
		});
	}

	@Transactional
	public void reviewReceiveNotice(final Review review) {
		// 알림 대상자 조회
		final User reviewee = review.getReviewee();

		// 알림 저장
		final ReviewNotification reviewNotification = reviewNotificationRepository.save(
				ReviewNotification.of(REVIEW_RECEIVE, LocalDateTime.now(), review, reviewee));

		// 실시간 알림 전송 요청
		sseEmitters.sendNotification(reviewee, new NotificationResponse(reviewNotification));
	}

	@Transactional
	public void reviewPeerFinishNotice(final Study study, final Review review) {
		// 알림 대상자 조회
		final User reviewer = review.getReviewer();
		final User reviewee = review.getReviewee();
		if (!isPeerReviewFinish(study, reviewee, reviewer)) {
			return;
		}

		// 알림 저장
		final ReviewNotification reviewNotificationOfReviewer = reviewNotificationRepository.save(
				ReviewNotification.of(REVIEW_PEER_FINISH, LocalDateTime.now(), review, reviewer));
		final ReviewNotification reviewNotificationOfReviewee = reviewNotificationRepository.save(
				ReviewNotification.of(REVIEW_PEER_FINISH, LocalDateTime.now(), review, reviewee));

		// 실시간 알림 전송 요청
		sseEmitters.sendNotification(reviewer, new NotificationResponse(reviewNotificationOfReviewer));
		sseEmitters.sendNotification(reviewee, new NotificationResponse(reviewNotificationOfReviewee));
	}

	private boolean isPeerReviewFinish(final Study study, final User reviewee, final User reviewer) {
		return reviewRepository.exists(study.getId(), reviewee.getId(), reviewer.getId());
	}

	@Transactional
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
