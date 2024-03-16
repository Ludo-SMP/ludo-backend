package com.ludo.study.studymatchingplatform.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.service.dto.response.MyPageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

	private final ParticipantRepositoryImpl participantRepository;
	private final ApplicantRepositoryImpl applicantRepository;
	private final StudyRepositoryImpl studyRepository;

	public MyPageResponse retrieveMyPage(final User user) {
		ensureStudyStatusCompleted(user);
		final List<Participant> participants = retrieveParticipantStudies(user);
		final List<Applicant> applicants = retrieveApplyRecruitment(user);
		final List<Participant> completedStudies = retrieveCompletedStudy(user);
		return MyPageResponse.from(user, participants, applicants, completedStudies);
	}

	private void ensureStudyStatusCompleted(final User user) {
		final List<Participant> participants = participantRepository.findByUserId(user.getId());
		for (Participant participant : participants) {
			final Study study = participant.getStudy();
			study.modifyStatusToCompleted();
			studyRepository.save(study);
		}
	}

	private List<Participant> retrieveParticipantStudies(final User user) {
		return participantRepository.findByUserId(user.getId());
	}

	private List<Applicant> retrieveApplyRecruitment(final User user) {
		return applicantRepository.findMyPageApplyRecruitmentInfoByUserId(user.getId());
	}

	private List<Participant> retrieveCompletedStudy(final User user) {
		return participantRepository.findCompletedStudyByUserId(user.getId());
	}

}
