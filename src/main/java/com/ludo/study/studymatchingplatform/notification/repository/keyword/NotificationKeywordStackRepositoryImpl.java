package com.ludo.study.studymatchingplatform.notification.repository.keyword;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationKeywordStackRepositoryImpl {

	private final NotificationKeywordStackJpaRepository notificationKeywordStackJpaRepository;

	public NotificationKeywordStack save(NotificationKeywordStack notificationKeywordStack) {
		return notificationKeywordStackJpaRepository.save(notificationKeywordStack);
	}

}
