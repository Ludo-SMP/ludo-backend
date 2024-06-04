package com.ludo.study.studymatchingplatform.user.repository.user;

import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordCategory.*;
import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordPosition.*;
import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordStack.*;
import static com.ludo.study.studymatchingplatform.study.domain.study.participant.QParticipant.*;
import static com.querydsl.jpa.JPAExpressions.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.repository.dto.RecruitmentNotifierCondition;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.ludo.study.studymatchingplatform.user.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {

	private final JPAQueryFactory q;
	private final UserJpaRepository userJpaRepository;

	public User save(final User user) {
		return userJpaRepository.save(user);
	}

	/**
	 * may need to add platformId into table
	 */
	public boolean existsByEmailForGoogle(final String email) {
		final Long id = q.select(user.id)
				.from(user)
				.where(
						user.social.eq(Social.GOOGLE),
						user.email.eq(email)
				)
				.fetchFirst();

		return id != null;
	}

    public Optional<User> findByEmail(final String email) {
        return findByEmail(email, false);
    }

    public Optional<User> findByEmail(final String email, final boolean includeDeleted) {
        return Optional.ofNullable(
                q.select(user)
                        .from(user)
                        .where(user.email.eq(email)
                                .and(deletedDateTimeCondition(includeDeleted)))
                        .fetchOne());
    }

    public BooleanExpression deletedDateTimeCondition(final boolean includeDeleted) {
        return includeDeleted ? null : user.deletedDateTime.isNull();
    }

    public Optional<User> findById(final Long id) {
        return userJpaRepository.findById(id);
    }

	public User getById(final Long id) {
		return userJpaRepository.findById(id)
				.orElseThrow(() -> new AuthenticationException("로그인 되지 않은 사용자입니다."));
	}

	public boolean existsByNickname(final String nickname) {
		Long userId = q.select(user.id)
				.from(user)
				.where(user.nickname.eq(nickname))
				.limit(1L)
				.fetchOne();

		return userId != null;
	}

	public List<User> findRecruitmentNotifiers(final RecruitmentNotifierCondition condition) {
		// TODO: 모집공고 알림 설정을 on한 사용자 쿼리 조건 추가
		// TODO: 내가 생성한 모집공고는 알림대상자에 제외하는 쿼리 조건 추가
		return q.select(user)
				.from(user)
				.where(
						user.id.in(
								select(notificationKeywordCategory.user.id)
										.from(notificationKeywordCategory)
										.where(notificationKeywordCategory.category.eq(condition.category()))))
				.where(
						user.id.in(
								select(notificationKeywordPosition.user.id)
										.from(notificationKeywordPosition)
										.where(positionsOrCondition(condition.positions()))))
				.where(
						user.id.in(
								select(notificationKeywordStack.user.id)
										.from(notificationKeywordStack)
										.where(stacksOrCondition(condition.stacks()))))
				.fetch();

	}

	private BooleanBuilder positionsOrCondition(List<Position> positions) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		positions.stream()
				.map(notificationKeywordPosition.position::eq)
				.forEach(booleanBuilder::or);

		return booleanBuilder;
	}

	private BooleanBuilder stacksOrCondition(List<Stack> stacks) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		stacks.stream()
				.map(notificationKeywordStack.stack::eq)
				.forEach(booleanBuilder::or);
		return booleanBuilder;
	}

	public List<User> findParticipantUsersByStudyId(final Long id) {
		// TODO: 모집공고 알림 설정을 on한 사용자 쿼리 조건 추가
		return q.select(user)
				.from(participant)
				.where(participant.study.id.eq(id))
				.fetch();
	}

}
