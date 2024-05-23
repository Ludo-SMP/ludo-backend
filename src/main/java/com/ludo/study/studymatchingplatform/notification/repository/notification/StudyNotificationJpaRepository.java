package com.ludo.study.studymatchingplatform.notification.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.notification.domain.notification.StudyNotification;

public interface StudyNotificationJpaRepository extends JpaRepository<StudyNotification, Long> {

}
