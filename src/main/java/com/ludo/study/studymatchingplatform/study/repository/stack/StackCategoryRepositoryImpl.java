package com.ludo.study.studymatchingplatform.study.repository.stack;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StackCategoryRepositoryImpl {

	private final StackCategoryJpaRepository stackCategoryJpaRepository;

	public StackCategory save(final StackCategory stackCategory) {
		return stackCategoryJpaRepository.save(stackCategory);
	}

}
