package com.ludo.study.studymatchingplatform.study.service.recruitment.applicant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.notification.service.NotificationService;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.applicant.StudyApplicantDecisionRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.participant.ParticipantUserResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyApplicantDecisionService {

	private final StudyRepositoryImpl studyRepository;
	private final UserRepositoryImpl userRepository;
	private final ParticipantRepositoryImpl participantRepository;
	private final UtcDateTimePicker utcDateTimePicker;

	private final NotificationService notificationService;

	@Transactional
	public ParticipantUserResponse applicantAccept(final User owner, final StudyApplicantDecisionRequest request) {
		final Study study = findStudy(request.studyId());
		final User applicantUser = findUser(request.applicantUserId());

		study.acceptApplicant(owner, applicantUser, utcDateTimePicker.now());
		Participant participant = findParticipant(study, applicantUser);

		// 스터디 지원 수락 알림 트리거
		notificationService.studyApplicantAcceptNotice(study, applicantUser);

		return ParticipantUserResponse.from(participant);
	}

	@Transactional
	public void applicantReject(final User owner, final StudyApplicantDecisionRequest request) {
		final Study study = findStudy(request.studyId());
		final User applicantUser = findUser(request.applicantUserId());

		study.rejectApplicant(owner, applicantUser);

		// 스터디 지원 거절 알림 트리거
		notificationService.studyApplicantRejectNotice(study, applicantUser);
	}

	private User findUser(final Long applicantUserId) {
		return userRepository.findById(applicantUserId)
				.orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
	}

	private Study findStudy(final Long studyId) {
		return studyRepository.findById(studyId)
				.orElseThrow(() -> new IllegalStateException("존재하지 않는 스터디입니다."));
	}

	private Participant findParticipant(Study study, User applicantUser) {
		return participantRepository.find(study.getId(), applicantUser.getId())
				.orElseThrow(() -> new IllegalStateException("존재하지 않은 스터디 참가자입니다."));
	}
}
