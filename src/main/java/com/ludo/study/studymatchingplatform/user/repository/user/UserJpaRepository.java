package com.ludo.study.studymatchingplatform.user.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.user.domain.user.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

}
