package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteReviewRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.PeerReviewsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.WriteReviewResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewRepositoryImpl reviewRepository;
    private final StudyRepositoryImpl studyRepository;
    private final ReviewService reviewService;
    private final UserRepositoryImpl userRepository;

    public List<PeerReviewsResponse> getPeerReviews(final Long studyId, final Long selfId, final Long peerId) {
        userRepository.findById(selfId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보 오류입니다. 리뷰를 작성할 수 없습니다."));
        studyRepository.findByIdWithParticipants(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. 리뷰를 조회할 수 없습니다."));

        final List<Review> selfReviews = reviewRepository.findSelfReviews(studyId, selfId, peerId);
        final List<Review> peerReviews = reviewRepository.findPeerReviews(studyId, selfId, peerId);

        return PeerReviewsResponse.listFrom(selfReviews, peerReviews);
    }

    public WriteReviewResponse write(WriteReviewRequest request, Long studyId, User reviewer) {
        final Study study = studyRepository.findByIdWithParticipants(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. 리뷰를 작성할 수 없습니다."));

        boolean existsReview = reviewRepository.exists(studyId, reviewer.getId(), request.revieweeId());
        if (existsReview) {
            throw new IllegalArgumentException("이미 리뷰를 작성 하셨습니다.");
        }

        final Review review = reviewService.write(request, study, reviewer);
        return WriteReviewResponse.from(reviewRepository.save(review));

    }

}
