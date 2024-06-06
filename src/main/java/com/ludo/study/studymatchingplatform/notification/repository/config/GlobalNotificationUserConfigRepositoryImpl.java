package com.ludo.study.studymatchingplatform.notification.repository.config;

import static com.ludo.study.studymatchingplatform.notification.domain.config.QGlobalNotificationUserConfig.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GlobalNotificationUserConfigRepositoryImpl {

	private final JPAQueryFactory q;

	private final GlobalNotificationUserConfigJpaRepository globalNotificationUserConfigJpaRepository;

	public GlobalNotificationUserConfig save(final GlobalNotificationUserConfig globalNotificationUserConfig) {
		return globalNotificationUserConfigJpaRepository.save(globalNotificationUserConfig);
	}

	public Optional<GlobalNotificationUserConfig> findByUserId(final Long userId) {
		return Optional.ofNullable(
				q.selectFrom(globalNotificationUserConfig)
						.where(globalNotificationUserConfig.user.id.eq(userId))
						.fetchOne()
		);
	}
}
