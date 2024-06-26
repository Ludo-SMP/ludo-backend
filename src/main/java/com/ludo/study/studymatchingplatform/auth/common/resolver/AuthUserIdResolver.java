package com.ludo.study.studymatchingplatform.auth.common.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.filter.JwtAuthenticationFilter;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * handler에서 `@AuthUser` annotation과 함께 사용 시, Type이 `User`인 경우 `User` 객체를 추출하며,
 * `Long`이면 `Long` 타입의 userId만 추출합니다.
 * Servlet Engine 초입 구간인 Filter에서 가져온 JPA Entity는 Tx Scope 불일치를 초래할 수 있기 때문에 이를 방지해야 하는 경우에 좀 더 간결하게 사용할 수 있습니다.
 *
 * @see AuthUserResolver
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthUserIdResolver implements HandlerMethodArgumentResolver {

	private final UserRepositoryImpl userRepository;

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		final boolean hasAuthUserAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
		final boolean isLong = Long.class.isAssignableFrom(parameter.getParameterType());
		return hasAuthUserAnnotation && isLong;
	}

	@Override
	public Long resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
								final NativeWebRequest webRequest,
								final WebDataBinderFactory binderFactory) {
		final HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		final AuthUserPayload payload = (AuthUserPayload)request.getAttribute(
				JwtAuthenticationFilter.AUTH_USER_PAYLOAD);
		log.info("UserRepositoryImpl.findById start");
		final User user = userRepository.getById(payload.getId());
		log.info("UserRepositoryImpl.findById end");
		return user.getId();
	}
}
