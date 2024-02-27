package com.ludo.study.studymatchingplatform.study.repository.stack;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitmentStack.*;
import static com.ludo.study.studymatchingplatform.study.domain.stack.QStack.*;
import static com.ludo.study.studymatchingplatform.study.domain.stack.QStackCategory.*;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
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
