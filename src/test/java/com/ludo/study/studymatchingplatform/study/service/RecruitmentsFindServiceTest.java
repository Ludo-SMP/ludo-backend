package com.ludo.study.studymatchingplatform.study.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.fixture.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.RecruitmentStackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.UserFixture;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponse;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RecruitmentsFindServiceTest {

	private static final int TOTAL_RECRUITMENT = 45;
	private static final int DEFAULT_PAGING_SIZE = 21;
	private static final int REMAIN_PAGING_SIZE = 3;

	@Autowired
	RecruitmentsFindService recruitmentsFindService;

	@Autowired
	RecruitmentRepositoryImpl recruitmentRepository;

	@Autowired
	StudyRepositoryImpl studyRepository;

	@Autowired
	UserRepositoryImpl userRepository;

	@Autowired
	CategoryRepositoryImpl categoryRepository;

	@BeforeEach
	void init() {
		User user = saveUser();
		Category category = saveCategory();
		saveRecruitments(category, user);
	}

	@Test
	@Transactional
	void 모집공고를_21개씩_조회한다() {
		List<RecruitmentPreviewResponse> firstSearchResults = recruitmentsFindService
				.findRecruitments(null, DEFAULT_PAGING_SIZE);
		assertThat(firstSearchResults).hasSize(DEFAULT_PAGING_SIZE);

		Long lastId = getLastId(firstSearchResults);
		List<RecruitmentPreviewResponse> secondSearchResults = recruitmentsFindService
				.findRecruitments(lastId, DEFAULT_PAGING_SIZE);
		assertThat(secondSearchResults).hasSize(DEFAULT_PAGING_SIZE);

		lastId = getLastId(secondSearchResults);
		List<RecruitmentPreviewResponse> thirdSearchResults = recruitmentsFindService
				.findRecruitments(lastId, DEFAULT_PAGING_SIZE);
		assertThat(thirdSearchResults).hasSize(REMAIN_PAGING_SIZE);
	}

	@Test
	@Transactional
	void 모집공고를_생성날짜기준_내림차순_조회한다() {
		List<RecruitmentPreviewResponse> firstSearchResults = recruitmentsFindService
				.findRecruitments(null, DEFAULT_PAGING_SIZE);
		assertThat(firstSearchResults)
				.extracting("title")
				.containsExactly(
						"모집공고45", "모집공고44", "모집공고43", "모집공고42", "모집공고41",
						"모집공고40", "모집공고39", "모집공고38", "모집공고37", "모집공고36",
						"모집공고35", "모집공고34", "모집공고33", "모집공고32", "모집공고31",
						"모집공고30", "모집공고29", "모집공고28", "모집공고27", "모집공고26", "모집공고25");

		Long lastId = getLastId(firstSearchResults);
		List<RecruitmentPreviewResponse> secondSearchResults = recruitmentsFindService
				.findRecruitments(lastId, DEFAULT_PAGING_SIZE);
		assertThat(secondSearchResults)
				.extracting("title")
				.containsExactly(
						"모집공고24", "모집공고23", "모집공고22", "모집공고21", "모집공고20",
						"모집공고19", "모집공고18", "모집공고17", "모집공고16", "모집공고15",
						"모집공고14", "모집공고13", "모집공고12", "모집공고11", "모집공고10",
						"모집공고9", "모집공고8", "모집공고7", "모집공고6", "모집공고5", "모집공고4");

		lastId = getLastId(secondSearchResults);
		List<RecruitmentPreviewResponse> thirdSearchResults = recruitmentsFindService.findRecruitments(lastId, 20);
		assertThat(thirdSearchResults)
				.extracting("title")
				.containsExactly("모집공고3", "모집공고2", "모집공고1");
	}

	private Long getLastId(List<RecruitmentPreviewResponse> searchResults) {
		return searchResults.get(searchResults.size() - 1).id();
	}

	private User saveUser() {
		User user = UserFixture.createUser(Social.GOOGLE, "아카", "archa@gmail.com");
		return userRepository.save(user);
	}

	private Category saveCategory() {
		Category project = CategoryFixture.createCategory("프로젝트");
		return categoryRepository.save(project);
	}

	private void saveRecruitments(Category category, User user) {
		for (int count = 1; count <= TOTAL_RECRUITMENT; count++) {
			Study study = saveStudy(category, user, count);
			RecruitmentStack spring = createRecruitmentStack();
			saveRecruitment(count, study, spring);
		}
	}

	private Study saveStudy(Category category, User user, int count) {
		String studyTitle = "스터디" + count;
		Study study = StudyFixture.createStudy(StudyStatus.RECRUITING, studyTitle, Way.ONLINE, category, user);
		studyRepository.save(study);
		return study;
	}

	private RecruitmentStack createRecruitmentStack() {
		return RecruitmentStackFixture.createRecruitmentStack(
				StackFixture.createStack("spring")
		);
	}

	private void saveRecruitment(int count, Study study, RecruitmentStack spring) {
		String recruitmentTitle = "모집공고" + count;
		Recruitment recruitment = RecruitmentFixture.createRecruitment(study, recruitmentTitle, "내용", 1, "call",
				spring);
		recruitmentRepository.save(recruitment);
	}

	@SpringBootTest
	@Transactional
	static
	class RecruitmentServiceTestBakup {

		@Autowired
		private UserRepositoryImpl userRepository;

		@Autowired
		private CategoryRepositoryImpl categoryRepository;

		@Autowired
		private StudyRepositoryImpl studyRepository;

		@Autowired
		private PositionRepositoryImpl positionRepository;

		@Autowired
		private StackRepositoryImpl stackRepository;

		@Autowired
		private StackCategoryRepositoryImpl stackCategoryRepository;

		@Autowired
		private RecruitmentRepositoryImpl recruitmentRepository;

		@Autowired
		private RecruitmentService recruitmentService;

		@DisplayName("스터디장은 모집 공고를 작성할 수 있다")
		@Test
		void writeRecruitment() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build());

			final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final StackCategory stackCategory = stackCategoryRepository.save(
				StackCategoryFixture.createStackCategory("stackCategory")
			);

			final Stack stack = stackRepository.save(
				StackFixture.createStack("stack", stackCategory)
			);
			final Position position = positionRepository.save(
				PositionFixture.createPosition("position")
			);

			final WriteRecruitmentRequest request = WriteRecruitmentRequest.builder()
				.studyId(study.getId())
				.title("recruitment")
				.content("I want to study")
				.stackIds(Set.of(stack.getId()))
				.positionIds(Set.of(position.getId()))
				.recruitmentLimit(4)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

			// when
			final Recruitment recruitment = recruitmentService.write(owner, request);

			// then
			Assertions.assertThat(recruitment.getTitle()).isEqualTo("recruitment");
			Assertions.assertThat(recruitment.getContent()).isEqualTo("I want to study");
			Assertions.assertThat(recruitment.getCallUrl()).isEqualTo("x.com");

		}

		@DisplayName("스터디장이 아니면 모집 공고를 작성할 수 없다")
		@Test
		void writeRecruitmentFailure() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build());
			final User user = userRepository.save(
				User.builder()
					.nickname("user")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build());

			final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final StackCategory stackCategory = stackCategoryRepository.save(
				StackCategoryFixture.createStackCategory("stackCategory")
			);

			final Stack stack = stackRepository.save(
				StackFixture.createStack("stack", stackCategory)
			);
			final Position position = positionRepository.save(
				PositionFixture.createPosition("position")
			);

			final WriteRecruitmentRequest request = WriteRecruitmentRequest.builder()
				.studyId(study.getId())
				.title("recruitment")
				.content("I want to study")
				.stackIds(Set.of(stack.getId()))
				.positionIds(Set.of(position.getId()))
				.recruitmentLimit(4)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

			// when
			// then
			assertThatThrownBy(() ->
				recruitmentService.write(user, request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("모집 공고를 작성할 권한이 없습니다.");
		}

		@DisplayName("스터디에 이미 작성된 모집 공고가 존재 하면 모집 공고를 작성할 수 없다")
		@Test
		void writeDuplicateRecruitmentFailure() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build());
			final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final StackCategory stackCategory = stackCategoryRepository.save(
				StackCategoryFixture.createStackCategory("stackCategory")
			);

			final Stack stack = stackRepository.save(
				StackFixture.createStack("stack", stackCategory)
			);
			final Position position = positionRepository.save(
				PositionFixture.createPosition("position")
			);

			final WriteRecruitmentRequest request = WriteRecruitmentRequest.builder()
				.studyId(study.getId())
				.title("recruitment")
				.content("I want to study")
				.stackIds(Set.of(stack.getId()))
				.positionIds(Set.of(position.getId()))
				.recruitmentLimit(4)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

			final Recruitment recruitment = recruitmentService.write(owner, request);
			study.registerRecruitment(recruitment);

			// when
			// then
			assertThatThrownBy(() ->
				recruitmentService.write(owner, request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("이미 작성된 모집 공고가 존재합니다.");
		}

		@DisplayName("스터디가 존재하지 않으면 모집 공고를 작성할 수 없다")
		@Test
		void writeRecruitmentFailureWithoutStudy() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build());

			final StackCategory stackCategory = stackCategoryRepository.save(
				StackCategoryFixture.createStackCategory("stackCategory")
			);

			final Stack stack = stackRepository.save(
				StackFixture.createStack("stack", stackCategory)
			);
			final Position position = positionRepository.save(
				PositionFixture.createPosition("position")
			);

			final WriteRecruitmentRequest request = WriteRecruitmentRequest.builder()
				.studyId(2147483647L)
				.title("recruitment")
				.content("I want to study")
				.stackIds(Set.of(stack.getId()))
				.positionIds(Set.of(position.getId()))
				.recruitmentLimit(4)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

			// when
			// then
			assertThatThrownBy(() ->
				recruitmentService.write(owner, request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("존재하지 않는 스터디입니다.");
		}

		@DisplayName("스터디에 지원한 적 없는 사용자는 스터디에 지원할 수 있다")
		@Test
		void applyRecruitment() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final User applier = userRepository.save(
				User.builder()
					.nickname("applier")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);

			// when
			final Applicant applicant = recruitmentService.apply(applier, recruitment.getId());

			// then
			Assertions.assertThat(applicant.getUser()).isEqualTo(applier);
		}

		@DisplayName("이미 지원한 모집 공고에 지원할 수 없다")
		@Test
		void applyRecruitmentFailIfAlreadyApplied() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final User applier = userRepository.save(
				User.builder()
					.nickname("applier")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);
			recruitmentService.apply(applier, recruitment.getId());

			// when
			// then
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("이미 지원한 모집 공고입니다.");
		}

		@DisplayName("모집 중이 아닌 스터디의 모집 공고에 지원할 수 없다")
		@Test
		void applyRecruitmentFailIfNotRecruiting() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final User applier = userRepository.save(
				User.builder()
					.nickname("applier")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);

			// when
			// then
			study.changeStatus(StudyStatus.RECRUITED);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

			study.changeStatus(StudyStatus.PROGRESS);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

			study.changeStatus(StudyStatus.COMPLETED);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");
		}

		@DisplayName("지원 취소한 모집 공고에 다시 지원할 수 있다")
		@Test
		void applyRecruitmentIfCancelled() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final User applier = userRepository.save(
				User.builder()
					.nickname("applier")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);
			recruitmentService.apply(applier, recruitment.getId());
			recruitmentService.cancel(applier, recruitment.getId());

			// when
			// then
			recruitmentService.apply(applier, recruitment.getId());
		}

		@DisplayName("지원 취소 상태가 아닌 경우, 모집 공고에 다시 지원할 수 없다")
		@Test
		void applyRecruitmentIfNotCancelled() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final User applier = userRepository.save(
				User.builder()
					.nickname("applier")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);
			final Applicant applicant = recruitmentService.apply(applier, recruitment.getId());

			// when
			// then
			applicant.changeStatus(ApplicantStatus.ACCEPTED);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("이미 수락된 모집 공고입니다.");

			applicant.changeStatus(ApplicantStatus.REJECTED);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("이미 거절된 모집 공고입니다.");

		}

		@DisplayName("모집 공고 지원 후 UNCHECKED 상태인 경우 지원 취소 가능")
		@Test
		void cancelApplicant() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final User applier = userRepository.save(
				User.builder()
					.nickname("applier")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);
			final Applicant applicant = recruitmentService.apply(applier, recruitment.getId());

			// when
			// then
			applicant.changeStatus(ApplicantStatus.UNCHECKED);
			final Applicant cancelledApplier = recruitmentService.cancel(applier, recruitment.getId());

			Assertions.assertThat(cancelledApplier.getApplicantStatus()).isEqualTo(ApplicantStatus.CANCELLED);

		}

		@DisplayName("모집 공고 지원 후 ACCEPTED 상태이며, 스터디가 모집 중인 경우 지원 취소 가능")
		@Test
		void cancelFailureIfAcceptedAndRecruiting() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final User applier = userRepository.save(
				User.builder()
					.nickname("applier")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);
			study.changeStatus(StudyStatus.RECRUITING);

			final Applicant applicant = recruitmentService.apply(applier, recruitment.getId());

			// when
			// then
			applicant.changeStatus(ApplicantStatus.ACCEPTED);
			final Applicant cancelledApplier = recruitmentService.cancel(applier, recruitment.getId());

			Assertions.assertThat(cancelledApplier.getApplicantStatus()).isEqualTo(ApplicantStatus.CANCELLED);

		}

		@DisplayName("모집 중이 아닌 스터디의 모집 공고는 취소할 수 없다.")
		@Test
		void cancelFailureIfNotRecruiting() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final User applier = userRepository.save(
				User.builder()
					.nickname("applier")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);
			final Applicant applicant = recruitmentService.apply(applier, recruitment.getId());

			study.changeStatus(StudyStatus.RECRUITED);

			// when
			// then
			applicant.changeStatus(ApplicantStatus.UNCHECKED);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

			applicant.changeStatus(ApplicantStatus.CANCELLED);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

			applicant.changeStatus(ApplicantStatus.REJECTED);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

			applicant.changeStatus(ApplicantStatus.REMOVED);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

			applicant.changeStatus(ApplicantStatus.ACCEPTED);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

		}

		@DisplayName("거절된 모집 공고는 지원 취소할 수 없다.")
		@Test
		void cancelFailureIfAlreadyRejected() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final User applier = userRepository.save(
				User.builder()
					.nickname("applier")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);
			final Applicant applicant = recruitmentService.apply(applier, recruitment.getId());

			// when
			// then
			applicant.changeStatus(ApplicantStatus.REJECTED);
			assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId()))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("이미 거절된 모집 공고입니다.");

		}

		@DisplayName("스터디장은 모집 공고를 수정할 수 있다.")
		@Test
		void editRecruitmentSuccess() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);

			// when
			// then
			final LocalDateTime editedRecruitmentEndDateTime = LocalDateTime.now().plusDays(5);
			final EditRecruitmentRequest request = EditRecruitmentRequest.builder()
				.title("edited text")
				// .content("edited content")
				.callUrl("edited callUrl")
				.recruitmentEndDateTime(editedRecruitmentEndDateTime)
				.build();
			final Recruitment editedRecruitment = recruitmentService.edit(owner, recruitment.getId(), request);

			// edited states
			Assertions.assertThat(editedRecruitment.getTitle()).isEqualTo("edited text");
			Assertions.assertThat(editedRecruitment.getCallUrl()).isEqualTo("edited callUrl");
			Assertions.assertThat(editedRecruitment.getRecruitmentEndDateTime()).isEqualTo(editedRecruitmentEndDateTime);

			// same as prev states
			Assertions.assertThat(editedRecruitment.getId()).isEqualTo(recruitment.getId());
			Assertions.assertThat(editedRecruitment.getContent()).isEqualTo(recruitment.getContent());
			Assertions.assertThat(editedRecruitment.getHits()).isEqualTo(recruitment.getHits());
		}

		@DisplayName("스터디장이 아니면 모집 공고를 수정할 수 없다.")
		@Test
		void editRecruitmentFailure() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);
			final User user = userRepository.save(
				User.builder()
					.nickname("user")
					.email("b@bb.bb")
					.social(Social.KAKAO)
					.build()
			);
			final Category category = categoryRepository.save(

				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);

			// when
			// then
			final LocalDateTime editedRecruitmentEndDateTime = LocalDateTime.now().plusDays(5);
			final EditRecruitmentRequest request = EditRecruitmentRequest.builder()
				.title("edited text")
				.content("edited content")
				.callUrl("edited callUrl")
				.recruitmentEndDateTime(editedRecruitmentEndDateTime)
				.build();

			assertThatThrownBy(() -> recruitmentService.edit(user, recruitment.getId(), request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("모집 공고를 작성할 권한이 없습니다.");

		}

		@DisplayName("존재하지 않는 모집 공고는 수정할 수 없다.")
		@Test
		void editRecruitmentFailureIfNotExists() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build()
			);

			final LocalDateTime editedRecruitmentEndDateTime = LocalDateTime.now().plusDays(5);
			final EditRecruitmentRequest request = EditRecruitmentRequest.builder()
				.title("edited text")
				.content("edited content")
				.callUrl("edited callUrl")
				.recruitmentEndDateTime(editedRecruitmentEndDateTime)
				.build();

			final Long invalidRecruitmentId = Long.MIN_VALUE;
			// when
			// then
			assertThatThrownBy(() -> recruitmentService.edit(owner, invalidRecruitmentId, request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("존재하지 않는 모집 공고입니다.");

		}

		@DisplayName("")
		@Test
		void updateRecruitmentStackSuccess3() {

			// given
			final User owner = userRepository.save(
				User.builder()
					.nickname("owner")
					.email("a@aa.aa")
					.social(Social.KAKAO)
					.build());

			final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

			final Study study = studyRepository.save(
				StudyFixture.createStudy(
					"study",
					category,
					owner,
					4
				)
			);
			final StackCategory stackCategory = stackCategoryRepository.save(
				StackCategoryFixture.createStackCategory("stackCategory")
			);

			final Stack stack1 = stackRepository.save(
				StackFixture.createStack("stack1", stackCategory)
			);
			final Stack stack2 = stackRepository.save(
				StackFixture.createStack("stack2", stackCategory)
			);
			final Stack stack3 = stackRepository.save(
				StackFixture.createStack("stack3", stackCategory)
			);
			final Position position = positionRepository.save(
				PositionFixture.createPosition("position")
			);
			final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
					study,
					"recruitment",
					"content",
					"callUrl",
					LocalDateTime.now().plusDays(5)
				)
			);
			study.registerRecruitment(recruitment);

			final EditRecruitmentRequest request = EditRecruitmentRequest.builder()
				.title("recruitment")
				.content("I want to study")
				.stackIds(Set.of(stack1.getId()))
				.positionIds(Set.of(position.getId()))
				.recruitmentLimit(4)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

			// when
			recruitmentService.edit(owner, recruitment.getId(), request);
			final List<RecruitmentStack> recruitmentStacks = recruitment.getRecruitmentStacks();

			// then
			Assertions.assertThat(recruitmentStacks).hasSize(1);
			Assertions.assertThat(recruitmentStacks).extracting("stack")
				.containsAll(List.of(stack1));

		}


	}
}