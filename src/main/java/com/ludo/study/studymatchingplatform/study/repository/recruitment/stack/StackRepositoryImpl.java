package com.ludo.study.studymatchingplatform.study.repository.recruitment.stack;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.QRecruitmentStack.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.QStack.*;
import static com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.QStackCategory.*;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StackRepositoryImpl {

	private final JPAQueryFactory q;

	private final StackJpaRepository stackJpaRepository;

	public Set<Stack> findByIdIn(final Set<Long> stackIds) {

		return stackJpaRepository.findByIdIn(stackIds);
	}

	public List<Stack> findAll() {
		return q.selectFrom(stack)
				.innerJoin(stack.stackCategory, stackCategory)
				.where(stackCategory.name.in("프론트엔드", "백엔드", "데이터베이스", "데브옵스", "언어", "디자인"))
				.fetch();
	}

	public List<Stack> findAllByIds(final Set<Long> stackIds) {
		return q.selectFrom(stack)
				.where(stack.id.in(stackIds))
				.leftJoin(stack.stackCategory, stackCategory).fetchJoin()
				.leftJoin(stack.recruitmentStacks, recruitmentStack).fetchJoin()
				.fetch();
	}

	public Stack save(final Stack stack) {
		return stackJpaRepository.save(stack);
	}

}
