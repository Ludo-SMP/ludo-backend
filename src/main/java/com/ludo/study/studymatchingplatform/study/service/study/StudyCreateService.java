package com.ludo.study.studymatchingplatform.study.service.study;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Calender;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.attendance.CalenderRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.BusinessException;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.Details;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.DetailsRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// 캐시 제거 적용 필요
public class StudyCreateService {

	private final StudyRepositoryImpl studyRepository;
	private final CategoryRepositoryImpl categoryRepository;
	private final PositionRepositoryImpl positionRepository;
	private final ParticipantRepositoryImpl participantRepository;
	private final UserRepositoryImpl userRepository;
	private final DetailsRepositoryImpl detailsRepository;
	private final CalenderRepositoryImpl calenderRepository;
	private final UtcDateTimePicker utcDateTimePicker;

	@Transactional
	public StudyResponse create(final WriteStudyRequest request, final User user) {
		final User owner = valifyExistUser(user.getId());
		final Category category = findCategoryById(request.categoryId());
		final Position ownerPosition = findPositionById(request.positionId());
		final Platform platform = valifyExistPlatform(request.platform());
		final Way way = valifyExistWay(request.way());
		final Study study = request.toStudy(owner, category, way, platform);

		// 생성된 스터디의 endDateTime 이 현재보다 이전일 경우 진행 완료 상태로 변경
		final LocalDateTime now = utcDateTimePicker.now();

		final Participant participant = Participant.from(study, owner, ownerPosition, Role.OWNER);
		study.addParticipant(participant);
		valifyEmptyParticipants(study);

		studyRepository.save(study);
		participantRepository.save(participant);

		final List<Integer> attendanceDay = request.attendanceDay();
		createCalender(study, attendanceDay); // 캘린더 만들기

		final Details details = findDetailsByUserId(user);
		// 생성된 스터디의 endDateTime 이 현재보다 이전일 경우 진행 완료 상태로 변경
		if (study.getStatus() != StudyStatus.COMPLETED
				&& study.getEndDateTime().isBefore(now)) { // 스터디 진행완료
			study.modifyStatusToCompleted(now);
			final List<Calender> calenders = findCalendersByStudyId(study.getId());
			details.makeAverageAttendanceRate(participant, calenders);
			detailsRepository.save(details);
		}

		return StudyResponse.from(study);
	}

	private void createCalender(final Study study, final List<Integer> attendanceDay) {
		LocalDateTime calenderStartDateTime = settingTheCalenderStartDate(study.getStartDateTime());
		final LocalDateTime calenderEndDateTime = settingTheCalenderEndDate(study.getEndDateTime());
		LocalDateTime temporaryEndDateTime = calenderStartDateTime.plusDays(6);
		while (!temporaryEndDateTime.isEqual(calenderEndDateTime.plusDays(7))) { // 날짜가 같지 않을때 까지
			final Calender calender = Calender.from(study, calenderStartDateTime, temporaryEndDateTime);

			calender.checkValidAttendanceDay(attendanceDay); // 유효 출석일 체크

			calenderStartDateTime = calenderStartDateTime.plusDays(7);
			temporaryEndDateTime = temporaryEndDateTime.plusDays(7);
			calenderRepository.save(calender);
		}
	}

	private LocalDateTime settingTheCalenderStartDate(LocalDateTime startDateTime) {
		final DayOfWeek startDayOfWeek = startDateTime.getDayOfWeek();
		final Integer startDayOfWeekNumber = startDayOfWeek.getValue();
		if (startDayOfWeekNumber > 1) { // 월요일 보다 클 경우
			startDateTime = startDateTime.minusDays(startDayOfWeekNumber - 1);
		}
		return startDateTime;
	}

	private LocalDateTime settingTheCalenderEndDate(LocalDateTime endDateTime) {
		final DayOfWeek endDayOfWeek = endDateTime.getDayOfWeek();
		final Integer endDayOfWeekNumber = endDayOfWeek.getValue();
		if (endDayOfWeekNumber < 7) { // 일요일 보다 작을 경우
			endDateTime = endDateTime.plusDays(7 - endDayOfWeekNumber);
		}
		return endDateTime;
	}

	private void addOwnerAsParticipant(final Study study, final Participant participant) {
		study.addParticipant(participant);
		valifyEmptyParticipants(study);
	}

	private Position findPositionById(final Long positionId) {
		return positionRepository.findById(positionId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 포지션 입니다."));
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 카테고리 입니다."));
	}

	private User valifyExistUser(final Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 사용자 입니다."));
	}

	private Platform valifyExistPlatform(final String platform) {
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

	private void valifyEmptyParticipants(final Study study) {
		final List<Participant> participants = study.getParticipants();
		if (participants.isEmpty()) {
			throw new BusinessException("스터디 생성시 참여자 리스트는 비어있을 수 없습니다.");
		}
	}

	private List<Calender> findCalendersByStudyId(final Long studyId) { // 리스트로 받아와야 함
		return calenderRepository.findByStudyId(studyId)
				.orElseThrow(() -> new SocialAccountNotFoundException("스터디에 일정표가 존재하지 않습니다."));
	}

	private Details findDetailsByUserId(final User user) {
		return detailsRepository.findByUserId(user.getId())
				.orElseThrow(() -> new SocialAccountNotFoundException("유효하지 않은 사용자 입니다."));
	}

}
