package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment;

import java.time.LocalDateTime;

import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.category.CategoryResponse;

public record WriteRecruitmentStudyInfoResponse(

		StudyInfoResponse study

) {

	record StudyInfoResponse(

			Long id,
			String title,
			Platform platform,
			Way way,
			Integer participantLimit,
			LocalDateTime startDateTime,
			LocalDateTime endDateTime,
			CategoryResponse category

	) {

		public static StudyInfoResponse from(final Study study) {
			final CategoryResponse categoryResponse = CategoryResponse.from(study.getCategory());
			return new WriteRecruitmentStudyInfoResponse.StudyInfoResponse(
					study.getId(), study.getTitle(), study.getPlatform(), study.getWay(), study.getParticipantLimit(),
					study.getStartDateTime(), study.getEndDateTime(), categoryResponse);
		}

	}

	public static WriteRecruitmentStudyInfoResponse from(final Study study) {
		final StudyInfoResponse studyInfoResponse = StudyInfoResponse.from(study);
		return new WriteRecruitmentStudyInfoResponse(studyInfoResponse);
	}

}
