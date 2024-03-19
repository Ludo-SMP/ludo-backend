package com.ludo.study.studymatchingplatform.study.service.study;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyStatusService {

	private final RecruitmentRepositoryImpl recruitmentRepository;
	private final StudyRepositoryImpl studyRepository;

	@Transactional
	public StudyResponse changeStatus(final Long studyId, final StudyStatus status, final User user) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디 입니다."));
		study.ensureStudyEditable(user);
		study.modifyStatus(status, LocalDateTime.now());
		hasRecruitment(study);
		studyRepository.save(study);
		return StudyResponse.from(study);
	}

	private void hasRecruitment(final Study study) {
		if (studyRepository.hasRecruitment(study.getId())) {
			if (study.getStatus() == StudyStatus.RECRUITING) {
				study.activateForRecruitment();
			}
			if (study.getStatus() != StudyStatus.RECRUITING) {
				study.deactivateForRecruitment();
			}
			recruitmentRepository.save(study.getRecruitment());
		}
	}

}
