package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.StudyApplicantDecisionRequest;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyApplicantDecisionService {

	private final StudyRepositoryImpl studyRepository;
	private final UserRepositoryImpl userRepository;
	private final ApplicantRepositoryImpl applicantRepository;

	@Transactional
	public void applicantAccept(final User owner, final StudyApplicantDecisionRequest request) {
		final Study study = findStudy(request.studyId());
		final User applicantUser = findUser(request.applicantUserId());

		study.acceptApplicant(owner, applicantUser, request.recruitmentId());
	}

	public void applicantReject(final User owner, final StudyApplicantDecisionRequest request) {
		final Study study = findStudy(request.studyId());
		final User applicantUser = findUser(request.applicantUserId());

		// TODO
	}

	private User findUser(final Long applicantUserId) {
		return userRepository.findById(applicantUserId)
				.orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
	}

	private Study findStudy(final Long studyId) {
		return studyRepository.findById(studyId)
				.orElseThrow(() -> new IllegalStateException("존재하지 않는 스터디입니다."));
	}
}
