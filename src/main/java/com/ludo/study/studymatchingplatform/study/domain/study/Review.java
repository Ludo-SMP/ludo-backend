package com.ludo.study.studymatchingplatform.study.domain.study;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import static jakarta.persistence.FetchType.LAZY;

@Slf4j
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    // 적극성
    private Long activenessScore;

    // 전문성
    private Long professionalismScore;

    // 의사소통
    private Long communicationScore;

    // 다시함께
    private Long togetherScore;

    // 추천 여부
    private Long recommendScore;

    public boolean isDuplicateReview(final Long studyId, final Long reviewerId, final Long revieweeId) {
        return study.getId() == studyId && reviewer.getId() == reviewerId && reviewee.getId() == revieweeId;
    }

}