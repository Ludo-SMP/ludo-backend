package com.ludo.study.studymatchingplatform.user.repository.user;

import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.jpa.UserJpaRepositoryImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ludo.study.studymatchingplatform.user.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {

    private final JPAQueryFactory q;
    private final JPAQueryFactory queryFactory;
    private final UserJpaRepositoryImpl userJpaRepository;

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
                queryFactory.select(user)
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

}
