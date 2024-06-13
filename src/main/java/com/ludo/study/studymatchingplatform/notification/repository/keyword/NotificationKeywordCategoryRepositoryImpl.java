package com.ludo.study.studymatchingplatform.notification.repository.keyword;

import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordCategory.*;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationKeywordCategoryRepositoryImpl {

	private final JPAQueryFactory q;
	private final NotificationKeywordCategoryJpaRepository notificationKeywordCategoryJpaRepository;
	private final EntityManager em;

	public NotificationKeywordCategory save(final NotificationKeywordCategory notificationKeywordCategory) {
		return notificationKeywordCategoryJpaRepository.save(notificationKeywordCategory);
	}

	public List<NotificationKeywordCategory> saveAll(
			final Set<NotificationKeywordCategory> notificationKeywordCategories
	) {
		return notificationKeywordCategoryJpaRepository.saveAll(notificationKeywordCategories);
	}

	public List<NotificationKeywordCategory> findByUserId(final Long userId) {
		return q.selectFrom(notificationKeywordCategory)
				.where(notificationKeywordCategory.user.id.eq(userId))
				.fetch();
	}

	public void deleteByUserId(final Long userId) {
		q.delete(notificationKeywordCategory)
				.where(notificationKeywordCategory.user.id.eq(userId))
				.execute();
		em.flush();
		em.clear();
	}

	public void deleteByIn(final Set<NotificationKeywordCategory> keywordCategories) {
		q.delete(notificationKeywordCategory)
				.where(notificationKeywordCategory.in(keywordCategories))
				.execute();
		em.flush();
		em.clear();
	}

}
