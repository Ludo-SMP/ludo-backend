package com.ludo.study.studymatchingplatform.notification.repository;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;
import com.ludo.study.studymatchingplatform.notification.repository.dto.RecruitmentNotifierCondition;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordPositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.RecruitmentPositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.RecruitmentStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class NotifierFindTest {

	@Autowired
	UserRepositoryImpl userRepository;

	@Autowired
	CategoryRepositoryImpl categoryRepository;

	@Autowired
	PositionRepositoryImpl positionRepository;

	@Autowired
	StackRepositoryImpl stackRepository;

	@Autowired
	StackCategoryRepositoryImpl stackCategoryRepository;

	@Autowired
	StudyRepositoryImpl studyRepository;

	@Autowired
	RecruitmentRepositoryImpl recruitmentRepository;

	@Autowired
	RecruitmentPositionRepositoryImpl recruitmentPositionRepository;

	@Autowired
	RecruitmentStackRepositoryImpl recruitmentStackRepository;

	@Autowired
	NotificationKeywordCategoryRepositoryImpl notificationKeywordCategoryRepository;

	@Autowired
	NotificationKeywordStackRepositoryImpl notificationKeywordStackRepository;

	@Autowired
	NotificationKeywordPositionRepositoryImpl notificationKeywordPositionRepository;

	User user;

	@BeforeEach
	void setUp() {
		categoryRepository.save(CategoryFixture.CATEGORY_PROJECT);
		categoryRepository.save(CategoryFixture.CATEGORY_CODING_TEST);
		positionRepository.save(PositionFixture.BACKEND);
		positionRepository.save(PositionFixture.FRONTEND);
		positionRepository.save(PositionFixture.DESIGNER);
		positionRepository.save(PositionFixture.DEVOPS);

		stackCategoryRepository.save(StackCategoryFixture.BACKEND);
		stackCategoryRepository.save(StackCategoryFixture.FRONTEND);
		stackRepository.save(StackFixture.JAVA);
		stackRepository.save(StackFixture.PYTHON);
		stackRepository.save(StackFixture.REACT);
		stackRepository.save(StackFixture.JAVA_SCRIPT);

		// given
		user = UserFixture.user1;
		userRepository.save(user);
	}

	@ParameterizedTest
	@MethodSource("provideNotifierConditions")
	@DisplayName("[Success] 알림 키워드-카테고리/포지션/기술스택 조건 중 3개를 모두 만족하면 모집공고 알림 대상자이다.")
	void 모집공고_알림_대상자_찾기(Category category, List<Position> positions, List<Stack> stacks) {
		// given: user1 = 카테고리 알림 키워드 - 프로젝트 등록
		givenNotificationKeywordCategoryIsProject();
		// given: user1 = 포지션 알림 키워드 - 백엔드 등록
		givenNotificationKeywordPositionIsBackend();
		// given: user1 = 기술스택 알림 키워드 - [java, python] 등록
		givenNotificationKeywordStackIsJavaAndPython();

		// when
		RecruitmentNotifierCondition recruitmentNotifierCondition = new RecruitmentNotifierCondition(
				category,
				positions,
				stacks);

		// then
		List<User> recruitmentNotifiers = userRepository.findRecruitmentNotifiers(recruitmentNotifierCondition);
		log.info("recruitmentNotifiers = {}", recruitmentNotifiers);
	}

	private void givenNotificationKeywordPositionIsBackend() {
		NotificationKeywordPosition notificationKeywordPosition1 = NotificationKeywordPosition.builder()
				.user(user)
				.position(PositionFixture.BACKEND)
				.build();
		notificationKeywordPositionRepository.save(notificationKeywordPosition1);
	}

	private void givenNotificationKeywordCategoryIsProject() {
		NotificationKeywordCategory notificationKeywordCategory1 = NotificationKeywordCategory.builder()
				.user(user)
				.category(CategoryFixture.CATEGORY_PROJECT)
				.build();
		notificationKeywordCategoryRepository.save(notificationKeywordCategory1);
	}

	private void givenNotificationKeywordStackIsJavaAndPython() {
		NotificationKeywordStack notificationKeywordStack1 = NotificationKeywordStack.builder()
				.user(user)
				.stack(StackFixture.JAVA)
				.build();
		NotificationKeywordStack notificationKeywordStack2 = NotificationKeywordStack.builder()
				.user(user)
				.stack(StackFixture.PYTHON)
				.build();
		notificationKeywordStackRepository.save(notificationKeywordStack1);
		notificationKeywordStackRepository.save(notificationKeywordStack2);
	}

	static Stream<Arguments> provideNotifierConditions() {
		return Stream.of(
				// 카테고리/포지션/기술스택 조건 중 3개를 모두 만족하는 조건
				Arguments.of(
						CategoryFixture.CATEGORY_PROJECT,
						List.of(PositionFixture.BACKEND, PositionFixture.FRONTEND),
						List.of(StackFixture.REACT, StackFixture.JAVA_SCRIPT, StackFixture.JAVA)
				),
				// 카테고리/포지션/기술스택 조건 중 카테고리/기술스택 2개만 만족하는 조건
				Arguments.of(
						CategoryFixture.CATEGORY_PROJECT,
						List.of(PositionFixture.DEVOPS),
						List.of(StackFixture.JAVA)
				),
				// 카테고리/포지션/기술스택 조건 중 기술스택 1개만 만족하는 조건
				Arguments.of(
						CategoryFixture.CATEGORY_CODING_TEST,
						List.of(PositionFixture.DEVOPS, PositionFixture.DESIGNER),
						List.of(StackFixture.JAVA)
				)
		);
	}

}