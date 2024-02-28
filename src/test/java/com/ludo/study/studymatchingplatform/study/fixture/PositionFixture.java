package com.ludo.study.studymatchingplatform.study.fixture;

import com.ludo.study.studymatchingplatform.study.domain.Position;

public class PositionFixture {

	public static Position createPosition(String name) {
		return Position.builder()
				.name(name)
				.build();

	}
}
