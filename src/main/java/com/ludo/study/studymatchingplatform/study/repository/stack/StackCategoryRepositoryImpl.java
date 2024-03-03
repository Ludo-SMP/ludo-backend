package com.ludo.study.studymatchingplatform.study.repository.stack;

import static com.ludo.study.studymatchingplatform.study.domain.stack.QStackCategory.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StackCategoryRepositoryImpl {

	private final JPAQueryFactory q;
	private final StackCategoryJpaRepository stackCategoryJpaRepository;

	public StackCategory save(final StackCategory stackCategory) {
		return stackCategoryJpaRepository.save(stackCategory);
	}

	public List<StackCategory> findAll() {
		return q.selectFrom(stackCategory)
				.where(stackCategory.name.in("프론트엔드", "백엔드", "디자이너", "데이터베이스", "데브옵스", "언어"))
				.fetch();
	}

}
