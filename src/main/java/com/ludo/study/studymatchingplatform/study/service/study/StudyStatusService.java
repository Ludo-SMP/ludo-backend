package com.ludo.study.studymatchingplatform.study.service.study;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.common.exception.DataForbiddenException;
import com.ludo.study.studymatchingplatform.common.exception.DataNotFoundException;
import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Calender;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.attendance.CalenderRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyStatisticsService;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyStatusService {

	private final RecruitmentRepositoryImpl recruitmentRepository;
	private final StudyRepositoryImpl studyRepository;
	private final UtcDateTimePicker utcDateTimePicker;
	private final StudyStatisticsService studyStatisticsService;
	private final CalenderRepositoryImpl calenderRepository;

	@Transactional
	public StudyResponse changeStatus(final Long studyId, final StudyStatus status, final User user) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디 입니다."));
		study.ensureStudyEditable(user);
		study.modifyStatus(status, utcDateTimePicker.now());
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
				study.deactivateForRecruitment(utcDateTimePicker.now());
			}
			recruitmentRepository.save(study.getRecruitment());
		}
	}

	public StudyResponse end(final User user, final Long studyId) {
		final Study study = studyRepository.findById(studyId)
				.orElseThrow(() -> new DataNotFoundException("존재하지 않는 스터디입니다."));

		//	if (!study.isOwner(user)) {
		//		throw new DataForbiddenException("스터디를 종료할 권한이 없습니다.");
		//	}

		final List<User> users = study.getParticipants().stream()
				.map(Participant::getUser)
				.toList();
		final List<StudyStatistics> statistics = studyStatisticsService.findOrCreateByUsers(users);
		final List<Calender> calenders = findCalendersByStudyId(study.getId());

		// TODO: 스터디 출석 일수 end에 추가
		study.end(utcDateTimePicker.now(), statistics, calenders);

		return StudyResponse.from(study);
	}

	private List<Calender> findCalendersByStudyId(final Long studyId) { // 리스트로 받아와야 함
		return calenderRepository.findByStudyId(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("스터디에 일정표가 존재하지 않습니다."));
	}

}
