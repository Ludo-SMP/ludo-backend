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

@Component
@RequiredArgsConstructor
@Slf4j
public final class AuthUserResolver implements HandlerMethodArgumentResolver {

	private final UserRepositoryImpl userRepository;

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		final boolean hasAuthUserAnnotation = parameter.getParameterAnnotation(AuthUser.class) != null;
		final boolean isUser = User.class.isAssignableFrom(parameter.getParameterType());
		return hasAuthUserAnnotation && isUser;
	}

	@Override
	public User resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
								final NativeWebRequest webRequest,
								final WebDataBinderFactory binderFactory) {
		final HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		final AuthUserPayload payload = (AuthUserPayload)request.getAttribute(
				JwtAuthenticationFilter.AUTH_USER_PAYLOAD);
		log.info("UserRepositoryImpl.findById start");
		User userById = userRepository.getById(payload.getId());
		log.info("UserRepositoryImpl.findById end");
		return userById;
	}
}
