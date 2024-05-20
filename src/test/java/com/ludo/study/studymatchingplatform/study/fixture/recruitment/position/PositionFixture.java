package com.ludo.study.studymatchingplatform.study.fixture.recruitment.position;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;

public class PositionFixture {

	public static final Position BACKEND = Position.builder().name("백엔드").build();
	public static final Position FRONTEND = Position.builder().name("프론트엔드").build();
	public static final Position DESIGNER = Position.builder().name("디자이너").build();
	public static final Position DEVOPS = Position.builder().name("데브옵스").build();

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
