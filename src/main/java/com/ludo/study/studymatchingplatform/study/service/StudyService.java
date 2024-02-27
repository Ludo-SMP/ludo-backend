package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.User;

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

}
