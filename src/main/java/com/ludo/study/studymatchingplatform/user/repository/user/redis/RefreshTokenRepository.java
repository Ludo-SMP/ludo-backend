package com.ludo.study.studymatchingplatform.user.repository.user.redis;

import org.springframework.data.repository.CrudRepository;

import com.ludo.study.studymatchingplatform.user.domain.user.redis.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

}
