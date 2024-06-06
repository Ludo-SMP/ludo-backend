package com.ludo.study.studymatchingplatform.notification.repository.notification;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.QReviewNotification.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewNotificationRepositoryImpl {

	private JPAQueryFactory q;
	private final ReviewNotificationJpaRepository reviewNotificationJpaRepository;

	public ReviewNotification save(final ReviewNotification reviewNotifications) {
		return reviewNotificationJpaRepository.save(reviewNotifications);
	}

	public List<ReviewNotification> saveAll(final List<ReviewNotification> reviewNotifications) {
		return reviewNotificationJpaRepository.saveAll(reviewNotifications);
	}

	public List<ReviewNotification> findAllByUserId(final Long userId) {
		return q.selectFrom(reviewNotification)
				.where(reviewNotification.notifier.id.eq(userId))
				.fetch();
	}

}
