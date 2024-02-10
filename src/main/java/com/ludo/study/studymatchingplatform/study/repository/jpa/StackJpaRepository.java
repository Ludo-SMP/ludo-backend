package com.ludo.study.studymatchingplatform.study.repository.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.repository.StackRepository;

public interface StackJpaRepository extends StackRepository, JpaRepository<Stack, Long> {
	Set<Stack> findByIdIn(List<Long> ids);
}
