package com.ludo.study.studymatchingplatform.study.service.study;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.service.NotificationService;
import com.ludo.study.studymatchingplatform.common.exception.DataConflictException;
import com.ludo.study.studymatchingplatform.common.exception.DataNotFoundException;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteReviewRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.WriteReviewResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.PeerReviewsResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewRepositoryImpl reviewRepository;
    private final StudyRepositoryImpl studyRepository;
    private final ReviewService reviewService;
    private final UserRepositoryImpl userRepository;
    private final ReviewStatisticsService reviewStatisticsService;

    private final NotificationService notificationService;

    public List<PeerReviewsResponse> getPeerReviews(final Long studyId, final Long selfId) {
        userRepository.findById(selfId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보 오류입니다. 리뷰를 작성할 수 없습니다."));
        final Study study = studyRepository.findByIdWithParticipants(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. 리뷰를 조회할 수 없습니다."));
        final List<Long> peerIds = study.getParticipants().stream()
                .map(p -> p.getUser().getId())
                .toList();

        final List<Review> selfReviews = reviewRepository.findSelfReviews(studyId, selfId, peerIds);
        final List<Review> peerReviews = reviewRepository.findPeerReviews(studyId, selfId, peerIds);

        return PeerReviewsResponse.listFrom(selfReviews, peerReviews);
    }

    public WriteReviewResponse write(final WriteReviewRequest request, final Long studyId, final User reviewer) {
        final Study study = studyRepository.findByIdWithParticipants(studyId)
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 스터디입니다. 리뷰를 작성할 수 없습니다."));

        final boolean existsReview = reviewRepository.exists(studyId, reviewer.getId(), request.revieweeId());
        if (existsReview) {
            throw new DataConflictException("이미 리뷰를 작성 하셨습니다.");
        }

        final Review review = reviewRepository.save(reviewService.write(request, study, reviewer));
        reviewStatisticsService.updateRevieweeStatistics(review);

        // 리뷰 알림
        notificationService.reviewReceiveNotice(review);
        notificationService.reviewPeerFinishNotice(study, review);

        return WriteReviewResponse.from(review);
    }

}
