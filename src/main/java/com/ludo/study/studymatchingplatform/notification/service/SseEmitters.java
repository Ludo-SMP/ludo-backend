package com.ludo.study.studymatchingplatform.notification.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ludo.study.studymatchingplatform.notification.service.dto.response.NotificationResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SseEmitters {
	private static final Long DEFAULT_TIME_OUT = 30 * 60 * 1000L;
	private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

	public SseEmitter connect(final User user) {
		final Long emitterId = createSseEmitterId(user);
		final SseEmitter emitter = new SseEmitter(DEFAULT_TIME_OUT);

		registerActionsOnCompletion(emitter);
		registerActionsOnTimeOut(emitter, emitterId);
		registerActionsOnError(emitter, emitterId);

		saveSseEmitter(emitter, emitterId);
		send(emitter, "publish", "SSE publish success");

		return emitter;
	}

	public void sendNotification(final User notifier, final NotificationResponse response) {
		log.info("================= sendNotification =================");
		final SseEmitter sseEmitter = getSseEmitter(createSseEmitterId(notifier));
		// TODO:
		// sseEmitter가 존재하는 경우에만 알림 발송
		if (sseEmitter != null) {
			log.info("사용자 = {} 에 대한 SseEmitter = {} 가 존재합니다.", notifier, sseEmitter);
			send(sseEmitter, "notification", response);
		}
	}

	private void send(final SseEmitter sseEmitter, final String eventName, final Object eventData) {
		try {
			log.info("================= Sse Emitter Send Data 시작 =================");
			sseEmitter.send(SseEmitter.event()
					.name(eventName)
					.data(eventData));
			log.info("================= Sse Emitter Send Data 종료 =================");
		} catch (IOException e) {
			sseEmitter.completeWithError(e);
		}
	}

	private Long createSseEmitterId(final User user) {
		final Long sseEmitterId = user.getId();
		return sseEmitterId;
	}

	private void registerActionsOnCompletion(final SseEmitter emitter) {
		emitter.onCompletion(() -> {
			log.info("[SSE onCompletion] server sent event onCompletion");
			emitter.complete();
		});
	}

	private void registerActionsOnTimeOut(final SseEmitter emitter, final Long sseEmitterId) {
		emitter.onTimeout(() -> {
			log.info("[SSE onTimeout] SSE 시간초과: emitter={}, sseEmitterId={}", emitter, sseEmitterId);
			removeSseEmitter(sseEmitterId);
		});
	}

	private void registerActionsOnError(final SseEmitter emitter, final Long sseEmitterId) {
		emitter.onError(exception -> {
			log.info("[SSE OnError] SSE 에러발생: sseEmitter={}, sseEmitterId={}, errorMsg={}",
					emitter, sseEmitterId, exception.getMessage());
			removeSseEmitter(sseEmitterId);
		});
	}

	private void saveSseEmitter(final SseEmitter emitter, final Long sseEmitterId) {
		this.sseEmitters.put(sseEmitterId, emitter);
		log.info("[SSE Save] new sseEmitter added: {}, emitter list size: {}", emitter, sseEmitters.size());
	}

	private void removeSseEmitter(final Long sseEmitterId) {
		if (this.sseEmitters.containsKey(sseEmitterId)) {
			SseEmitter remove = this.sseEmitters.remove(sseEmitterId);
			log.info("[SSE Remove] sseEmitter 삭제 완료: sseEmitter={}, sseEmitterId={}", remove, sseEmitterId);
		}
	}

	private SseEmitter getSseEmitter(final Long sseEmitterId) {
		// TODO:
		// 존재하지 않는 경우 null 반환
		if (this.sseEmitters.containsKey(sseEmitterId)) {
			SseEmitter sseEmitter = sseEmitters.get(sseEmitterId);
			log.info("sseEmitter={}, sseEmitterId={}", sseEmitter, sseEmitterId);
			return sseEmitter;
		}
		log.info("sseEmitterId={} 에 해당하는 SseEmitter 가 없습니다.", sseEmitterId);
		return null;
		//		throw new NotExistSseEmitterException(
		//				String.format("id = %d 에 해당하는 SseEmitter가 존재하지 않습니다.", sseEmitterId));
	}

	public SseEmitter findSseEmitter(final User user) {
		return sseEmitters.get(user.getId());
	}

}
