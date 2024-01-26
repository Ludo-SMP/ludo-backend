package com.ludo.study.studymatchingplatform.study.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepository;

public interface UserJpaRepository extends UserRepository, JpaRepository<User, Long> {

}
