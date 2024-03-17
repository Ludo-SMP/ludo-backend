package com.ludo.study.studymatchingplatform.user.domain.user.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "refresh_token")
public class RefreshToken {

	@Id
	private Long userId;

	@Indexed
	private String token;

	@TimeToLive
	private Long ttl;

	public void update(final String token, final Long ttl) {
		this.token = token;
		this.ttl = ttl;
	}

}
