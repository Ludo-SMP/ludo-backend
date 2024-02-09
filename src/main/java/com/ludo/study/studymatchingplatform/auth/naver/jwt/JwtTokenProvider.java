package com.ludo.study.studymatchingplatform.auth.naver.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${jwt.token.secret-key}")
	private String secretKey;

	@Value("${jwt.token.access-token-expiresin}")
	private String accessTokenExpiresIn;

	public String createAccessToken(final String payload) {
		final Claims claims = createClaims(payload);
		final Date expiresIn = createExpiresIn();
		final Key signingKey = createSigningKey();

		return Jwts.builder()
			.setClaims(claims)
			.setExpiration(expiresIn)
			.signWith(signingKey)
			.compact();
	}

	private Claims createClaims(final String payload) {
		return Jwts.claims()
			.setSubject(payload);
	}

	private Date createExpiresIn() {
		long currentDateTime = new Date().getTime();
		return new Date(currentDateTime + Long.parseLong(accessTokenExpiresIn));
	}

	private Key createSigningKey() {
		byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(secretKeyBytes);
	}

	public String getAccessTokenExpiresIn() {
		return accessTokenExpiresIn;
	}
}
