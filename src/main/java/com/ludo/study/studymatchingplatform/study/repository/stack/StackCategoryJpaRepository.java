package com.ludo.study.studymatchingplatform.study.repository.stack;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;

public interface StackCategoryJpaRepository extends JpaRepository<StackCategory, Long>, StackCategoryRepository {

}
