package com.ludo.study.studymatchingplatform.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.Category;

public interface CategoryJpaRepository extends JpaRepository<Category, Long>, CategoryRepository {

}
