package com.ludo.study.studymatchingplatform.study.service.study;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Attendance;
import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Calender;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.attendance.AttendanceRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.attendance.CalenderRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantWithReviewStatisticsResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

	private final StudyRepositoryImpl studyRepository;
	private final ApplicantRepositoryImpl applicantRepository;
	private final ParticipantRepositoryImpl participantRepository;
	private final UtcDateTimePicker utcDateTimePicker;
	private final ReviewStatisticsService reviewStatisticsService;
	private final StudyStatisticsRepositoryImpl studyStatisticsRepository;
	private final ReviewStatisticsRepositoryImpl reviewStatisticsRepository;
	private final UserRepositoryImpl userRepository;
	private final CalenderRepositoryImpl calenderRepository;
	private final AttendanceRepositoryImpl attendanceRepository;

	public void leave(final User user, final Long studyId) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));

		final Participant participant = study.getParticipant(user);
		if (study.isOwner(participant)) {
			throw new IllegalStateException("스터디장은 탈퇴가 불가능합니다.");
		}

		// 무단 탈퇴 신뢰도 반영 코드
		final StudyStatistics studyStatistics = findStudyStatisticsByUserId(user.getId());
		studyStatistics.increaseTotalLeftStudyCount(); // 무단 스터디 탈퇴 카운트

		final ReviewStatistics reviewStatistics = findReviewStatisticsByUserId(user.getId());
		reviewStatistics.decreaseTotalActivenessScore(); // 적극성 약 12% 하락

		participant.leave(study, utcDateTimePicker.now());
	}

	public void approvedLeaveRequest(final User user, final Long studyId) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));

		final Participant participant = study.getParticipant(user);
		if (study.isOwner(participant)) {
			throw new IllegalStateException("스터디장은 탈퇴가 불가능합니다.");
		}

		participant.approvedStudyWithdrawalRequest(); // 보류중으로 상태 변경 승인시 탈퇴 처리
		participantRepository.save(participant);
	}

	public void approvedLeave(final Long studyId, final Long userId) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));

		final User member = findUserById(userId);
		final Participant participant = study.getParticipant(member);
		if (study.isOwner(participant)) {
			throw new IllegalStateException("스터디장은 탈퇴가 불가능합니다.");
		}

		// 승인된 탈퇴시 신뢰도 하락등 별도의 액션 없음

		participant.leave(study, utcDateTimePicker.now());
	}

	// 알림 기능 반영 필요
	public void rejectedLeave(final Long studyId, final Long userId) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));

		final User member = findUserById(userId);
		final Participant participant = study.getParticipant(member);
		if (study.isOwner(participant)) {
			throw new IllegalStateException("스터디장은 탈퇴가 불가능합니다.");
		}

		participant.rejectedStudyWithdrawalRequest(); // 보류중으로 상태 변경 승인시 탈퇴 처리
		participantRepository.save(participant);
	}

	public ApplicantWithReviewStatisticsResponse findApplicantsWithReviewStatistics(final User user,
																					final Long studyId) {
		final Study study = findByIdWithRecruitment(studyId);
		// 스터디 참여자 검증
		study.getParticipant(user);
		final Recruitment recruitment = study.getRecruitment();
		final List<Applicant> applicants =
				applicantRepository.findStudyApplicantInfoByRecruitmentId(recruitment.getId());
		final List<Long> applicantsUserIds = applicants.stream()
				.map(a -> a.getUser().getId()).toList();

		return ApplicantWithReviewStatisticsResponse.from(
				study,
				applicants,
				reviewStatisticsService.findByUserIdsIn(applicantsUserIds)
		);
	}

	public ApplicantResponse findApplicantsInfo(final User user, final Long studyId) {
		final Study study = findByIdWithRecruitment(studyId);
		// 스터디 참여자 검증
		study.getParticipant(user);
		final Recruitment recruitment = study.getRecruitment();
		final List<Applicant> applicants =
				applicantRepository.findStudyApplicantInfoByRecruitmentId(recruitment.getId());
		return ApplicantResponse.from(study, applicants);
	}

	private Study findByIdWithRecruitment(final Long studyId) {
		return studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));
	}

	public void attendance(final User user, final Long studyId) {
		final LocalDate now = utcDateTimePicker.now().toLocalDate();
		final Study study = findStudyById(studyId);
		final Participant participant = findParticipantByStudyIdAndUserId(studyId, user.getId());
		if (!participant.isFirstAttendance()) {
			final LocalDate recentAttendanceDate = participant.getRecentAttendanceDate();
			isDuplicateAttendance(recentAttendanceDate, now); // 중복 출석 체크
		}
		participant.renewalAttendanceDate(now); // 최근 출석일 갱신
		final Attendance attendance = Attendance.from(study, user, now);

		participant.increaseTotalAttendance();
		attendanceRepository.save(attendance); // 누적 출석일 체크

		final List<Calender> calender = findCalenderByStudyId(studyId);
		for (Calender c : calender) {
			if (now.isAfter(c.getCalenderStartDateTime())
					&& now.isBefore(c.getCalenderEndDateTime())) {
				isValidAttendance(c, now, participant); // 유효 출석일 체크
				break;
			}
		}
		participantRepository.save(participant);
	}

	private void isDuplicateAttendance(final LocalDate recentAttendanceDate, final LocalDate now) {
		if (recentAttendanceDate.getYear() == now.getYear() &&
				recentAttendanceDate.getMonth() == now.getMonth() &&
				recentAttendanceDate.getDayOfMonth() == now.getDayOfMonth()) {
			throw new IllegalArgumentException("출석 체크는 하루에 한 번만 가능합니다.");
		}
	}

	private Optional<Attendance> findMaxIdByStudyIdAndUserId(final Long studyId, final Long userId) {
		return attendanceRepository.findMaxIdByStudyIdAndUserId(studyId, userId);
	}

	private void isValidAttendance(final Calender calender, final LocalDate now, final Participant participant) {
		final DayOfWeek nowOfWeek = now.getDayOfWeek();
		final Integer nowOfWeekNumber = nowOfWeek.getValue();
		switch (nowOfWeekNumber) {
			case 1: // 월요일
				if (Boolean.TRUE.equals(calender.getMonday())) {
					participant.increaseValidAttendance();
				}
				break;
			case 2: // 화요일
				if (Boolean.TRUE.equals(calender.getTuesday())) {
					participant.increaseValidAttendance();
				}
				break;
			case 3: // 수요일
				if (Boolean.TRUE.equals(calender.getWednesday())) {
					participant.increaseValidAttendance();
				}
				break;
			case 4: // 목요일
				if (Boolean.TRUE.equals(calender.getThursday())) {
					participant.increaseValidAttendance();
				}
				break;
			case 5: // 금요일
				if (Boolean.TRUE.equals(calender.getFriday())) {
					participant.increaseValidAttendance();
				}
				break;
			case 6: // 토요일
				if (Boolean.TRUE.equals(calender.getSaturday())) {
					participant.increaseValidAttendance();
				}
				break;
			case 7: // 일요일
				if (Boolean.TRUE.equals(calender.getSunday())) {
					participant.increaseValidAttendance();
				}
				break;
		}
	}

	private Study findStudyById(final Long studyId) {
		return studyRepository.findById(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디 입니다."));
	}

	private Participant findParticipantByStudyIdAndUserId(final Long studyId, final Long userId) {
		return participantRepository.find(studyId, userId)
				.orElseThrow(() -> new SocialAccountNotFoundException("스터디의 참가자가 아닙니다."));
	}

	private List<Calender> findCalenderByStudyId(final Long studyId) { // 리스트로 받아와야 함
		return calenderRepository.findByStudyId(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("스터디에 일정표가 존재하지 않습니다."));
	}

	private StudyStatistics findStudyStatisticsByUserId(final Long userId) {
		return studyStatisticsRepository.findByUserId(userId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 사용자 기록 입니다."));
	}

	private ReviewStatistics findReviewStatisticsByUserId(final Long userId) {
		return reviewStatisticsRepository.findByUserId(userId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 사용자 리뷰 기록 입니다."));
	}

	private User findUserById(final Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 사용자 입니다."));
	}

}
