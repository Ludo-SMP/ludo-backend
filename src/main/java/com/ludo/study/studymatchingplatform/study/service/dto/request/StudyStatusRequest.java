package com.ludo.study.studymatchingplatform.study.service.dto.request;

import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;

public record StudyStatusRequest(

		Long studyId,
		StudyStatus status

) {

}
