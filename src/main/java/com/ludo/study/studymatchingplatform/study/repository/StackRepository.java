package com.ludo.study.studymatchingplatform.study.repository;

import java.util.List;
import java.util.Set;

import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;

public interface StackRepository {
	Set<Stack> findByIdIn(List<Long> ids);
	// Optional<Stack> findById(long id);
	//
	// Set<Stack> findAllByNameIn(List<String> names);
	//
	// Set<Stack> findManyByIdIn(Set<Long> ids);
}
