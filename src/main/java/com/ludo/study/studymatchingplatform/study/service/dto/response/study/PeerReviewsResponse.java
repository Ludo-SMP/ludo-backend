package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record PeerReviewsResponse(
        ReviewResponse selfReview, // self wrote review
        ReviewResponse peerReview,  // peer wrote review
        LocalDateTime updatedDateTime
) {

    public static PeerReviewsResponse of(final Review selfReview, final Review peerReview) {
        LocalDateTime updatedDateTime = null;
        if (selfReview != null && peerReview != null) {
            updatedDateTime = selfReview.getUpdatedDateTime().isAfter(peerReview.getUpdatedDateTime()) ? selfReview.getUpdatedDateTime() : peerReview.getUpdatedDateTime();
        } else if (selfReview == null) {
            updatedDateTime = peerReview.getUpdatedDateTime();
        } else if (peerReview == null) {
            updatedDateTime = selfReview.getUpdatedDateTime();

        }
        return new PeerReviewsResponse(
                selfReview == null ? null : ReviewResponse.from(selfReview),
                peerReview == null ? null : ReviewResponse.from(peerReview),
                updatedDateTime
        );
    }

    public static List<PeerReviewsResponse> listFrom(final List<Review> selfReviews, final List<Review> peerReviews) {
        final List<PeerReviewsResponse> responses = new ArrayList<>();
        final Map<Long, Review> selfReviewMap = selfReviews.stream()
                .collect(Collectors.toMap(r -> r.getReviewer().getId(), r -> r));
        final Map<Long, Review> peerReviewMap = peerReviews.stream()
                .collect(Collectors.toMap(r -> r.getReviewee().getId(), r -> r));

        final Set<Long> allReviewerIds = new HashSet<>();
        allReviewerIds.addAll(selfReviewMap.keySet());
        allReviewerIds.addAll(peerReviewMap.keySet());

        for (Long reviewerId : allReviewerIds) {
            final Review selfReview = selfReviewMap.get(reviewerId);
            final Review peerReview = peerReviewMap.get(reviewerId);
            responses.add(PeerReviewsResponse.of(selfReview, peerReview));
        }

        return responses;

    }
}
//
//        public static List<PeerReviewsResponse> listFrom ( final List<Review> selfReviews,
//        final List<Review> peerReviews){
//
//            final Map<Long, PeerReviewsResponse> mp = selfReviews.stream()
//                    .collect(Collectors.toMap(
//                            s -> s.getReviewee().getId(),
//                            PeerReviewsResponse::withSelf
//                    ));
//
//            for (final Review p : peerReviews) {
//                final PeerReviewsResponse res = mp.getOrDefault(p.getReviewer().getId(), PeerReviewsResponse.withSelf(null));
//                mp.put(p.getReviewer().getId(), res.withPeerReview(p));
//            }
//
//            return mp.values().stream().toList();
//        }
//
//        private static PeerReviewsResponse withSelfReview ( final Review selfReview){
//            ReviewResponse reviewResponse = null;
//            if (selfReview != null) {
//                reviewResponse = ReviewResponse.from(selfReview);
//            }
//            return PeerReviewsResponse.of(
//                    reviewResponse,
//                    null
//            );
//        }
//
//        private PeerReviewsResponse withPeerReview ( final Review peerReview){
//            ReviewResponse reviewResponse = null;
//            if (peerReview != null) {
//                reviewResponse = ReviewResponse.from(peerReview);
//            }
//            return new PeerReviewsResponse(selfReview, reviewResponse);
//        }
//    }
