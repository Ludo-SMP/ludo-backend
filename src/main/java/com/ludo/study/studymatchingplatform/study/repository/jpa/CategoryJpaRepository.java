package com.ludo.study.studymatchingplatform.study.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepository;

public interface CategoryJpaRepository extends CategoryRepository, JpaRepository<Category, Long> {
}
