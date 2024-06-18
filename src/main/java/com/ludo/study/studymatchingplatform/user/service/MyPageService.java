package com.ludo.study.studymatchingplatform.user.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.id.ApplicantId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.Details;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.service.dto.response.MyPageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

	private final ParticipantRepositoryImpl participantRepository;
	private final ApplicantRepositoryImpl applicantRepository;
	private final StudyRepositoryImpl studyRepository;
	private final StudyStatisticsRepositoryImpl studyStatisticsRepository;
	private final ReviewStatisticsRepositoryImpl reviewStatisticsRepository;
	private final UtcDateTimePicker utcDateTimePicker;

	@Transactional
	public MyPageResponse retrieveMyPage(final User user) {
		final LocalDateTime now = utcDateTimePicker.now();
		ensureStudyStatusCompleted(user, now);
		final List<Participant> participants = retrieveParticipantStudies(user);
		final List<Applicant> applicants = retrieveApplyRecruitment(user);
		final List<Participant> completedStudies = retrieveCompletedStudy(user);
		final StudyStatistics studyStatistics = retrieveStudyStatistics(user);
		final ReviewStatistics reviewStatistics = retrieveReviewStatistics(user);
		return MyPageResponse.from(user, studyStatistics, reviewStatistics, participants, applicants, completedStudies);
	}

	// 테스트 케이스의 불변 유지하기 위해 now 를 컨트롤러에서 받아오도록 변경
	private void ensureStudyStatusCompleted(final User user, final LocalDateTime now) {
		final List<Participant> participants = participantRepository.findByUserId(user.getId());
		for (Participant participant : participants) {
			final Study study = participant.getStudy();
			if (study.getStatus() != StudyStatus.COMPLETED
					&& study.getEndDateTime().isBefore(now)) { // 스터디 진행완료
				study.modifyStatusToCompleted(now);
			}
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

	private StudyStatistics retrieveStudyStatistics(final User user) {
		return studyStatisticsRepository.findByUserId(user.getId())
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 사용자 기록 입니다."));
	}

	private ReviewStatistics retrieveReviewStatistics(final User user) {
		return reviewStatisticsRepository.findByUserId(user.getId())
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 사용자 리뷰 기록 입니다."));
	}

	public void deleteApplyHistory(final User user, final Long recruitmentId) {
		final ApplicantId applicantId = new ApplicantId(recruitmentId, user.getId());
		final Applicant applicant = applicantRepository.findById(applicantId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원 기록 입니다."));
		// 모집 공고 지원 기록 삭제 시 softDelete 처리
		applicant.softDelete(utcDateTimePicker.now());
		applicantRepository.save(applicant);
	}

}
