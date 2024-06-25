package com.ludo.study.studymatchingplatform.study.service.dto.response.study;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.common.exception.DataNotFoundException;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.study.StudyStatisticsResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyStatisticsService {

	private final StudyStatisticsRepositoryImpl studyStatisticsRepository;
	private final UserRepositoryImpl userRepository;

	public StudyStatisticsResponse findOrCreateByUserId(final Long userId) {
		final User user = userRepository.findById(userId)
				.orElseThrow(() -> new DataNotFoundException("탈퇴한 사용자입니다."));

		return StudyStatisticsResponse.from(
				studyStatisticsRepository.findByUserId(userId)
						.orElseGet(() -> studyStatisticsRepository.save(StudyStatistics.of(user)))
		);
	}

	public List<StudyStatistics> findOrCreateByUsers(final List<User> users) {
		final List<Long> userIds = users.stream().map(User::getId).toList();

		final List<StudyStatistics> studyStatistics = studyStatisticsRepository.findByUserIdsIn(userIds);
		for (final User user : users) {
			final boolean notExists = studyStatistics.stream()
					.noneMatch(s -> s.getUser() == user);

			if (notExists) {
				final StudyStatistics newStatistics = StudyStatistics.builder()
						.user(user)
						.build();
				studyStatisticsRepository.save(newStatistics);
				studyStatistics.add(newStatistics);
			}
		}

		return studyStatistics;
	}
}
