package com.ludo.study.studymatchingplatform.auth.common.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash(value = "user_details")
@ToString
public class UserDetails {

	@Id
	private Long userId;

	private String userAgent;

	private String clientIp;

	@TimeToLive
	private Long ttl;

	public void update(final Long ttl) {
		this.ttl = ttl;
	}

}
