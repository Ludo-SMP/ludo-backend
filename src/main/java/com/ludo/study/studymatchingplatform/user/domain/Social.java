package com.ludo.study.studymatchingplatform.user.domain;

public enum Social {

	GOOGLE("google"),
	NAVER("naver"),
	KAKAO("kakao");

	private final String name;

	Social(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
