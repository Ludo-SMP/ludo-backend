package com.ludo.study.studymatchingplatform.notification.repository.notification;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.domain.notification.NotificationEventType;
import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.RecruitmentPositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.RecruitmentStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@Slf4j
class RecruitmentNotificationRepositoryImplTest {

	@Autowired
	RecruitmentNotificationRepositoryImpl recruitmentNotificationRepository;

	@Autowired
	RecruitmentNotificationRepositoryImpl notificationRepository;

	@Autowired
	UserRepositoryImpl userRepository;

	@Autowired
	StudyRepositoryImpl studyRepository;

	@Autowired
	RecruitmentRepositoryImpl recruitmentRepository;

	@Autowired
	StackRepositoryImpl stackRepository;

	@Autowired
	RecruitmentStackRepositoryImpl recruitmentStackRepository;

	@Autowired
	RecruitmentPositionRepositoryImpl recruitmentPositionRepository;

	@Autowired
	CategoryRepositoryImpl categoryRepository;

	Study study;

	Recruitment recruitment;

	@BeforeEach
	@DisplayName("모집공고 :(프로젝트, [java, react], [백엔드] 가 생성되었을 때")
	void temp() {
		User user = UserFixture.user1;
		userRepository.save(user);

		Category project = CategoryFixture.CATEGORY_PROJECT;
		categoryRepository.save(project);

		study = StudyFixture.PROJECT_ONLINE_STUDY;
		study = studyRepository.save(study);
		categoryRepository.save(study.getCategory());

		recruitment = RecruitmentFixture.recruitment1;
		study.registerRecruitment(recruitment);

		Stack java = StackFixture.JAVA;
		Stack react = StackFixture.REACT;
		RecruitmentStack javaRecruitmentStack = RecruitmentStack.from(recruitment, java);
		RecruitmentStack reactRecruitmentStack = RecruitmentStack.from(recruitment, react);

		Position backend = PositionFixture.BACKEND;
		RecruitmentPosition backendRecruitmentPosition = RecruitmentPosition.from(recruitment, backend);
		recruitmentRepository.save(recruitment);
	}

	@Test
	@DisplayName("[Success] 모집공고 알림 조회")
	void test() {
		// given: 모집 공고 알림 대상자가 주어졌을 때
		User user1 = UserFixture.user1;
		User user2 = UserFixture.user2;
		User user3 = UserFixture.user3;
		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);

		NotificationEventType recruitmentEventType = NotificationEventType.RECRUITMENT;
		List<RecruitmentNotification> recruitmentNotifications = List.of(
				of(recruitmentEventType, LocalDateTime.now(), recruitment, user1),
				of(recruitmentEventType, LocalDateTime.now(), recruitment, user2),
				of(recruitmentEventType, LocalDateTime.now(), recruitment, user3));

		// when: 알림 대상자에 맞는 알림을 저장하면
		List<RecruitmentNotification> findRecruitmentNotifications = recruitmentNotificationRepository.saveAll(
				recruitmentNotifications);

		// then: 알림 대상자 수만큼 모집공고 알림이 저장된다.
		log.info("findRecruitmentNotifications = {}", findRecruitmentNotifications);
		List<RecruitmentNotification> allByUserId = notificationRepository.findAllByUserId(user1.getId());
		RecruitmentNotification notification = allByUserId.get(0);
		User notifier = notification.getNotifier();
		log.info("findNotifications = {}", notification);
		log.info("notifier = {}", notifier);

	}
}