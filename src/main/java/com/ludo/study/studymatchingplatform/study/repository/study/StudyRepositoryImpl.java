package com.ludo.study.studymatchingplatform.study.repository.study;

import static com.ludo.study.studymatchingplatform.study.domain.recruitment.QRecruitment.*;
import static com.ludo.study.studymatchingplatform.study.domain.study.QStudy.*;
import static com.ludo.study.studymatchingplatform.study.domain.study.participant.QParticipant.*;
import static com.ludo.study.studymatchingplatform.user.domain.user.QUser.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.ludo.study.studymatchingplatform.study.domain.id.ParticipantId;
import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl {

    private final JPAQueryFactory q;

    private final StudyJpaRepository studyJpaRepository;

    public Optional<Study> findById(final Long studyId) {
        return studyJpaRepository.findById(studyId);
    }

    public Optional<Study> findByIdWithParticipants(final Long studyId) {
        return Optional.ofNullable(
                q.selectFrom(study)
                        .where(study.id.eq(studyId))
                        .leftJoin(study.participants, participant).fetchJoin()
                        .join(study.owner, user).fetchJoin()
                        .fetchFirst()
        );
    }

    public Optional<Study> findByIdWithRecruitment(final Long id) {
        return Optional.ofNullable(
                q.selectFrom(study)
                        .where(study.id.eq(id))
                        .leftJoin(study.recruitment, recruitment).fetchJoin()
                        // .leftJoin(recruitment.recruitmentStacks, recruitmentStack).fetchJoin()
                        // .leftJoin(recruitment.recruitmentPositions, recruitmentPosition).fetchJoin()
                        .join(study.owner, user).fetchJoin()
                        .fetchFirst()
        );
    }

    public boolean hasRecruitment(final Long id) {
        final Long recruitmentId = q.select(study.recruitment.id)
                .from(study)
                .where(study.id.eq(id))
                .fetchOne();

        return recruitmentId != null;
    }

    public Study save(final Study study) {
        return studyJpaRepository.save(study);
    }

    // TODO:
    // temp
    public List<Study> findByParticipantsId(final List<ParticipantId> participantsId) {
        return q.selectFrom(study)
                .where(participant.id.in(participantsId)
                        .and(participant.deletedDateTime.isNotNull()))
                .fetch();
    }
    // 1. 내가 가입한 스터디 + 나랑 상대 중 한 명이라도 리뷰를 쓴 경우 모두 가져옴

    // -> 최근 기준? -> 스터디 상관 없이 최근 순으로만 정렬
    //

    // -> 최근 + 스터디 기준 -> 최근 리뷰 순으로 정렬한다음에 그거에 붙여서
    // 스터디1 리뷰가 최근 -> 3번째, 7번쨰
    // 스터디2 리뷰가 2번째 -> 4번째 5번쨰
    // 스터디3 리뷰가 6번째 -> 8번쨰

    // 1,3,7 -> 2,4,5 -> 6,8
}
