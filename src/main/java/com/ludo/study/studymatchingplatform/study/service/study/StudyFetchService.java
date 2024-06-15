package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Calender;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.attendance.AttendanceRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.attendance.CalenderRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.Details;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.DetailsRepositoryImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyFetchService {

    private final StudyRepositoryImpl studyRepository;
	private final ParticipantRepositoryImpl participantRepository;
	private final DetailsRepositoryImpl detailsRepository;
	private final CalenderRepositoryImpl calenderRepository;
	private final AttendanceRepositoryImpl attendanceRepository;

    public StudyResponse getStudyDetails(final User user, final Long studyId) {
        final Study study = studyRepository.findByIdWithParticipants(studyId)
                .orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));

		if (!study.isParticipating(user)) {
			throw new IllegalArgumentException("스터디원만 조회 가능합니다.");
		}

		final Participant participant = findParticipantByUserIdAndStudyId(user.getId(), studyId);
		final Details details = findDetailsByUserId(user.getId());

		// endDateTime 에 따른 진행 완료 상태 변경
		final LocalDateTime now = LocalDateTime.now();
		if (study.getStatus() != StudyStatus.COMPLETED
				&& study.getEndDateTime().isBefore(now)) { // 스터디 진행완료
			study.modifyStatusToCompleted(now);
			final List<Calender> calenders = findCalendersByStudyId(studyId);
			details.makeAverageAttendanceRate(participant, calenders);
			detailsRepository.save(details);
		}

		return StudyResponse.from(study);
	}

	private Participant findParticipantByUserIdAndStudyId(final Long userId, final Long studyId) {
		return participantRepository.find(userId, studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("유효하지 않은 참여자 입니다."));
	}

	private Details findDetailsByUserId(final Long userId) {
		return detailsRepository.findByUserId(userId)
				.orElseThrow(() -> new SocialAccountNotFoundException("유효하지 않은 사용자 입니다."));
	}

	private List<Calender> findCalendersByStudyId(final Long studyId) { // 리스트로 받아와야 함
		return calenderRepository.findByStudyId(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("스터디에 일정표가 존재하지 않습니다."));
	}

}
