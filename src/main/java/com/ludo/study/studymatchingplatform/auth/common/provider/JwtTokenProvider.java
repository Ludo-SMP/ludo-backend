package com.ludo.study.studymatchingplatform.auth.common.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Component
public class JwtTokenProvider {

	private static final String TYPE_CLAIM_KEY = "type";

	@Value("${jwt.token.secret-key}")
	private String secretKey;

	@Getter
	@Value("${jwt.token.access-token-expiresin}")
	private String accessTokenExpiresIn;

	public String createAccessToken(final AuthUserPayload payload) {
		final Claims claims = createClaims(payload);
		claims.put(TYPE_CLAIM_KEY, "Access");
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
		final Claims claims = Jwts.claims();
		claims.put("id", payload.getId());
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

	// public void isValidTokenOrThrows(final String token) {
	// 	verifyAuthTokenOrThrow(token);
	// }

	public Claims verifyAuthTokenOrThrow(final String token) {
		try {
			final Jws<Claims> claimsJws = parseToClaimsJws(token);
			return claimsJws.getBody();
		} catch (final ExpiredJwtException expiredJwtException) {
			throw new AuthenticationException("Expired Token", expiredJwtException);
		} catch (final JwtException jwtException) {
			throw new AuthenticationException("Invalid Token", jwtException);
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
