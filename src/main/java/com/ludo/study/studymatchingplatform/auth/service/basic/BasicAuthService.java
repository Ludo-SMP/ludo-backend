package com.ludo.study.studymatchingplatform.auth.service.basic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.auth.service.bcrypt.BcryptService;
import com.ludo.study.studymatchingplatform.auth.service.google.dto.request.BasicLoginRequest;
import com.ludo.study.studymatchingplatform.auth.service.google.dto.request.BasicSignupRequest;
import com.ludo.study.studymatchingplatform.common.exception.DataConflictException;
import com.ludo.study.studymatchingplatform.common.exception.DataNotFoundException;
import com.ludo.study.studymatchingplatform.common.exception.UnauthorizedUserException;
import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.repository.config.GlobalNotificationUserConfigRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.domain.study.ReviewStatistics;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyStatisticsRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicAuthService {

	private final UserRepositoryImpl userRepository;
	private final BcryptService bcryptService;
	private final StudyStatisticsRepositoryImpl studyStatisticsRepository;
	private final ReviewStatisticsRepositoryImpl reviewStatisticsRepository;

	private final GlobalNotificationUserConfigRepositoryImpl notificationUserConfigRepository;

	public User signup(final BasicSignupRequest request) {
		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new DataConflictException("이미 가입된 이메일입니다.");
		}

		if (userRepository.existsByNickname(request.nickname())) {
			throw new DataConflictException("이미 존재하는 닉네임입니다.");
		}

		final String hashedPassword = bcryptService.hashPassword(request.password());
		final User user = userRepository.save(request.toUser(hashedPassword));
		notificationUserConfigRepository.save(GlobalNotificationUserConfig.ofNewSignUpUser(user));
		createStudyStatistics(user); // 사용자 세부 정보 생성
		createReviewStatistics(user); // 사용자 기본 리뷰 정보 생성
		return user;
	}

	public User login(final BasicLoginRequest request) {
		final User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new DataNotFoundException("가입되지 않은 이메일입니다."));

		if (!bcryptService.verifyPassword(request.password(), user.getPassword())) {
			throw new UnauthorizedUserException("비밀번호가 일치하지 않습니다.");
		}

		return user;
	}

	private void createStudyStatistics(final User user) {
		final StudyStatistics studyStatistics = StudyStatistics.of(user);
		studyStatisticsRepository.save(studyStatistics);
	}

	private void createReviewStatistics(final User user) {
		final ReviewStatistics reviewStatistics = ReviewStatistics.of(user);
		reviewStatisticsRepository.save(reviewStatistics);
	}

}
