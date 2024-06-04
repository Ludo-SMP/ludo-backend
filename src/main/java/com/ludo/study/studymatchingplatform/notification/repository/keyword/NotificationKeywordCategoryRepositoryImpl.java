package com.ludo.study.studymatchingplatform.notification.repository.keyword;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationKeywordCategoryRepositoryImpl {

	private final NotificationKeywordCategoryJpaRepository notificationKeywordCategoryJpaRepository;

	public NotificationKeywordCategory save(final NotificationKeywordCategory notificationKeywordCategory) {
		return notificationKeywordCategoryJpaRepository.save(notificationKeywordCategory);
	}

}
