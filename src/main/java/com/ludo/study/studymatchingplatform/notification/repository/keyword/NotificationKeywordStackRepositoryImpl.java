package com.ludo.study.studymatchingplatform.notification.repository.keyword;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationKeywordStackRepositoryImpl {

	private final NotificationKeywordStackJpaRepository notificationKeywordStackJpaRepository;

	public NotificationKeywordStack save(final NotificationKeywordStack notificationKeywordStack) {
		return notificationKeywordStackJpaRepository.save(notificationKeywordStack);
	}

	public List<NotificationKeywordStack> saveAll(final List<NotificationKeywordStack> notificationKeywordStacks) {
		return notificationKeywordStackJpaRepository.saveAll(notificationKeywordStacks);
	}

}
