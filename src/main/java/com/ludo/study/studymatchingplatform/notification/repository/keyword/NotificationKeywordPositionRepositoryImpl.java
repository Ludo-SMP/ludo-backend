package com.ludo.study.studymatchingplatform.notification.repository.keyword;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationKeywordPositionRepositoryImpl {

	private final NotificationKeywordPositionJpaRepository notificationKeywordPositionJpaRepository;

	public NotificationKeywordPosition save(final NotificationKeywordPosition notificationKeywordPosition) {
		return notificationKeywordPositionJpaRepository.save(notificationKeywordPosition);
	}

	public List<NotificationKeywordPosition> saveAll(
			final List<NotificationKeywordPosition> notificationKeywordPosition
	) {
		return notificationKeywordPositionJpaRepository.saveAll(notificationKeywordPosition);
	}

}
