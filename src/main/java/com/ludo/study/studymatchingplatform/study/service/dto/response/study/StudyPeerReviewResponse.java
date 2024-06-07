package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record StudyPeerReviewResponse(
        Long id,
        String title,
        List<PeerReviewsResponse> reviews,
        LocalDateTime updatedDateTime
) {
    public static List<StudyPeerReviewResponse> listFrom(final List<Review> selfReviews, final List<Review> peerReviews) {
        final List<Study> studySet = extractStudies(selfReviews, peerReviews);
        final Map<Long, List<Review>> selfReviewsMap = selfReviews.stream()
                .collect(Collectors.groupingBy(r -> r.getStudy().getId()));
        final Map<Long, List<Review>> peerReviewsMap = peerReviews.stream()
                .collect(Collectors.groupingBy(r -> r.getStudy().getId()));

        final List<StudyPeerReviewResponse> studyPeerReviewResponses = new ArrayList<>();
        for (final Study study : studySet) {
            final Long studyId = study.getId();
            final List<PeerReviewsResponse> peerReviewsResponses = PeerReviewsResponse.listFrom(selfReviewsMap.getOrDefault(studyId, new ArrayList<>()), peerReviewsMap.getOrDefault(studyId, new ArrayList<>()));
            final LocalDateTime latestUpdatedDateTime = extractLatestUpdatedDateTime(selfReviewsMap.getOrDefault(studyId, new ArrayList<>()), peerReviewsMap.getOrDefault(studyId, new ArrayList<>()));
            studyPeerReviewResponses.add(new StudyPeerReviewResponse(study.getId(), study.getTitle(), peerReviewsResponses, latestUpdatedDateTime));
        }
        // Sort by updatedDateTime in descending order
        studyPeerReviewResponses.sort(Comparator.comparing(StudyPeerReviewResponse::updatedDateTime).reversed());

        return studyPeerReviewResponses;
    }

    private static List<Study> extractStudies(List<Review> selfReviews, List<Review> peerReviews) {
        // Extract studyIds from both lists
        final Set<Study> studySet = new HashSet<>();

        // Add studyIds from selfReviews
        for (Review review : selfReviews) {
            studySet.add(review.getStudy());
        }

        // Add studyIds from peerReviews
        for (Review review : peerReviews) {
            studySet.add(review.getStudy());
        }

        // Convert the set to a list
        return new ArrayList<>(studySet);
    }

    private static LocalDateTime extractLatestUpdatedDateTime(List<Review> selfReviews, List<Review> peerReviews) {
        return Stream.concat(selfReviews.stream(), peerReviews.stream())
                .map(Review::getUpdatedDateTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);  // Return null if no reviews are present
    }

}
