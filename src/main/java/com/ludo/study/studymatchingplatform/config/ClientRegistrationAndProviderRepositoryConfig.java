package com.ludo.study.studymatchingplatform.config;

import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ludo.study.studymatchingplatform.auth.config.property.ClientRegistrationAndProvider;
import com.ludo.study.studymatchingplatform.auth.config.property.ClientRegistrationAndProviderMapper;
import com.ludo.study.studymatchingplatform.auth.config.property.OAuthProperties;
import com.ludo.study.studymatchingplatform.auth.naver.repository.InMemoryClientRegistrationAndProviderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(OAuthProperties.class)
public class ClientRegistrationAndProviderRepositoryConfig {

	private final OAuthProperties oAuthProperties;

	public ClientRegistrationAndProviderRepositoryConfig(OAuthProperties oAuthProperties) {
		this.oAuthProperties = oAuthProperties;
	}

	@Bean
	public InMemoryClientRegistrationAndProviderRepository oAuthProviderRepository() {
		Map<String, ClientRegistrationAndProvider> providers
				= ClientRegistrationAndProviderMapper.mapBy(oAuthProperties);
		return new InMemoryClientRegistrationAndProviderRepository(providers);
	}

}
