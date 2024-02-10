package com.ludo.study.studymatchingplatform.study.domain.recruitment;

public enum ApplicantStatus {

	UNCHECKED,
	ACCEPTED,
	REJECTED,
	REMOVED,
	CANCELLED;

	public void ensureReapplicable() {
		if (this == REJECTED) {
			throw new IllegalArgumentException("이미 거절된 모집 공고입니다.");
		}
		if (this == ACCEPTED) {
			throw new IllegalArgumentException("이미 수락된 모집 공고입니다.");
		}
		if (this == UNCHECKED) {
			throw new IllegalArgumentException("이미 지원한 모집 공고입니다.");
		}
	}
}
