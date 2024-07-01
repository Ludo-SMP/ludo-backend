package com.ludo.study.studymatchingplatform.study.service.study;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Calender;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.attendance.CalenderRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.StudyUpdateRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyUpdateService {

	private final StudyRepositoryImpl studyRepository;
	private final CategoryRepositoryImpl categoryRepository;
	private final PositionRepositoryImpl positionRepository;
	private final ParticipantRepositoryImpl participantRepository;
	private final UserRepositoryImpl userRepository;
	private final UtcDateTimePicker utcDateTimePicker;
	private final StudyStatusService studyStatusService;
	private final CalenderRepositoryImpl calenderRepository;

	@Transactional
	public StudyResponse update(final User user, final Long studyId, final StudyUpdateRequest request) {
		final User owner = valifyExistUser(user.getId());
		final Category category = findCategoryById(request.categoryId());
		final Platform platform = valifyExistPlatform(request.platform());
		final Way way = valifyExistWay(request.way());
		final Study study = findStudyById(studyId);
		study.ensureStudyEditable(owner);

		final Position ownerPosition = findPositionById(request.positionId());
		final Participant participant = findParticipantByIds(study.getId(), user.getId());
		participant.updatePosition(ownerPosition); // 업데이트 진행

		study.update(request.title(), category, request.participantLimit(),
				request.attendanceDay(), way, platform, request.platformUrl(),
				request.startDateTime(), request.endDateTime());

		studyRepository.save(study);
		participantRepository.save(participant);

		final List<Integer> attendanceDay = request.attendanceDay();
		final List<Calender> calenders = findCalendersByStudyId(studyId);
		deleteCalenders(calenders); // 기존 캘린더 제거
		createCalender(study, attendanceDay); // 새로운 캘린더 만들기

		// 수정된 endDateTime이 현재 시간 이전일 경우 진행 완료 상태로 변경
		final LocalDate now = utcDateTimePicker.now().toLocalDate();
		final LocalDate endDate = study.getEndDateTime().toLocalDate();
		if (study.getStatus() != StudyStatus.COMPLETED
				&& endDate.isBefore(now)) { // 스터디 진행완료
			studyStatusService.end(user, studyId);
		}

		return StudyResponse.from(study);
	}

	private void createCalender(final Study study, final List<Integer> attendanceDay) {
		LocalDate calenderStartDateTime = settingTheCalenderStartDate(study.getStartDateTime().toLocalDate());
		final LocalDate calenderEndDateTime = settingTheCalenderEndDate(study.getEndDateTime().toLocalDate());
		LocalDate temporaryEndDateTime = calenderStartDateTime.plusDays(6);
		while (!temporaryEndDateTime.isEqual(calenderEndDateTime.plusDays(7))) { // 날짜가 같지 않을때 까지
			final Calender calender = Calender.from(study, calenderStartDateTime, temporaryEndDateTime);

			calender.checkValidAttendanceDay(attendanceDay); // 유효 출석일 체크

			calenderStartDateTime = calenderStartDateTime.plusDays(7);
			temporaryEndDateTime = temporaryEndDateTime.plusDays(7);
			calenderRepository.save(calender);
		}
	}

	private LocalDate settingTheCalenderStartDate(LocalDate startDateTime) {
		final DayOfWeek startDayOfWeek = startDateTime.getDayOfWeek();
		final Integer startDayOfWeekNumber = startDayOfWeek.getValue();
		if (startDayOfWeekNumber > 1) { // 월요일 보다 클 경우
			startDateTime = startDateTime.minusDays(startDayOfWeekNumber - 1);
		}
		return startDateTime;
	}

	private LocalDate settingTheCalenderEndDate(LocalDate endDateTime) {
		final DayOfWeek endDayOfWeek = endDateTime.getDayOfWeek();
		final Integer endDayOfWeekNumber = endDayOfWeek.getValue();
		if (endDayOfWeekNumber < 7) { // 일요일 보다 작을 경우
			endDateTime = endDateTime.plusDays(7 - endDayOfWeekNumber);
		}
		return endDateTime;
	}

	private List<Calender> findCalendersByStudyId(final Long studyId) {
		return calenderRepository.findByStudyId(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 일정표 입니다."));
	}

	private void deleteCalenders(final List<Calender> calenders) {
		calenderRepository.deleteExistingCalendars(calenders);
	}

	private Study findStudyById(final Long studyId) {
		return studyRepository.findById(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));
	}

	private Position findPositionById(final Long positionId) {
		return positionRepository.findById(positionId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 포지션입니다."));
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() ->
						new EntityNotFoundException("존재하지 않는 카테고리입니다"));
	}

	private Platform valifyExistPlatform(final String platform) {
		if (platform == null) {
			return null;
		}
		return Stream.of(Platform.values())
				.filter(p -> p.name().equals(platform))
				.findFirst()
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 플랫폼 입니다."));
	}

	private Way valifyExistWay(final String way) {
		return Stream.of(Way.values())
				.filter(w -> w.name().equals(way))
				.findFirst()
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 진행 방식 입니다."));
	}

	private User valifyExistUser(final Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 사용자 입니다."));
	}

	private Participant findParticipantByIds(final Long studyId, final Long userId) {
		return participantRepository.find(studyId, userId)
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참가자 입니다."));
	}

	private List<Participant> findParticipantsByStudyId(final Long studyId) {
		return participantRepository.findByStudyId(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 참여자들 입니다."));
	}

}
