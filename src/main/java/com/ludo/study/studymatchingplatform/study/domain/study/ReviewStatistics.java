package com.ludo.study.studymatchingplatform.study.domain.study;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewStatistics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // reviewStatistics count
    @Builder.Default
    private Long totalActivenessReviewCount = 0L;

    @Builder.Default
    private Long totalProfessionalismReviewCount = 0L;

    @Builder.Default
    private Long totalCommunicationReviewCount = 0L;

    @Builder.Default
    private Long totalTogetherReviewCount = 0L;

    @Builder.Default
    private Long totalRecommendReviewCount = 0L;

    // scores
    @Builder.Default
    private Long totalActivenessScore = 0L;

    @Builder.Default
    private Long totalProfessionalismScore = 0L;

    @Builder.Default
    private Long totalCommunicationScore = 0L;

    @Builder.Default
    private Long totalTogetherScore = 0L;

    @Builder.Default
    private Long totalRecommendScore = 0L;

    public static ReviewStatistics of(final User user) {
        return ReviewStatistics.builder()
                .user(user)
                .build();
    }

    // 적극성
    public Double getActivenessPercentage() {
        return calculatePercentage(totalActivenessScore, totalActivenessReviewCount);
    }

    // 전문성
    public Double getProfessionalismPercentage() {
        return calculatePercentage(totalProfessionalismScore, totalProfessionalismReviewCount);
    }

    // 의사소통
    public Double getCommunicationPercentage() {
        return calculatePercentage(totalCommunicationScore, totalCommunicationReviewCount);
    }

    public Double getTogetherPercentage() {
        return calculatePercentage(totalTogetherScore, totalTogetherReviewCount);
    }

    public Double getRecommendPercentage() {
        return calculatePercentage(totalRecommendScore, totalRecommendReviewCount);
    }

    private Double calculatePercentage(final Long totalScore, final Long reviewCount) {
        if (reviewCount == 0) {
            return 0.0;
        }
        return (totalScore / (reviewCount * 5.0)) * 100;
    }

    public void update(final Review review) {
        totalActivenessScore += review.getActivenessScore();
        totalRecommendScore += review.getRecommendScore();
        totalCommunicationScore += review.getCommunicationScore();
        totalProfessionalismScore += review.getProfessionalismScore();
        totalTogetherScore += review.getTogetherScore();

        totalActivenessReviewCount++;
        totalRecommendReviewCount++;
        totalCommunicationReviewCount++;
        totalProfessionalismReviewCount++;
        totalTogetherReviewCount++;
    }
}
