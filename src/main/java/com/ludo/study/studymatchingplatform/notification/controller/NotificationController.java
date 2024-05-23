package com.ludo.study.studymatchingplatform.notification.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.notification.service.NotificationService;
import com.ludo.study.studymatchingplatform.notification.service.SseEmitters;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {

	private final NotificationService notificationService;
	private final SseEmitters sseEmitters;

	/**
	 * SSE 구독 로직을 컨트롤러에 둔 이유
	 * 1. 패키지 의존 관계: SseEmitter는 web mvc 라이브러리
	 * 2. 클라이언트 응답 방식이 바뀌어도, 서비스는 영향을 받지 않게 하기 위함
	 */
	@GetMapping(value = "/subscribe")
	public SseEmitter sseConnect(@AuthUser final User user, final HttpServletResponse response) {
		setSseResponseHeaders(response);
		SseEmitter connected = sseEmitters.connect(user);
		log.info("connected = {}", connected);
		return connected;
	}

	private void setSseResponseHeaders(final HttpServletResponse response) {
		response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
		response.setCharacterEncoding("utf-8");
	}

	@GetMapping
	public List<NotificationResponse> readNotifications(@AuthUser final User user) {
		return notificationService.findNotifications(user);
	}

}
