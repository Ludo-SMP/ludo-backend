package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record PeerReviewsResponse(
        ReviewResponse selfReview, // self wrote review
        ReviewResponse peerReview  // peer wrote review
) {
    public static PeerReviewsResponse withSelf(final Review selfReview) {
        ReviewResponse reviewResponse = null;
        if (selfReview != null) {
            reviewResponse = ReviewResponse.from(selfReview);
        }
        return new PeerReviewsResponse(
                reviewResponse,
                null
        );
    }

    public PeerReviewsResponse withPeerReview(final Review peerReview) {
        ReviewResponse reviewResponse = null;
        if (peerReview != null) {
            reviewResponse = ReviewResponse.from(peerReview);
        }
        return new PeerReviewsResponse(selfReview, reviewResponse);
    }

    public static List<PeerReviewsResponse> listFrom(final List<Review> selfReviews, final List<Review> peerReviews) {

        final Map<Long, PeerReviewsResponse> mp = selfReviews.stream()
                .collect(Collectors.toMap(
                        selfReview -> selfReview.getReviewee().getId(),
                        PeerReviewsResponse::withSelf
                ));

        for (final Review peerReview : peerReviews) {
            PeerReviewsResponse res = mp.getOrDefault(peerReview.getReviewer().getId(), PeerReviewsResponse.withSelf(null));
            mp.put(peerReview.getReviewer().getId(), res.withPeerReview(peerReview));
        }

        return mp.values().stream().toList();
    }
}
