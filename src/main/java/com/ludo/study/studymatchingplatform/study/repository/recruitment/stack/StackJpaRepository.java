package com.ludo.study.studymatchingplatform.study.repository.recruitment.stack;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;

public interface StackJpaRepository extends JpaRepository<Stack, Long> {
	Set<Stack> findByIdIn(Set<Long> stackIds);
}
