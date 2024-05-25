package com.ludo.study.studymatchingplatform.study.fixture.recruitment.position;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;

public class PositionFixture {

	public static Position createPosition(String name) {
		return Position.builder()
				.name(name)
				.build();

	}

	public static Position createPosition(Long id, String name) {
		return Position.builder()
				.id(id)
				.name(name)
				.build();
	}

}
