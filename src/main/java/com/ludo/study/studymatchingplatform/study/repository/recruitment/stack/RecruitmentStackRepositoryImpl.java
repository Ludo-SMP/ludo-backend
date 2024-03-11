package com.ludo.study.studymatchingplatform.study.repository.recruitment.stack;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.QRecruitmentStack.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.id.RecruitmentStackId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentStackRepositoryImpl {

	private final JPAQueryFactory q;

	private final RecruitmentStackJpaRepository recruitmentStackJpaRepository;

	public List<RecruitmentStack> saveAll(final List<RecruitmentStack> recruitmentStacks) {
		return recruitmentStackJpaRepository.saveAll(recruitmentStacks);
	}

	public void deleteById(final RecruitmentStackId id) {
		q.update(recruitmentStack)
				.set(recruitmentStack.deletedDateTime, LocalDateTime.now())
				.where(recruitmentStack.id.eq(id))
				.execute();
	}

	public void save(final RecruitmentStack recruitmentStack) {
		recruitmentStackJpaRepository.save(recruitmentStack);
	}
}
