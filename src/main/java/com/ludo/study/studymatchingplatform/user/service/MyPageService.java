package com.ludo.study.studymatchingplatform.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

	private final ParticipantRepositoryImpl participantRepository;
	private final ApplicantRepositoryImpl applicantRepository;

	public List<Participant> retrieveParticipantStudies(final User user) {
		return participantRepository.findByUserId(user.getId()).orElseThrow();
	}

	public List<Applicant> retrieveApplicantRecruitment(final User user) {
		return applicantRepository.findByUserId(user.getId()).orElseThrow();
	}

	public List<Participant> retrieveCompletedStudy(final User user) {
		return participantRepository.findCompletedStudyByUserId(user.getId()).orElseThrow();
	}

}
