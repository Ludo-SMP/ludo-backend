package com.ludo.study.studymatchingplatform.study.domain;

import java.util.Arrays;

public enum Way {

	ONLINE,
	OFFLINE;

	public static Way from(String way) {
		return Arrays.stream(Way.values())
				.filter(w -> w.toString().equals(way))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException(String.format("잘못된 way 문자입니다. %s\n", way)));
	}

}
