package com.ludo.study.studymatchingplatform.user.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.user.domain.User;

public interface UserJpaRepositoryImpl extends JpaRepository<User, Long> {

}
