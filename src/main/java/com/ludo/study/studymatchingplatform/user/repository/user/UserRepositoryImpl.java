package com.ludo.study.studymatchingplatform.user.repository.user;

import static com.ludo.study.studymatchingplatform.notification.domain.config.QGlobalNotificationUserConfig.*;
import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordCategory.*;
import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordPosition.*;
import static com.ludo.study.studymatchingplatform.notification.domain.keyword.QNotificationKeywordStack.*;
import static com.ludo.study.studymatchingplatform.study.domain.study.participant.QParticipant.*;
import static com.ludo.study.studymatchingplatform.user.domain.user.QUser.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.repository.dto.RecruitmentNotifierCond;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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

	public List<User> findRecruitmentNotifiers(final RecruitmentNotifierCond condition) {
		return q.selectFrom(user)
				.join(globalNotificationUserConfig).on(globalNotificationUserConfig.user.eq(user))
				.join(notificationKeywordCategory).on(notificationKeywordCategory.user.eq(user))
				.join(notificationKeywordPosition).on(notificationKeywordPosition.user.eq(user))
				.join(notificationKeywordStack).on(notificationKeywordStack.user.eq(user))
				.where(
						globalNotificationUserConfig.recruitmentConfig.isTrue(),
						notificationKeywordCategory.category.eq(condition.category()),
						positionsOrCondition(condition.positions()),
						stacksOrCondition(condition.stacks()),
						user.id.ne(condition.owner().getId())
				)
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

	public List<User> findStudyApplicantNotifiers(final Long id) {
		return q.selectFrom(user)
				.join(globalNotificationUserConfig).on(globalNotificationUserConfig.user.eq(user))
				.join(participant).on(participant.user.eq(user))
				.from(participant)
				.where(
						globalNotificationUserConfig.studyApplicantConfig.isTrue(),
						participant.study.id.eq(id))
				.fetch();
	}

	public List<User> findParticipantLeaveNotifiers(final Long id) {
		return q.selectFrom(user)
				.join(globalNotificationUserConfig).on(globalNotificationUserConfig.user.eq(user))
				.join(participant.user)
				.from(participant.study)
				.where(
						globalNotificationUserConfig.studyParticipantLeaveConfig.isTrue(),
						participant.study.id.eq(id))
				.fetch();
	}

}
