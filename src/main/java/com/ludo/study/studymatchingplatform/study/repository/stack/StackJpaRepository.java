package com.ludo.study.studymatchingplatform.study.repository.stack;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;

public interface StackJpaRepository extends JpaRepository<Stack, Long>, StackRepository {

}
