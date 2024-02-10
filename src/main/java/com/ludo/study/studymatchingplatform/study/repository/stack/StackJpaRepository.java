package com.ludo.study.studymatchingplatform.study.repository.stack;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;

public interface StackJpaRepository extends JpaRepository<Stack, Long> {

	Set<Stack> findByIdIn(List<Long> stackIds);

}
