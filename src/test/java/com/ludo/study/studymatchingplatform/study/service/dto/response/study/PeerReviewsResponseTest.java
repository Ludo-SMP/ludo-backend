package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PeerReviewsResponseTest {

    @Test
    @DisplayName("[Success] SelfReview와 PeerReview List로 List<PeerReviewsResponse> 생성 예시1")
    void convertPeerReviewsResponseEx1() {
        final User self = User.builder()
                .id(100L)
                .build();
        final List<User> peers = List.of(
                User.builder()
                        .id(1L)
                        .build(),
                User.builder()
                        .id(2L)
                        .build(),
                User.builder()
                        .id(3L)
                        .build()
        );

        List<Review> selfReviews = new ArrayList<>();
        List<Review> peerReviews = new ArrayList<>();

        for (final User peer : peers) {
            selfReviews.add(writeReview(self, peer));
            peerReviews.add(writeReview(peer, self));
        }

        final List<PeerReviewsResponse> peerReviewsResponses = PeerReviewsResponse.listFrom(selfReviews, peerReviews);
        assertThat(peerReviewsResponses.size()).isEqualTo(3);
        for (int i = 0; i < peerReviewsResponses.size(); i++) {
            assertThat(peerReviewsResponses.get(i).selfReview()).isNotNull();
            assertThat(peerReviewsResponses.get(i).peerReview()).isNotNull();
        }

    }

    @Test
    @DisplayName("[Success] SelfReview가 하나도 없는 경우")
    void convertPeerReviewsResponseEx2() {
        final User self = User.builder()
                .id(100L)
                .build();
        final List<User> peers = List.of(
                User.builder()
                        .id(1L)
                        .build(),
                User.builder()
                        .id(2L)
                        .build(),
                User.builder()
                        .id(3L)
                        .build()
        );

        List<Review> selfReviews = new ArrayList<>();
        List<Review> peerReviews = new ArrayList<>();

        for (final User peer : peers) {
            peerReviews.add(writeReview(peer, self));
        }

        final List<PeerReviewsResponse> peerReviewsResponses = PeerReviewsResponse.listFrom(selfReviews, peerReviews);
        assertThat(peerReviewsResponses.size()).isEqualTo(3);
        for (int i = 0; i < peerReviewsResponses.size(); i++) {
            assertThat(peerReviewsResponses.get(i).selfReview()).isNull();
            assertThat(peerReviewsResponses.get(i).peerReview()).isNotNull();
        }
    }


    @Test
    @DisplayName("[Success] PeerReview가 하나도 없는 경우")
    void convertPeerReviewsResponseEx3() {
        final User self = User.builder()
                .id(100L)
                .build();
        final List<User> peers = List.of(
                User.builder()
                        .id(1L)
                        .build(),
                User.builder()
                        .id(2L)
                        .build(),
                User.builder()
                        .id(3L)
                        .build()
        );

        List<Review> selfReviews = new ArrayList<>();
        List<Review> peerReviews = new ArrayList<>();

        for (final User peer : peers) {
            selfReviews.add(writeReview(self, peer));
        }

        final List<PeerReviewsResponse> peerReviewsResponses = PeerReviewsResponse.listFrom(selfReviews, peerReviews);
        assertThat(peerReviewsResponses.size()).isEqualTo(3);
        for (int i = 0; i < peerReviewsResponses.size(); i++) {
            assertThat(peerReviewsResponses.get(i).selfReview()).isNotNull();
            assertThat(peerReviewsResponses.get(i).peerReview()).isNull();
        }

    }

    @Test
    @DisplayName("[Success] SelfReview 다수(3), PeerReview 소수(1) 리뷰 작성한 경우")
    void convertPeerReviewsResponseEx4() {
        final User self = User.builder()
                .id(100L)
                .build();
        final List<User> peers = List.of(
                User.builder()
                        .id(1L)
                        .build(),
                User.builder()
                        .id(2L)
                        .build(),
                User.builder()
                        .id(3L)
                        .build()
        );

        List<Review> selfReviews = new ArrayList<>();
        List<Review> peerReviews = new ArrayList<>();

        for (final User peer : peers) {
            selfReviews.add(writeReview(self, peer));
        }
        peerReviews.add(writeReview(peers.getFirst(), self));

        final List<PeerReviewsResponse> peerReviewsResponses = PeerReviewsResponse.listFrom(selfReviews, peerReviews);
        assertThat(peerReviewsResponses.size()).isEqualTo(3);

        long nullPeerReviewsCount = peerReviewsResponses.stream().filter(p -> p.peerReview() == null).count();
        long notNullPeerReviewsCount = peerReviewsResponses.stream().filter(p -> p.peerReview() != null).count();
        assertThat(nullPeerReviewsCount).isEqualTo(2);
        assertThat(notNullPeerReviewsCount).isEqualTo(1);

    }

    @Test
    @DisplayName("[Success] SelfReview 소수(1), PeerReview 다수(3) 리뷰 작성한 경우")
    void convertPeerReviewsResponseEx5() {
        final User self = User.builder()
                .id(100L)
                .build();
        final List<User> peers = List.of(
                User.builder()
                        .id(1L)
                        .build(),
                User.builder()
                        .id(2L)
                        .build(),
                User.builder()
                        .id(3L)
                        .build()
        );

        List<Review> selfReviews = new ArrayList<>();
        List<Review> peerReviews = new ArrayList<>();

        for (final User peer : peers) {
            peerReviews.add(writeReview(peer, self));
        }
        selfReviews.add(writeReview(self, peers.getLast()));

        final List<PeerReviewsResponse> peerReviewsResponses = PeerReviewsResponse.listFrom(selfReviews, peerReviews);
        assertThat(peerReviewsResponses.size()).isEqualTo(3);
        for (int i = 0; i < peerReviewsResponses.size(); i++) {
            assertThat(peerReviewsResponses.get(i).peerReview()).isNotNull();
        }
        long nullPeerReviewsCount = peerReviewsResponses.stream().filter(p -> p.selfReview() == null).count();
        long notNullPeerReviewsCount = peerReviewsResponses.stream().filter(p -> p.selfReview() != null).count();
        assertThat(nullPeerReviewsCount).isEqualTo(2);
        assertThat(notNullPeerReviewsCount).isEqualTo(1);
    }

    private Review writeReview(final User reviewer, final User reviewee) {
        return Review.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .build();
    }

}