package com.ludo.study.studymatchingplatform.study.service.dto.request.study;

import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;

public record StudyStatusRequest(

		Long studyId,
		StudyStatus status

) {

}
