package com.ludo.study.studymatchingplatform.study.domain.study;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserReviewStatistics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // review count
    private Long totalActivenessReviewCount;
    private Long totalProfessionalismReviewCount;
    private Long totalCommunicationReviewCount;
    private Long totalTogetherReviewScore;
    private Long totalRecommendReviewScore;

    // scores
    private Long totalActivenessScore;
    private Long totalProfessionalismScore;
    private Long totalCommunicationScore;
    private Long totalTogetherScore;
    private Long totalRecommendScore;


}
