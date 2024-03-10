package com.ludo.study.studymatchingplatform.study.service.study;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyService {

	private final StudyRepositoryImpl studyRepository;

	public void leave(final User user, final Long studyId) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 스터디입니다."));

		final Participant participant = study.getParticipant(user);
		if (study.isOwner(participant)) {
			throw new IllegalStateException("스터디장은 탈퇴가 불가능합니다.");
		}
		participant.leave(study);
	}

	public ApplicantResponse findApplicantsInfo(final User user, final Long studyId) {
		final Study study = findByIdWithRecruitment(studyId);
		// 스터디 참여자 검증
		study.getParticipant(user);
		final Recruitment recruitment = study.getRecruitment();
		return ApplicantResponse.from(study, recruitment.getApplicants());
	}

	private Study findByIdWithRecruitment(final Long studyId) {
		return studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 스터디입니다."));
	}

}