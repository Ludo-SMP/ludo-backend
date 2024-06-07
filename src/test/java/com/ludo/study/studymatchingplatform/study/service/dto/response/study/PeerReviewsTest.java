//package com.ludo.study.studymatchingplatform.study.service.dto.response.study;
//
//import static org.assertj.core.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import com.ludo.study.studymatchingplatform.study.domain.study.Review;
//import com.ludo.study.studymatchingplatform.user.domain.user.User;
//
//class PeerReviewsTest {
//
//	@Test
//	@DisplayName("[Success] SelfReview와 PeerReview List로 List<PeerReviewsResponse> 생성 예시1")
//	void convertPeerReviewsResponseEx1() {
//		final User self = User.builder()
//				.id(100L)
//				.build();
//		final List<User> peers = List.of(
//				User.builder()
//						.id(1L)
//						.build(),
//				User.builder()
//						.id(2L)
//						.build(),
//				User.builder()
//						.id(3L)
//						.build()
//		);
//
//		List<Review> selfReviews = new ArrayList<>();
//		List<Review> peerReviews = new ArrayList<>();
//
//		for (final User peer : peers) {
//			selfReviews.add(writeReview(self, peer));
//			peerReviews.add(writeReview(peer, self));
//		}
//
//		final List<PeerReviews> peerReviewsResponses = PeerReviews.listFrom(selfReviews, peerReviews);
//		assertThat(peerReviewsResponses.size()).isEqualTo(3);
//		for (int i = 0; i < peerReviewsResponses.size(); i++) {
//			assertThat(peerReviewsResponses.get(i).selfReview()).isNotNull();
//			assertThat(peerReviewsResponses.get(i).peerReview()).isNotNull();
//		}
//
//	}
//
//	@Test
//	@DisplayName("[Success] SelfReview가 하나도 없는 경우")
//	void convertPeerReviewsResponseEx2() {
//		final User self = User.builder()
//				.id(100L)
//				.build();
//		final List<User> peers = List.of(
//				User.builder()
//						.id(1L)
//						.build(),
//				User.builder()
//						.id(2L)
//						.build(),
//				User.builder()
//						.id(3L)
//						.build()
//		);
//
//		List<Review> selfReviews = new ArrayList<>();
//		List<Review> peerReviews = new ArrayList<>();
//
//		for (final User peer : peers) {
//			peerReviews.add(writeReview(peer, self));
//		}
//
//		final List<PeerReviews> peerReviewsRespons = PeerReviews.listFrom(selfReviews, peerReviews);
//		assertThat(peerReviewsRespons.size()).isEqualTo(3);
//		for (int i = 0; i < peerReviewsRespons.size(); i++) {
//			assertThat(peerReviewsRespons.get(i).selfReview()).isNull();
//			assertThat(peerReviewsRespons.get(i).peerReview()).isNotNull();
//		}
//	}
//
//	@Test
//	@DisplayName("[Success] PeerReview가 하나도 없는 경우")
//	void convertPeerReviewsResponseEx3() {
//		final User self = User.builder()
//				.id(100L)
//				.build();
//		final List<User> peers = List.of(
//				User.builder()
//						.id(1L)
//						.build(),
//				User.builder()
//						.id(2L)
//						.build(),
//				User.builder()
//						.id(3L)
//						.build()
//		);
//
//		List<Review> selfReviews = new ArrayList<>();
//		List<Review> peerReviews = new ArrayList<>();
//
//		for (final User peer : peers) {
//			selfReviews.add(writeReview(self, peer));
//		}
//
//		final List<PeerReviews> peerReviewsRespons = PeerReviews.listFrom(selfReviews, peerReviews);
//		assertThat(peerReviewsRespons.size()).isEqualTo(3);
//		for (int i = 0; i < peerReviewsRespons.size(); i++) {
//			assertThat(peerReviewsRespons.get(i).selfReview()).isNotNull();
//			assertThat(peerReviewsRespons.get(i).peerReview()).isNull();
//		}
//
//	}
//
//	@Test
//	@DisplayName("[Success] SelfReview 다수(3), PeerReview 소수(1) 리뷰 작성한 경우")
//	void convertPeerReviewsResponseEx4() {
//		final User self = User.builder()
//				.id(100L)
//				.build();
//		final List<User> peers = List.of(
//				User.builder()
//						.id(1L)
//						.build(),
//				User.builder()
//						.id(2L)
//						.build(),
//				User.builder()
//						.id(3L)
//						.build()
//		);
//
//		List<Review> selfReviews = new ArrayList<>();
//		List<Review> peerReviews = new ArrayList<>();
//
//		for (final User peer : peers) {
//			selfReviews.add(writeReview(self, peer));
//		}
//		peerReviews.add(writeReview(peers.get(0), self));
//
//		final List<PeerReviews> peerReviewsRespons = PeerReviews.listFrom(selfReviews, peerReviews);
//		assertThat(peerReviewsRespons.size()).isEqualTo(3);
//
//		long nullPeerReviewsCount = peerReviewsRespons.stream().filter(p -> p.peerReview() == null).count();
//		long notNullPeerReviewsCount = peerReviewsRespons.stream().filter(p -> p.peerReview() != null).count();
//		assertThat(nullPeerReviewsCount).isEqualTo(2);
//		assertThat(notNullPeerReviewsCount).isEqualTo(1);
//
//	}
//
//	@Test
//	@DisplayName("[Success] SelfReview 소수(1), PeerReview 다수(3) 리뷰 작성한 경우")
//	void convertPeerReviewsResponseEx5() {
//		final User self = User.builder()
//				.id(100L)
//				.build();
//		final List<User> peers = List.of(
//				User.builder()
//						.id(1L)
//						.build(),
//				User.builder()
//						.id(2L)
//						.build(),
//				User.builder()
//						.id(3L)
//						.build()
//		);
//
//		List<Review> selfReviews = new ArrayList<>();
//		List<Review> peerReviews = new ArrayList<>();
//
//		for (final User peer : peers) {
//			peerReviews.add(writeReview(peer, self));
//		}
//		selfReviews.add(writeReview(self, peers.get(peers.size() - 1)));
//
//		final List<PeerReviews> peerReviewsRespons = PeerReviews.listFrom(selfReviews, peerReviews);
//		assertThat(peerReviewsRespons.size()).isEqualTo(3);
//		for (int i = 0; i < peerReviewsRespons.size(); i++) {
//			assertThat(peerReviewsRespons.get(i).peerReview()).isNotNull();
//		}
//		long nullPeerReviewsCount = peerReviewsRespons.stream().filter(p -> p.selfReview() == null).count();
//		long notNullPeerReviewsCount = peerReviewsRespons.stream().filter(p -> p.selfReview() != null).count();
//		assertThat(nullPeerReviewsCount).isEqualTo(2);
//		assertThat(notNullPeerReviewsCount).isEqualTo(1);
//	}
//
//	private Review writeReview(final User reviewer, final User reviewee) {
//		return Review.builder()
//				.reviewer(reviewer)
//				.reviewee(reviewee)
//				.build();
//	}
//
//}