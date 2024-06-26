package com.ludo.study.studymatchingplatform.notification.repository.keyword;

import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordPosition.*;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationKeywordPositionRepositoryImpl {

	private final JPAQueryFactory q;
	private final NotificationKeywordPositionJpaRepository notificationKeywordPositionJpaRepository;
	private final EntityManager em;

	public NotificationKeywordPosition save(final NotificationKeywordPosition notificationKeywordPosition) {
		return notificationKeywordPositionJpaRepository.save(notificationKeywordPosition);
	}

	public List<NotificationKeywordPosition> saveAll(
			final Set<NotificationKeywordPosition> notificationKeywordPosition
	) {
		return notificationKeywordPositionJpaRepository.saveAll(notificationKeywordPosition);
	}

	public List<NotificationKeywordPosition> findByUserId(final Long userId) {
		return q.selectFrom(notificationKeywordPosition)
				.where(notificationKeywordPosition.user.id.eq(userId))
				.fetch();
	}

	public void deleteByUserId(final Long userId) {
		q.delete(notificationKeywordPosition)
				.where(notificationKeywordPosition.user.id.eq(userId))
				.execute();
		em.flush();
		em.clear();
	}

	public void deleteByIn(final Set<NotificationKeywordPosition> keywordPositions) {
		q.delete(notificationKeywordPosition)
				.where(notificationKeywordPosition.in(keywordPositions))
				.execute();
		em.flush();
		em.clear();
	}

}
