package com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment;

import java.time.LocalDateTime;
import java.util.Set;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditRecruitmentRequest {

	private final String title;

	private final Contact contact;

	private final String callUrl;

	private final Set<Long> stackIds;

	private final Set<Long> positionIds;

	private final Integer applicantCount;

	private final LocalDateTime recruitmentEndDateTime;

	private final String content;

}
