package com.ludo.study.studymatchingplatform.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.user.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

}
