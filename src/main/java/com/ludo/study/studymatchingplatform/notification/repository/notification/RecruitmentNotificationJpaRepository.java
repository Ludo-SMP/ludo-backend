package com.ludo.study.studymatchingplatform.notification.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;

public interface RecruitmentNotificationJpaRepository extends JpaRepository<RecruitmentNotification, Long> {

}
