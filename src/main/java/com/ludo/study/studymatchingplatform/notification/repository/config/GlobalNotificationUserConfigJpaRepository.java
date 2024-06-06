package com.ludo.study.studymatchingplatform.notification.repository.config;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;

public interface GlobalNotificationUserConfigJpaRepository extends JpaRepository<GlobalNotificationUserConfig, Long> {

}
