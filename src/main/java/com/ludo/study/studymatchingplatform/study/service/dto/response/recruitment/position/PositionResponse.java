package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class PositionResponse {

	private final Long id;

	private final String name;

	public static PositionResponse from(final Position position) {
		return new PositionResponse(
				position.getId(),
				position.getName()
		);
	}
}
