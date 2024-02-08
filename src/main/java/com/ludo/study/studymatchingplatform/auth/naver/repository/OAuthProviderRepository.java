package com.ludo.study.studymatchingplatform.auth.naver.repository;

import java.util.Map;

import com.ludo.study.studymatchingplatform.auth.naver.service.naver.vo.OAuthProvider;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class OAuthProviderRepository {

	private final Map<String, OAuthProvider> providers;

	public OAuthProvider findByName(final String name) {
		return providers.get(name);
	}

}
