package com.ludo.study.studymatchingplatform.study.service.study;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.common.exception.DataNotFoundException;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.ReviewStatisticsResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewStatisticsService {

	private final UserRepositoryImpl userRepository;
	private final ReviewStatisticsRepositoryImpl reviewStatisticsRepository;

	public Optional<ReviewStatistics> findByUserId(final Long userId) {
		return reviewStatisticsRepository.findByUserId(userId);
	}

	public ReviewStatistics _findOrCreateByUserId(final Long userId) {
		final User user = userRepository.findById(userId)
				.orElseThrow(() -> new DataNotFoundException("탈퇴한 사용자입니다."));

		return reviewStatisticsRepository.findByUserId(userId)
				.orElseGet(() -> reviewStatisticsRepository.save(ReviewStatistics.of(user)));
	}

	public ReviewStatisticsResponse findOrCreateByUserId(final Long userId) {
		return ReviewStatisticsResponse.from(_findOrCreateByUserId(userId));
	}

	public void updateRevieweeStatistics(final Review review) {
		final ReviewStatistics revieweeStatistics = _findOrCreateByUserId(review.getReviewee().getId());
		revieweeStatistics.update(review);
	}

	public List<ReviewStatistics> findByUserIdsIn(final List<Long> userIds) {
		final List<ReviewStatistics> reviewStatistics = new ArrayList<>();
		for (final Long userId : userIds) {
			reviewStatistics.add(_findOrCreateByUserId(userId));
		}
		return reviewStatistics;
	}
}
