package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyStatusService {

	private final StudyRepositoryImpl studyRepository;
	private final RecruitmentService recruitmentService;
	private final RecruitmentRepositoryImpl recruitmentRepository;

	@Transactional
	public Study changeStatus(final Long studyId, final StudyStatus status, final User user) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디 입니다."));

		study.ensureStudyEditable(user);
		study.edit(status);

		if (studyRepository.hasRecruitment(studyId) && status != StudyStatus.RECRUITING) {
			recruitmentService.delete(user, studyId);
		}
		return studyRepository.save(study);
	}

}
