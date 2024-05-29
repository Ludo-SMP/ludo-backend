package com.ludo.study.studymatchingplatform.study.service.study;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.service.NotificationService;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteReviewRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.ReviewResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewFacade {

	private final ReviewRepositoryImpl reviewRepository;
	private final StudyRepositoryImpl studyRepository;
	private final ReviewService reviewService;
	private final NotificationService notificationService;

	public ReviewResponse write(WriteReviewRequest request, Long studyId, User reviewer) {
		final Study study = studyRepository.findByIdWithParticipants(studyId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. 리뷰를 작성할 수 없습니다."));

		boolean b = reviewRepository.exists(studyId, reviewer.getId(), request.revieweeId());

		if (b) {
			throw new IllegalArgumentException("이미 리뷰를 작성 하셨습니다.");
		}

		final Review review = reviewRepository.save(reviewService.write(request, study, reviewer));

		// 리뷰 알림
		notificationService.reviewReceiveNotice(review);
		notificationService.reviewPeerFinishNotice(study, review);

		return ReviewResponse.from(review);

	}

}
