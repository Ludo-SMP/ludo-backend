package com.ludo.study.studymatchingplatform.notification.service;

import static com.ludo.study.studymatchingplatform.study.domain.study.participant.Role.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.notification.domain.config.GlobalNotificationUserConfig;
import com.ludo.study.studymatchingplatform.notification.domain.config.NotificationConfigGroup;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;
import com.ludo.study.studymatchingplatform.notification.repository.config.GlobalNotificationUserConfigRepositoryImpl;
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
import com.ludo.study.studymatchingplatform.study.service.study.FixedUtcDateTimePicker;
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

	@Autowired
	StudyRepositoryImpl studyRepository;

	@Autowired
	ParticipantRepositoryImpl participantRepository;

	@Autowired
	GlobalNotificationUserConfigRepositoryImpl globalNotificationUserConfigRepository;

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
		user1 = userRepository.save(UserFixture.USER1());
		user2 = userRepository.save(UserFixture.USER2());
		user3 = userRepository.save(UserFixture.USER3());
		user4 = userRepository.save(UserFixture.USER4());
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
		@DisplayName("[Success] 알림 키워드-카테고리/포지션/기술스택 조건 중 최소 한개씩 모두 일치하는 사용자 조회")
		@Transactional
		void findNotifierMatchedCondition() {
			// given
			setUpUser1ProjectJavaPythonBackendRecruitment();
			setupNotificationKeyword(user1, List.of(codingTest), List.of(java, python), List.of(backend));
			setupNotificationKeyword(user2, List.of(project), List.of(javaScript, react, python),
					List.of(backend));
			setupNotificationKeyword(user3, List.of(project), List.of(react, python), List.of(frontend));
			setUpGlobalNotificationUserConfig(List.of(user1, user2, user3), NotificationConfigGroup.RECRUITMENT_CONFIG,
					true);

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
			setUpGlobalNotificationUserConfig(List.of(user1, user2, user3), NotificationConfigGroup.RECRUITMENT_CONFIG,
					true);

			// when
			List<User> recruitmentNotifiers = notificationQueryService.findRecruitmentNotifier(recruitment);

			// then
			assertThat(recruitmentNotifiers).containsExactly(user2, user3);
		}

		@Test
		@DisplayName("[Success] 모집공고 알림 설정을 on한 알림 대상자만 조회")
		@Transactional
		void findNotifierWithRecruitmentNotificationConfigOn() {
			setUpUser1ProjectJavaPythonBackendRecruitment();
			setupNotificationKeyword(user1, List.of(project), List.of(java), List.of(backend));
			setupNotificationKeyword(user2, List.of(project), List.of(java), List.of(backend));
			setupNotificationKeyword(user3, List.of(project), List.of(java), List.of(backend));
			setupNotificationKeyword(user4, List.of(project), List.of(java), List.of(backend));
			setUpGlobalNotificationUserConfig(List.of(user2, user4), NotificationConfigGroup.RECRUITMENT_CONFIG, true);

			// when
			List<User> recruitmentNotifiers = notificationQueryService.findRecruitmentNotifier(recruitment);

			// then
			assertThat(recruitmentNotifiers).containsExactly(user2, user4);
		}

		private void setUpUser1ProjectJavaPythonBackendRecruitment() {
			Study study = StudyFixture.PROJECT_ONLINE_STUDY(user1, new FixedUtcDateTimePicker());

			recruitment = RecruitmentFixture.recruitment1;
			RecruitmentStack.from(recruitment, java)
					.registerRecruitmentAndStack(recruitment, java);
			RecruitmentStack.from(recruitment, python)
					.registerRecruitmentAndStack(recruitment, python);
			RecruitmentPosition.from(recruitment, backend)
					.registerRecruitment(recruitment);

			study.registerRecruitment(recruitment);
		}

		private void setupNotificationKeyword(User user,
											  List<Category> categories,
											  List<Stack> stacks,
											  List<Position> positions
		) {
			for (Category category : categories) {
				NotificationKeywordCategory keywordCategory = NotificationKeywordCategory.of(user, category);
				keywordCategoryRepository.save(keywordCategory);
			}
			for (Stack stack : stacks) {
				NotificationKeywordStack keywordStack = NotificationKeywordStack.of(user, stack);
				keywordStackRepository.save(keywordStack);
			}
			for (Position position : positions) {
				NotificationKeywordPosition keywordPosition = NotificationKeywordPosition.of(user, position);
				keywordPositionRepository.save(keywordPosition);
			}
		}
	}

	@Nested
	@DisplayName("스터디 종료기간 알림 대상자 조회는 ")
	class Describe_FindStudyEndDateNotifier {

		@Autowired
		ParticipantRepositoryImpl participantRepository;

		@Autowired
		NotificationQueryService notificationQueryService;

		@Test
		@DisplayName("[Success] 스터디 종료 기간이 현재 시각 기준 +5일인 모든 스터디의 스터디장을 알림 대상자로 조회")
		@Transactional
		void studyEndDateInRangeTest() {
			// given
			LocalDateTime fixedDateTime = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME;
			Study study1 = StudyFixture.STUDY1(user1, fixedDateTime,
					LocalDateTime.of(fixedDateTime.getYear(), fixedDateTime.getMonth(),
							fixedDateTime.getDayOfMonth() + 5, 0, 0, 0, 0));

			Study study2 = StudyFixture.STUDY2(user2, fixedDateTime,
					LocalDateTime.of(fixedDateTime.getYear(), fixedDateTime.getMonth(),
							fixedDateTime.getDayOfMonth() + 5, 23, 59, 59, 999999));

			Study study3 = StudyFixture.STUDY3(user3, fixedDateTime,
					LocalDateTime.of(fixedDateTime.getYear(), fixedDateTime.getMonth(),
							fixedDateTime.getDayOfMonth() + 5, 11, 30, 50, 0));

			Participant participant1OfStudy1 = createParticipant(study1, user1, backend, OWNER);
			Participant participant2OfStudy1 = createParticipant(study1, user2, backend, MEMBER);

			Participant participant1OfStudy2 = createParticipant(study2, user2, backend, OWNER);
			Participant participant2OfStudy2 = createParticipant(study2, user1, backend, MEMBER);
			Participant participant3OfStudy2 = createParticipant(study2, user3, backend, MEMBER);
			Participant participant4OfStudy2 = createParticipant(study2, user4, backend, MEMBER);

			Participant participant1OfStudy3 = createParticipant(study3, user1, backend, OWNER);

			setUpStudyAndParticipant(
					List.of(study1, study2, study3),
					List.of(participant1OfStudy1, participant2OfStudy1, participant1OfStudy2, participant2OfStudy2,
							participant3OfStudy2, participant4OfStudy2, participant1OfStudy3));

			setUpGlobalNotificationUserConfig(List.of(user1, user2, user3, user4),
					NotificationConfigGroup.STUDY_END_DATE_CONFIG, true);

			// when
			List<Participant> studyEndDateNotifier = notificationQueryService.findStudyEndDateNotifier();

			// then
			assertThat(studyEndDateNotifier).containsExactly(participant1OfStudy1, participant1OfStudy2,
					participant1OfStudy3);
		}

		@Test
		@DisplayName("[Success] 스터디 종료 기간이 현재 시각 기준 +5일이 아닌 모든 스터디의 스터디장은 알림 대상자 조회 X")
		@Transactional
		void studyEndDateOutRangeTest() {
			// given
			LocalDateTime fixedDateTime = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME;
			Study study1 = StudyFixture.STUDY1(user1, fixedDateTime,
					LocalDateTime.of(fixedDateTime.getYear(), fixedDateTime.getMonth(),
							fixedDateTime.getDayOfMonth() + 4, 23, 59, 59, 999999));
			Study study2 = StudyFixture.STUDY2(user2, fixedDateTime,
					LocalDateTime.of(fixedDateTime.getYear(), fixedDateTime.getMonth(),
							fixedDateTime.getDayOfMonth() + 6, 0, 0, 0, 1));

			Participant participant1 = createParticipant(study1, user1, frontend, OWNER);
			Participant participant2 = createParticipant(study2, user2, backend, OWNER);

			setUpStudyAndParticipant(List.of(study1, study2), List.of(participant1, participant2));
			setUpGlobalNotificationUserConfig(List.of(user1, user2), NotificationConfigGroup.STUDY_END_DATE_CONFIG,
					true);

			// when
			List<Participant> studyEndDateNotifiers = notificationQueryService.findStudyEndDateNotifier();
			// then
			assertThat(studyEndDateNotifiers).isEmpty();
		}

		@Test
		@DisplayName("[Success] 스터디 종료기간 알림 설정을 on한 알림 대상자만 조회")
		@Transactional
		void findNotifierWithConfigOn() {
			// given
			LocalDateTime fixedDateTime = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME;
			Study study1 = StudyFixture.STUDY1(user1, fixedDateTime,
					LocalDateTime.of(fixedDateTime.getYear(), fixedDateTime.getMonth(),
							fixedDateTime.getDayOfMonth() + 5, 0, 0, 0, 0));

			Study study2 = StudyFixture.STUDY2(user2, fixedDateTime,
					LocalDateTime.of(fixedDateTime.getYear(), fixedDateTime.getMonth(),
							fixedDateTime.getDayOfMonth() + 5, 11, 59, 59, 999999));

			Participant participant1OfStudy1 = createParticipant(study1, user1, backend, OWNER);
			Participant participant1OfStudy2 = createParticipant(study2, user2, backend, OWNER);
			setUpStudyAndParticipant(List.of(study1, study2), List.of(participant1OfStudy1, participant1OfStudy2));

			setUpGlobalNotificationUserConfig(List.of(user2), NotificationConfigGroup.STUDY_END_DATE_CONFIG, true);

			// when
			List<Participant> studyEndDateNotifiers = notificationQueryService.findStudyEndDateNotifier();

			// then
			assertThat(studyEndDateNotifiers).containsExactly(participant1OfStudy2);
		}

		private Participant createParticipant(Study study, User user, Position position, Role role) {
			Participant participant1OfStudy1 = Participant.from(study, user, position, role);
			study.addParticipant(participant1OfStudy1);
			return participant1OfStudy1;
		}

		private void setUpStudyAndParticipant(List<Study> studies, List<Participant> participants
		) {
			for (Study study : studies) {
				studyRepository.save(study);
			}
			participantRepository.saveAll(participants);
		}

	}

	@Nested
	@DisplayName("스터디 지원현황 알림 대상자 조회는")
	@Transactional
	class Describe_FindStudyApplicantNotifier {

		@Test
		@DisplayName("[Success] 지원자가 지원한 스터디의 모든 스터디원들을 알림 대상자로 조회")
		@Transactional
		void findNotifierStudyParticipants() {
			// given
			Study study = setUpStudyWithParticipants(
					StudyFixture.PROJECT_ONLINE_STUDY(user1, new FixedUtcDateTimePicker()),
					user1, List.of(user3));
			setUpGlobalNotificationUserConfig(List.of(user1, user3), NotificationConfigGroup.STUDY_APPLICANT_CONFIG,
					true);

			// when
			List<User> studyApplicantNotifiers = notificationQueryService.findStudyApplicantNotifier(study);

			// then
			assertThat(studyApplicantNotifiers).containsExactly(user1, user3);
		}

		@Test
		@DisplayName("[Success] 지원현황 알림 설정을 on한 알림 대상자만 조회")
		@Transactional
		void findNotifierWithStudyApplicantNotificationConfigOn() {
			// given
			Study study = setUpStudyWithParticipants(
					StudyFixture.PROJECT_ONLINE_STUDY(user1, new FixedUtcDateTimePicker()), user1,
					List.of(user2, user3));
			setUpGlobalNotificationUserConfig(List.of(user1, user3), NotificationConfigGroup.STUDY_APPLICANT_CONFIG,
					true);

			// when
			List<User> studyApplicantNotifier = notificationQueryService.findStudyApplicantNotifier(study);

			// then
			assertThat(studyApplicantNotifier).containsExactly(user1, user3);
		}

		private Study setUpStudyWithParticipants(Study study, User owner, List<User> members) {
			Participant owners = Participant.from(study, owner, backend, OWNER);
			study.addParticipant(owners);

			List<Participant> participants = members.stream()
					.map(member -> Participant.from(study, member, backend, Role.MEMBER))
					.toList();
			for (Participant participant : participants) {
				study.addParticipant(participant);
			}

			Study savedStudy = studyRepository.save(study);
			participantRepository.save(owners);
			participantRepository.saveAll(participants);
			return savedStudy;
		}

	}

	@Nested
	@DisplayName("스터디 리뷰 시작 알림 대상자 조회는")
	class Describe_FindStudyReviewStartNotifier {

		@Autowired
		ParticipantRepositoryImpl participantRepository;

		@Test
		@DisplayName("[Success] 현재 시각기준, 스터디 종료 기간이 어제인 모든 스터디들의 모든 스터디원들을 알림 대상자로 조회")
		@Transactional
		void findStudyReviewStartNotifier() {
			// given
			LocalDateTime today = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME;
			LocalDateTime yesterday = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME.minusDays(1L);

			Study study1 = StudyFixture.STUDY1(user1, today.minusDays(10L), yesterday);
			Participant study1Participant1 = Participant.from(study1, user1, backend, OWNER);
			Participant study1Participant2 = Participant.from(study1, user2, frontend, MEMBER);
			Participant study1Participant3 = Participant.from(study1, user3, frontend, MEMBER);
			registerStudyAndParticipants(study1, List.of(study1Participant1, study1Participant2, study1Participant3));

			Study study2 = StudyFixture.STUDY2(user2, today.minusDays(10L), yesterday);
			Participant study2Participant1 = Participant.from(study2, user2, frontend, OWNER);
			Participant study2Participant2 = Participant.from(study2, user3, frontend, MEMBER);
			Participant study2Participant3 = Participant.from(study2, user4, backend, MEMBER);
			registerStudyAndParticipants(study2, List.of(study2Participant1, study2Participant2, study2Participant3));

			setUpStudies(List.of(study1, study2));
			setUpParticipants(List.of(study1Participant1, study1Participant2, study1Participant3, study2Participant1,
					study2Participant2, study2Participant3));
			setUpGlobalNotificationUserConfig(List.of(user1, user2, user3, user4),
					NotificationConfigGroup.REVIEW_CONFIG, true);

			// when
			List<Participant> studyReviewStartNotifiers = notificationQueryService.findStudyReviewStartNotifier();

			// then
			assertThat(studyReviewStartNotifiers).containsExactly(study1Participant1, study1Participant2,
					study1Participant3, study2Participant1, study2Participant2, study2Participant3);
		}

		@Test
		@DisplayName("[Success] 스터디 리뷰 시작 알림 설정을 on한 알림 대상자만 조회")
		@Transactional
		void findNotifierWithReviewConfigIsTrue() {
			// given
			LocalDateTime today = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME;
			LocalDateTime yesterday = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME.minusDays(1L);

			Study study1 = StudyFixture.STUDY1(user1, today.minusDays(10L), yesterday);
			Participant study1Participant1 = Participant.from(study1, user1, backend, OWNER);
			registerStudyAndParticipants(study1, List.of(study1Participant1));

			Study study2 = StudyFixture.STUDY2(user2, today.minusDays(10L), yesterday);
			Participant study2Participant1 = Participant.from(study2, user2, frontend, OWNER);
			Participant study2Participant2 = Participant.from(study2, user3, frontend, MEMBER);
			registerStudyAndParticipants(study2, List.of(study2Participant1, study2Participant2));

			setUpStudies(List.of(study1, study2));
			setUpParticipants(List.of(study1Participant1, study2Participant1, study2Participant2));
			setUpGlobalNotificationUserConfig(List.of(user1, user3), NotificationConfigGroup.REVIEW_CONFIG, true);
			setUpGlobalNotificationUserConfig(List.of(user2), NotificationConfigGroup.REVIEW_CONFIG, false);

			// when
			List<Participant> studyReviewStartNotifiers = notificationQueryService.findStudyReviewStartNotifier();

			// then
			assertThat(studyReviewStartNotifiers).containsExactly(study1Participant1, study2Participant2);
		}

		private void registerStudyAndParticipants(Study study, List<Participant> participants) {
			for (Participant participant : participants) {
				study.addParticipant(participant);
			}
		}

		private void setUpStudies(List<Study> studies) {
			for (Study study : studies) {
				studyRepository.save(study);
			}
		}

		private void setUpParticipants(List<Participant> participants) {
			participantRepository.saveAll(participants);
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
		@Transactional
		void findNotifierPeerReviewFinish() {
			// given
			Study study = StudyFixture.PROJECT_ONLINE_STUDY(user1, new FixedUtcDateTimePicker());
			Review user1ToUser2Review = createReview(study, user1, user2);
			Review user2ToUser1Review = createReview(study, user2, user1);    // 상호 리뷰 완료
			Review user3ToUser4Review = createReview(study, user3, user4);    // 상호 리뷰 X
			setUpGlobalNotificationUserConfig(List.of(user1, user2), NotificationConfigGroup.REVIEW_CONFIG, true);

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
		@DisplayName("[Success] 상호 리뷰 완료한 스터디원 중 알림 설정을 on한 스터디원만 알림 대상자로 조회")
		@Transactional
		void findNotifierWithConfigOn() {
			// given
			Study study = StudyFixture.PROJECT_ONLINE_STUDY(user1, new FixedUtcDateTimePicker());
			Review user1ToUser2Review = createReview(study, user1, user2);
			Review user2ToUser1Review = createReview(study, user2, user1);    // 상호 리뷰 완료
			Review user3ToUser4Review = createReview(study, user3, user4);
			Review user4ToUser3Review = createReview(study, user4, user3);    // 상호 리뷰 완료
			setUpGlobalNotificationUserConfig(List.of(user2), NotificationConfigGroup.REVIEW_CONFIG, true);
			setUpGlobalNotificationUserConfig(List.of(user1, user3, user4), NotificationConfigGroup.REVIEW_CONFIG,
					false);

			studyRepository.save(study);
			reviewRepository.save(user1ToUser2Review);
			reviewRepository.save(user2ToUser1Review);
			reviewRepository.save(user3ToUser4Review);
			reviewRepository.save(user4ToUser3Review);

			// when
			List<User> reviewPeerFinishNotifiers1 = notificationQueryService.findReviewPeerFinishNotifier(study,
					user2ToUser1Review);
			List<User> reviewPeerFinishNotifiers2 = notificationQueryService.findReviewPeerFinishNotifier(study,
					user4ToUser3Review);

			// then
			assertThat(reviewPeerFinishNotifiers1).containsExactly(user2);
			assertThat(reviewPeerFinishNotifiers2).isEmpty();
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

	@Nested
	@DisplayName("알림 설정이 True인지 확인하는 메서드는")
	class Describe_IsNotificationConfigTrue {

		@ParameterizedTest
		@DisplayName("사용자가 설정한 GlobalNotificationUserConfig 의 config 컬럼이 true인지 확인")
		@MethodSource("getNotificationConfigGroup")
		@Transactional
		void checkGlobalNotificationConfigWithConfigGroup(NotificationConfigGroup notificationConfigGroup) {
			setUpGlobalNotificationUserConfig(List.of(user1), notificationConfigGroup, true);
			boolean result = notificationQueryService.isNotificationConfigTrue(user1, notificationConfigGroup);
			assertThat(result).isTrue();
		}

		static Stream<NotificationConfigGroup> getNotificationConfigGroup() {
			return Stream.of(NotificationConfigGroup.values());
		}
	}

	private void setUpGlobalNotificationUserConfig(List<User> users,
												   NotificationConfigGroup notificationConfigGroup,
												   boolean configOn
	) {
		for (User user : users) {
			GlobalNotificationUserConfig notificationUserConfig = GlobalNotificationUserConfig.of(user, false,
					false, false, false,
					false, false, false);
			notificationUserConfig.updateConfig(notificationConfigGroup, configOn);

			globalNotificationUserConfigRepository.save(notificationUserConfig);
		}
	}

}