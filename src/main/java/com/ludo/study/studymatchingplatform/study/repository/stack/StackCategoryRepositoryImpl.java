package com.ludo.study.studymatchingplatform.study.repository.stack;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StackCategoryRepositoryImpl {

	private final StackCategoryJpaRepository stackCategoryJpaRepository;

}
