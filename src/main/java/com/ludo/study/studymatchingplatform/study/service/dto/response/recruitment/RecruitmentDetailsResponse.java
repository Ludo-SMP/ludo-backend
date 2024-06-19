package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.common.ResourcePath;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;

public record RecruitmentDetailsResponse(RecruitmentDetail recruitment,
										 StudyDetail study
) {

	public record RecruitmentDetail(Long id, Integer applicantCount, Integer applicantLimit,
									List<PositionDetail> positions, List<StackDetail> stacks,
									Contact contact, String callUrl, String title, String content,
									LocalDateTime endDateTime,
									LocalDateTime createdDateTime, LocalDateTime updatedDateTime) {
	}

	public record PositionDetail(Long id, String name) {
	}

	public record StackDetail(Long id, String name, String imageUrl) {
	}

	public record StudyDetail(Long id, String title, OwnerDetail owner, String platform, String way,
							  Integer participantLimit, LocalDateTime startDateTime, LocalDateTime endDateTime,
							  CategoryDetail category) {
	}

	public record OwnerDetail(Long id, String nickname, String email) {
	}

	public record CategoryDetail(Long id, String name) {
	}

	public RecruitmentDetailsResponse(final Recruitment recruitment, final Study study) {
		this(new RecruitmentDetail(
						recruitment.getId(),
						recruitment.getApplicantsCount(),
						recruitment.getApplicantLimit(),
						recruitment.getPositions().stream()
								.map(position -> new PositionDetail(position.getId(), position.getName()))
								.toList(),
						recruitment.getStacks().stream()
								.map(stack -> new StackDetail(stack.getId(), stack.getName(),
										ResourcePath.STACK_IMAGE.getPath() + stack.getImageUrl()))
								.toList(),
						recruitment.getContact(),
						recruitment.getCallUrl(),
						recruitment.getTitle(),
						recruitment.getContent(),
						recruitment.getRecruitmentEndDateTime(),
						recruitment.getCreatedDateTime(),
						recruitment.getUpdatedDateTime()
				),
				new StudyDetail(
						study.getId(),
						study.getTitle(),
						new OwnerDetail(study.getOwnerId(), study.getOwnerNickname(), study.getOwnerEmail()),
						study.getPlatform().toString(),
						study.getWay().toString(),
						study.getParticipantLimit(),
						study.getStartDateTime(),
						study.getEndDateTime(),
						new CategoryDetail(study.getCategoryId(), study.getCategoryName())
				));
	}

}
