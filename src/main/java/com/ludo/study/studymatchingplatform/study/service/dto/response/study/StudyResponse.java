package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.category.CategoryResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.participant.ParticipantUserResponse;
import com.ludo.study.studymatchingplatform.user.service.dto.response.UserResponse;

import lombok.Builder;

@Builder
public record StudyResponse(

		Long id,
		StudyStatus status,
		Boolean hasRecruitment,
		String title,
		Platform platform,
		String platformUrl,
		Way way,
		Integer participantLimit,
		Integer participantCount,
		Integer applicantCount,
		List<Integer> attendanceDay,
		LocalDateTime startDateTime,
		LocalDateTime endDateTime,
		CategoryResponse category,
		UserResponse owner,
		List<ParticipantUserResponse> participants,
		LocalDateTime createdDateTime,
		LocalDateTime updatedDateTime

) {

	public static StudyResponse from(final Study study) {
		final List<ParticipantUserResponse> participants = study.getParticipants().stream()
				.filter(participant -> participant.getDeletedDateTime() == null)
				.map(ParticipantUserResponse::from)
				.toList();

		final CategoryResponse category = CategoryResponse.from(study.getCategory());
		final UserResponse owner = UserResponse.from(study.getOwner());

		Integer applicantCount = 0; // 지원자 수
		if (Boolean.TRUE.equals(study.ensureHasRecruitment())) {
			final Recruitment recruitment = study.getRecruitment();
			applicantCount = recruitment.getApplicantsCount();
		}

		return StudyResponse.builder()
				.id(study.getId())
				.status(study.getStatus())
				.hasRecruitment(study.ensureHasRecruitment())
				.category(category)
				.owner(owner)
				.title(study.getTitle())
				.platform(study.getPlatform())
				.platformUrl(study.getPlatformUrl())
				.participants(participants)
				.way(study.getWay())
				.participantLimit(study.getParticipantLimit())
				.participantCount(study.getParticipantCount())
				.applicantCount(applicantCount)
				.attendanceDay(study.getAttendanceDay())
				.startDateTime(study.getStartDateTime())
				.endDateTime(study.getEndDateTime())
				.createdDateTime(study.getCreatedDateTime())
				.updatedDateTime(study.getUpdatedDateTime())
				.build();
	}

}
