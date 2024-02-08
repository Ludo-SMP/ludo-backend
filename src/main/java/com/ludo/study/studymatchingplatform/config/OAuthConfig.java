package com.ludo.study.studymatchingplatform.config;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ludo.study.studymatchingplatform.auth.naver.repository.OAuthProviderRepository;
import com.ludo.study.studymatchingplatform.auth.naver.service.naver.vo.OAuthProperties;
import com.ludo.study.studymatchingplatform.auth.naver.service.naver.vo.OAuthProvider;
import com.ludo.study.studymatchingplatform.auth.naver.service.naver.vo.OAuthProviderMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(OAuthProperties.class)
public class OAuthConfig {

	private final OAuthProperties oAuthProperties;

	public OAuthConfig(OAuthProperties oAuthProperties) {
		this.oAuthProperties = oAuthProperties;
	}

	@Bean
	public OAuthProviderRepository oAuthProviderRepository() {
		Map<String, OAuthProvider> providers = OAuthProviderMapper.mapBy(oAuthProperties);
		return new OAuthProviderRepository(providers);
	}

}