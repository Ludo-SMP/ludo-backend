package com.ludo.study.studymatchingplatform.study.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.repository.StackCategoryRepository;

public interface StackCategoryJpaRepository extends StackCategoryRepository, JpaRepository<StackCategory, Long> {
}
