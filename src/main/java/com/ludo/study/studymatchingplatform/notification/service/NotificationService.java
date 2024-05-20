package com.ludo.study.studymatchingplatform.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.notification.repository.dto.RecruitmentNotifierCondition;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

	private final UserRepositoryImpl userRepository;

	public List<User> recruitmentNotice(final Recruitment recruitment) {
		final RecruitmentNotifierCondition recruitmentNotifierCondition = new RecruitmentNotifierCondition(
				recruitment.getCategory(),
				recruitment.getPositions(),
				recruitment.getStacks());

		List<User> recruitmentNotifiers = userRepository.findRecruitmentNotifiers(recruitmentNotifierCondition);
		log.info("recruitmentNotifiers = {}", recruitmentNotifiers);

		// TODO: 알림 대상자에게 SSE 실시간 알림 전송로직 추가
		// TODO: 실시간 알림 전송 후 알림 테이블에 저장하는 로직 추가
		return recruitmentNotifiers;
	}

	public void studyApplicantNotice(final Recruitment recruitment) {
		final List<User> studyParticipantUsers = userRepository.findParticipantUsersByStudyId(recruitment.getStudyId());

		// TODO: 알림 대상자에게 SSE 실시간 알림 전송로직 추가
		// TODO: 실시간 알림 전송 후 알림 테이블에 저장하는 로직 추가
	}

}
