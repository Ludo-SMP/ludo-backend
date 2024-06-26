package com.ludo.study.studymatchingplatform.auth.common.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.common.exception.UnauthorizedUserException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	@Value("${jwt.token.secret-key}")
	private String secretKey;

	@Getter
	@Value("${jwt.token.access-token-expiresin}")
	private String accessTokenExpiresIn;

	public String createAccessToken(final AuthUserPayload payload) {
		final Claims claims = createClaims(payload);
		return createToken(accessTokenExpiresIn, claims);
	}

	private String createToken(final String tokenValidityInSeconds, final Claims claims) {
		final Long tokenValidity = Long.parseLong(tokenValidityInSeconds);
		final Date expiresIn = createExpiresIn(tokenValidity);
		final Key signingKey = createSigningKey();
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(expiresIn)
				.signWith(signingKey)
				.compact();
	}

	private Claims createClaims(final AuthUserPayload payload) {
		log.info("create Claims start");
		final Claims claims = Jwts.claims();
		claims.put("id", payload.getId());
		log.info("create Claims end");
		return claims;
	}

	private Date createExpiresIn(final Long tokenValidityInSeconds) {
		final Long currentDateTime = new Date().getTime();
		return new Date(currentDateTime + tokenValidityInSeconds);
	}

	private Key createSigningKey() {
		byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(secretKeyBytes);
	}

	public Claims verifyAuthTokenOrThrow(final String token) {
		try {
			final Jws<Claims> claimsJws = parseToClaimsJws(token);
			return claimsJws.getBody();
		} catch (final ExpiredJwtException expiredJwtException) {
			log.info("만료된 토큰입니다. msg = {}", expiredJwtException.getMessage());
			throw new UnauthorizedUserException("Expired Token", expiredJwtException);
		} catch (final JwtException jwtException) {
			log.info("유효하지 않은 토큰입니다. msg = {}", jwtException.getMessage());
			throw new UnauthorizedUserException("Invalid Token", jwtException);
		}
	}

	private Jws<Claims> parseToClaimsJws(final String token) {
		final Key key = createSigningKey();
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
	}

}
