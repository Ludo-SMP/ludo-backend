package com.ludo.study.studymatchingplatform.user.domain.user;

public enum Social {

    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao"),
    NONE("none");

    private final String name;

    Social(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
