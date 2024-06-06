package com.ludo.study.studymatchingplatform.notification.repository.keyword;

import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordCategory.*;
import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordStack.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationKeywordStackRepositoryImpl {

	private final JPAQueryFactory q;
	private final NotificationKeywordStackJpaRepository notificationKeywordStackJpaRepository;
	private final EntityManager em;

	public NotificationKeywordStack save(final NotificationKeywordStack notificationKeywordStack) {
		return notificationKeywordStackJpaRepository.save(notificationKeywordStack);
	}

	public List<NotificationKeywordStack> saveAll(final List<NotificationKeywordStack> notificationKeywordStacks) {
		return notificationKeywordStackJpaRepository.saveAll(notificationKeywordStacks);
	}

	public List<NotificationKeywordStack> findByUserId(final Long userId) {
		return q.selectFrom(notificationKeywordStack)
				.where(notificationKeywordStack.user.id.eq(userId))
				.fetch();
	}

	public void deleteByUserId(final Long userId) {
		q.delete(notificationKeywordCategory)
				.where(notificationKeywordCategory.id.eq(userId))
				.execute();
		em.flush();
		em.clear();
	}

}
