package com.ludo.study.studymatchingplatform.notification.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordPositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.notification.repository.keyword.NotificationKeywordStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class NotificationQueryServiceTest {

	@Autowired
	NotificationQueryService notificationQueryService;

	@Autowired
	UserRepositoryImpl userRepository;

	Category project = CategoryFixture.CATEGORY_PROJECT;
	Category codingTest = CategoryFixture.CATEGORY_CODING_TEST;

	Stack java = StackFixture.JAVA;
	Stack python = StackFixture.PYTHON;
	Stack react = StackFixture.REACT;
	Stack javaScript = StackFixture.JAVA_SCRIPT;

	Position backend = PositionFixture.BACKEND;
	Position frontend = PositionFixture.FRONTEND;

	User user1;
	User user2;
	User user3;
	User user4;

	@BeforeEach
	void setup() {
		user1 = userRepository.save(UserFixture.user1);
		user2 = userRepository.save(UserFixture.user2);
		user3 = userRepository.save(UserFixture.user3);
		user4 = userRepository.save(UserFixture.user4);
	}

	@Nested
	@DisplayName("관심 모집공고 알림 대상자 조회는 모집공고의 스펙이 [카테고리:=프로젝트], [모집기술스택:=java, python], [모집포지션:=백엔드]일 때")
	class Describe_FindRecruitmentNotifier {

		@Autowired
		NotificationKeywordCategoryRepositoryImpl keywordCategoryRepository;

		@Autowired
		NotificationKeywordStackRepositoryImpl keywordStackRepository;

		@Autowired
		NotificationKeywordPositionRepositoryImpl keywordPositionRepository;

		private Recruitment recruitment;

		@Test
		@DisplayName("[Success] 알림 키워드-카테고리/포지션/기술스택 조건 중 3개를 모두 만족하는 알림 대상자를 조회")
		@Transactional
		void findNotifierMatchedCondition() {
			// given
			setUpUser1ProjectJavaPythonBackendRecruitment();
			setupNotificationKeyword(user1, List.of(codingTest), List.of(java, python), List.of(backend));
			setupNotificationKeyword(user2, List.of(project), List.of(javaScript, react, python),
					List.of(backend));
			setupNotificationKeyword(user3, List.of(project), List.of(react, python), List.of(frontend));

			// when
			List<User> recruitmentNotifiers = notificationQueryService.findRecruitmentNotifier(recruitment);

			// then
			assertThat(recruitmentNotifiers).containsExactly(user2);
		}

		@Test
		@DisplayName("[Success] 모집공고 스터디장을 제외한 알림 대상자를 조회")
		@Transactional
		void findNotifierExceptRecruitmentOwner() {
			// given
			setUpUser1ProjectJavaPythonBackendRecruitment();
			setupNotificationKeyword(user1, List.of(project), List.of(java), List.of(backend));
			setupNotificationKeyword(user2, List.of(project), List.of(java), List.of(backend));
			setupNotificationKeyword(user3, List.of(project), List.of(python), List.of(backend));

			// when
			List<User> recruitmentNotifiers = notificationQueryService.findRecruitmentNotifier(recruitment);

			// then
			assertThat(recruitmentNotifiers).containsExactly(user2, user3);
		}

		@Test
		@DisplayName("[Success] 모집공고 알림 설정을 on한 알림 대상자만 조회")
		@Transactional
		void findNotifierWithRecruitmentNotificationConfigOn() {
			// TODO: 알림 설정 API 구현 완료 후 테스트
		}

		private void setUpUser1ProjectJavaPythonBackendRecruitment() {
			Study study = StudyFixture.USER1_PROJECT_ONLINE_STUDY;

			recruitment = RecruitmentFixture.recruitment1;
			RecruitmentStack.from(recruitment, java)
					.registerRecruitmentAndStack(recruitment, java);
			RecruitmentStack.from(recruitment, python)
					.registerRecruitmentAndStack(recruitment, python);
			RecruitmentPosition.from(recruitment, backend)
					.registerRecruitment(recruitment);

			study.registerRecruitment(recruitment);
		}

		private void setupNotificationKeyword(User user, List<Category> categories,
											  List<Stack> stacks,
											  List<Position> positions
		) {
			for (Category category : categories) {
				NotificationKeywordCategory notificationKeywordCategory = NotificationKeywordCategory.of(user,
						category);
				keywordCategoryRepository.save(notificationKeywordCategory);
			}
			for (Stack stack : stacks) {
				NotificationKeywordStack notificationKeywordStack = NotificationKeywordStack.of(user, stack);
				keywordStackRepository.save(notificationKeywordStack);
			}
			for (Position position : positions) {
				NotificationKeywordPosition notificationKeywordPosition = NotificationKeywordPosition.of(user,
						position);
				keywordPositionRepository.save(notificationKeywordPosition);
			}
		}
	}

	@Nested
	@DisplayName("스터디 종료기간 알림 대상자 조회는 ")
	class Describe_FindStudyEndDateNotifier {

		@Autowired
		ParticipantRepositoryImpl participantRepository;

		@Test
		@DisplayName("[Success] 현재 시각이 yy:mm:dd:hh:mm:ss일 때, yy:mm:dd+5:hh:mm:ss인 스터디들의 참가자들을 알림 대상자로 조회")
		void test() {
			// TODO: UtcTimePicker PR merge 후 테스트 작성
		}

		@Test
		@DisplayName("[Success] 스터디 종료기간 알림 설정을 on한 알림 대상자만 조회")
		@Transactional
		void findNotifierWithConfigOn() {
			// TODO: 알림 설정 API 구현 완료 후 테스트
		}

	}

	@Nested
	@DisplayName("스터디 지원현황 알림 대상자 조회는")
	class Describe_FindStudyApplicantNotifier {

		@Autowired
		private StudyRepositoryImpl studyRepository;

		@Autowired
		private ParticipantRepositoryImpl participantRepository;

		@Test
		@DisplayName("[Success] 지원자가 지원한 스터디의 스터디원들을 알림 대상자로 조회")
		@Transactional
		void findNotifierStudyParticipants() {
			// given
			Study study = setUpStudyWithParticipantUser1User3();

			// when
			List<User> studyApplicantNotifiers = notificationQueryService.findStudyApplicantNotifier(study);

			// then
			assertThat(studyApplicantNotifiers).containsExactly(user1, user3);
		}

		@Test
		@DisplayName("[Success] 지원현황 알림 설정을 on한 알림 대상자만 조회")
		@Transactional
		void findNotifierWithStudyApplicantNotificationConfigOn() {
			// TODO: 알림 설정 API 구현 완료 후 테스트
		}

		private Study setUpStudyWithParticipantUser1User3() {
			Study study = StudyFixture.USER1_PROJECT_ONLINE_STUDY;
			Participant participant1 = Participant.from(study, user1, backend, Role.OWNER);
			Participant participant3 = Participant.from(study, user3, backend, Role.MEMBER);

			study.addParticipant(participant1);
			study.addParticipant(participant3);

			studyRepository.save(study);
			participantRepository.save(participant1);
			participantRepository.save(participant3);
			return study;
		}

	}

	@Nested
	@DisplayName("스터디 리뷰 시작 알림 대상자 조회는")
	class Describe_FindStudyReviewStartNotifier {

		@Autowired
		ParticipantRepositoryImpl participantRepository;

		@Test
		@DisplayName("[Success] 현재 시각이 yy:mm:dd:hh:mm:ss일 때, 종료 기간이 yy:mm:dd-1:hh:mm:ss이고 진행 상태가 완료인 스터디들의 스터디원들을 알림 대상자로 조회")
		void test() {
			// TODO: UtcNowPicker PR merge 후 테스트
		}

		@Test
		@DisplayName("[Success] 스터디 리뷰 시작 알림 설정을 on한 알림 대상자만 조회")
		@Transactional
		void findNotifierWithConfigOn() {
			// TODO: 알림 설정 API 구현 완료 후 테스트
		}

	}

	@Nested
	@DisplayName("상호 리뷰 완료 알림 대상자 조회는")
	class Describe_FindReviewPeerFinishNotifier {

		@Autowired
		StudyRepositoryImpl studyRepository;

		@Autowired
		ReviewRepositoryImpl reviewRepository;

		@Test
		@DisplayName("[Success] 서로에게 리뷰를 남겨준 스터디원을 알림 대상자로 조회")
		void findNotifierPeerReviewFinish() {
			// given
			Study study = StudyFixture.USER1_PROJECT_ONLINE_STUDY;
			Review user1ToUser2Review = createReview(study, user1, user2);
			Review user2ToUser1Review = createReview(study, user2, user1);    // 상호 리뷰 완료
			Review user3ToUser4Review = createReview(study, user3, user4);    // 상호 리뷰 X

			studyRepository.save(study);
			reviewRepository.save(user1ToUser2Review);
			reviewRepository.save(user2ToUser1Review);
			reviewRepository.save(user3ToUser4Review);

			// when
			List<User> reviewPeerFinishNotifiers1 = notificationQueryService.findReviewPeerFinishNotifier(study,
					user2ToUser1Review);
			List<User> reviewPeerFinishNotifiers2 = notificationQueryService.findReviewPeerFinishNotifier(study,
					user3ToUser4Review);

			// then
			assertThat(reviewPeerFinishNotifiers1).contains(user1, user2);
			assertThat(reviewPeerFinishNotifiers2).isEmpty();
		}

		@Test
		@DisplayName("[Success] 상호 리뷰 완료 알림 설정을 on한 알림 대상자만 조회")
		@Transactional
		void findNotifierWithConfigOn() {
			// TODO: 알림 설정 API 구현 완료 후 테스트
		}

		private Review createReview(Study study, User reviewer, User reviewee) {
			return Review.builder()
					.study(study)
					.reviewer(reviewer)
					.reviewee(reviewee)
					.build();
		}
	}

	@Nested
	@DisplayName("알림 목록 조회는")
	class Describe_FindNotifications {
		// TODO: Front Route Params 매핑 로직 구현 후 테스트
	}

}