package com.ludo.study.studymatchingplatform.notification.repository.notification;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.QNotification.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.notification.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl {

	private final JPAQueryFactory q;
	private final NotificationJpaRepository notificationJpaRepository;
	private final EntityManager em;

	public void updateNotificationsAsRead(final Long userId, final List<Long> notificationIds) {
		q.update(notification)
				.set(notification.read, true)
				.where(notification.notifier.id.eq(userId),
						notification.id.in(notificationIds))
				.execute();

		em.flush();
		em.clear();
	}

	public List<Notification> saveAll(final List<Notification> notifications) {
		return notificationJpaRepository.saveAll(notifications);
	}

	public List<Notification> findAllByUserId(final Long userId) {
		return q.selectFrom(notification)
				.where(notification.notifier.id.eq(userId))
				.fetch();
	}

}
