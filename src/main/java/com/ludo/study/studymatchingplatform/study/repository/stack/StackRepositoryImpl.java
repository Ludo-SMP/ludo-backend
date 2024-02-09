package com.ludo.study.studymatchingplatform.study.repository.stack;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StackRepositoryImpl {

	private final StackJpaRepository stackJpaRepository;

}
