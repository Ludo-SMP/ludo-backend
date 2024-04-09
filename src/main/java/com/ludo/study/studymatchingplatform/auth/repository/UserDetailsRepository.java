package com.ludo.study.studymatchingplatform.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.ludo.study.studymatchingplatform.auth.common.redis.UserDetails;

public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {

}
