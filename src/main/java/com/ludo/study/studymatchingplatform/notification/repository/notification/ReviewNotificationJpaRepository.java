package com.ludo.study.studymatchingplatform.notification.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.notification.domain.notification.ReviewNotification;

public interface ReviewNotificationJpaRepository extends JpaRepository<ReviewNotification, Long> {

}
