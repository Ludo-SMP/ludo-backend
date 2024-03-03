package com.ludo.study.studymatchingplatform.study.repository;

import static com.ludo.study.studymatchingplatform.study.domain.QCategory.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl {

	private final CategoryJpaRepository categoryJpaRepository;
	private final JPAQueryFactory q;

	public Category save(Category category) {
		categoryJpaRepository.save(category);
		return category;
	}

	public Optional<Category> findById(final Long categoryId) {
		return categoryJpaRepository.findById(categoryId);
	}

	public Optional<Category> findByName(final String name) {
		return Optional.ofNullable(
				q.select(category)
						.from(category)
						.where(category.name.eq(name))
						.fetchOne());
	}

}
