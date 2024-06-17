package com.ludo.study.studymatchingplatform.auth.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.auth.common.redis.UserDetails;
import com.ludo.study.studymatchingplatform.auth.common.util.WebUtils;
import com.ludo.study.studymatchingplatform.auth.repository.UserDetailsRepository;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsService {

	@Value("${jwt.token.refresh-token-expiresin}")
	private String refreshExpiresIn;

	private final UserDetailsRepository userDetailsRepository;

	public void createUserDetails(final User user, final HttpServletRequest request) {
		log.info("UserDetailsService.createUserDetails start");
		final String userAgent = WebUtils.getUserAgent(request);
		final String clientIp = WebUtils.getClientIp(request);
		final UserDetails userDetails =
				new UserDetails(user.getId(), userAgent, clientIp, Long.parseLong(refreshExpiresIn));
		userDetailsRepository.save(userDetails);
	}

	public void verifyUserDetails(final Long userId, final HttpServletRequest request) {
		log.info("UserDetailsService.verifyUserDetails start");
		final String userAgent = WebUtils.getUserAgent(request);
		final String clientIp = WebUtils.getClientIp(request);
		final UserDetails userDetails = userDetailsRepository.findById(userId)
				.orElseThrow(() -> new SocialAccountNotFoundException("만료된 사용자 정보 입니다."));
		if (!userDetails.getUserAgent().equals(userAgent) || !userDetails.getClientIp().equals(clientIp)) {
			throw new AuthenticationException("검증되지 않은 사용자 입니다.");
		}
		log.info("UserDetailsService.verifyUserDetails end");
	}

}
