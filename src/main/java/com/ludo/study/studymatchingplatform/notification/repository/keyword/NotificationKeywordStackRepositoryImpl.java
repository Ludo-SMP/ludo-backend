package com.ludo.study.studymatchingplatform.notification.repository.keyword;

import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordStack.*;

import java.util.List;
import java.util.Set;

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

	public List<NotificationKeywordStack> saveAll(final Set<NotificationKeywordStack> notificationKeywordStacks) {
		return notificationKeywordStackJpaRepository.saveAll(notificationKeywordStacks);
	}

	public List<NotificationKeywordStack> findByUserId(final Long userId) {
		return q.selectFrom(notificationKeywordStack)
				.where(notificationKeywordStack.user.id.eq(userId))
				.fetch();
	}

	public void deleteByUserId(final Long userId) {
		q.delete(notificationKeywordStack)
				.where(notificationKeywordStack.user.id.eq(userId))
				.execute();
		em.flush();
		em.clear();
	}

	public void deleteByIn(final Set<NotificationKeywordStack> keywordStacks) {
		q.delete(notificationKeywordStack)
				.where(notificationKeywordStack.in(keywordStacks))
				.execute();
		em.flush();
		em.clear();
	}

}
