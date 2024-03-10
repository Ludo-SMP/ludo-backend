package com.ludo.study.studymatchingplatform.study.service.study;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentService;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyStatusService {

	private final StudyRepositoryImpl studyRepository;
	private final RecruitmentService recruitmentService;

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
