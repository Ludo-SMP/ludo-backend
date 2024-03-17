package com.ludo.study.studymatchingplatform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(host);
		config.setPort(port);
		return new LettuceConnectionFactory(config);
	}

	// 스프링 2.0 이후로 필요없다는 정보가 있어 검증 후 제거할 예정입니다.
	// @Bean
	// public RedisTemplate<String, Object> redisTemplate() {
	// 	RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	// 	redisTemplate.setKeySerializer(new StringRedisSerializer());
	// 	redisTemplate.setValueSerializer(new StringRedisSerializer());
	// 	redisTemplate.setConnectionFactory(redisConnectionFactory());
	//
	// 	return redisTemplate;
	// }

}
