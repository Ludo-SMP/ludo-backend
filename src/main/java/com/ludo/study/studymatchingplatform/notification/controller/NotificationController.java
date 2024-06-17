package com.ludo.study.studymatchingplatform.notification.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.notification.controller.dto.request.NotificationKeywordConfigRequest;
import com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType;
import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;
import com.ludo.study.studymatchingplatform.notification.service.NotificationService;
import com.ludo.study.studymatchingplatform.notification.service.SseEmitters;
import com.ludo.study.studymatchingplatform.notification.service.dto.request.NotificationConfigRequest;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationResponse;
import com.ludo.study.studymatchingplatform.notification.service.dto.response.config.NotificationConfigResponse;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
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

	@GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter sseConnect(@AuthUser final User user, final HttpServletResponse response) {
		SseEmitter connected = sseEmitters.connect(user);
		response.setCharacterEncoding("utf-8");
		response.addHeader("X-Accel-Buffering", "no");
		log.info("connected = {}", connected);
		return connected;
	}

	@GetMapping
	public List<NotificationResponse> readNotifications(@AuthUser final User user) {
		return notificationService.findNotifications(user);
	}

	@GetMapping("/settings")
	public NotificationConfigResponse readNotificationConfig(@AuthUser final User user) {
		return notificationService.findNotificationConfig(user);
	}

	@PostMapping("/settings")
	public ResponseEntity<Void> updateNotificationConfig(@AuthUser final User user,
														 @RequestBody final NotificationConfigRequest notificationConfigRequest
	) {
		notificationService.configGlobalNotificationUserConfig(user, notificationConfigRequest);

		return ResponseEntity.ok().build();
	}

	@PutMapping("/settings/keywords")
	public ResponseEntity<Void> updateNotificationKeywordConfig(@AuthUser final User user,
																@RequestBody final NotificationKeywordConfigRequest notificationKeywordConfigRequest
	) {
		notificationService.configNotificationKeywords(user, notificationKeywordConfigRequest);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/check")
	public ResponseEntity<Void> checkNotifications(@AuthUser final User user,
												   @RequestBody final NotificationCheckRequest notificationCheckRequest
	) {
		notificationService.checkNotificationsAsRead(user, notificationCheckRequest.notificationIds());

		return ResponseEntity.ok().build();
	}

	@PostMapping("/ping")
	public void ping(@AuthUser final User user) {
		log.info("===== ping START ===== ");
		final Study study = Study.builder().title("스터디~!").owner(user).build();
		sseEmitters.sendNotification(user, NotificationResponse.from(StudyNotification.of(
				NotificationEventType.STUDY_APPLICANT, LocalDateTime.now(), study, user)));
		log.info("===== ping END ===== ");
	}

}
