package com.ludo.study.studymatchingplatform.study.service.recruitment;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.applicant.ApplyRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.EditRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

@SpringBootTest
@Transactional
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

	Position createPosition() {
		return PositionFixture.createPosition("position");
	}

	ApplyRecruitmentRequest createRequest() {
		return ApplyRecruitmentRequest.builder()
				.positionId(1L)
				.build();
	}

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
						4,
						Platform.GATHER
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
				.applicantCount(4)
				.contect(Contact.KAKAO)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

		// when
		final Recruitment recruitment = recruitmentService.write(owner, request);

		// then
		assertThat(recruitment.getTitle()).isEqualTo("recruitment");
		assertThat(recruitment.getContent()).isEqualTo("I want to study");
		assertThat(recruitment.getCallUrl()).isEqualTo("x.com");

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
						4,
						Platform.GATHER
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
				.applicantCount(4)
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
						4,
						Platform.GATHER
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
				.applicantCount(4)
				.contect(Contact.KAKAO)
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
				.applicantCount(4)
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
						4,
						Platform.GATHER
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

		// 포지션 생성
		final Position position1 = createPosition();
		final ApplyRecruitmentRequest request = createRequest();

		// when
		final Applicant applicant = recruitmentService.apply(applier, recruitment.getId(), request);

		// then
		assertThat(applicant.getUser()).isEqualTo(applier);
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
						4,
						Platform.GATHER
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

		// 포지션 생성
		final Position position1 = createPosition();
		final ApplyRecruitmentRequest request = createRequest();

		study.registerRecruitment(recruitment);
		recruitmentService.apply(applier, recruitment.getId(), request);

		// when
		// then
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
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
						4,
						Platform.GATHER
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

		final Position position1 = createPosition();
		final ApplyRecruitmentRequest request = createRequest();

		// when
		// then
		study.changeStatus(StudyStatus.RECRUITED);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

		study.changeStatus(StudyStatus.PROGRESS);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

		study.changeStatus(StudyStatus.COMPLETED);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
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
						4,
						Platform.GATHER
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

		final Position position1 = createPosition();
		final ApplyRecruitmentRequest request = createRequest();

		recruitmentService.apply(applier, recruitment.getId(), request);
		recruitmentService.cancel(applier, recruitment.getId());

		// when
		// then
		recruitmentService.apply(applier, recruitment.getId(), request);
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
						4,
						Platform.GATHER
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

		final Position position1 = createPosition();
		final ApplyRecruitmentRequest request = createRequest();

		final Applicant applicant = recruitmentService.apply(applier, recruitment.getId(), request);

		// when
		// then
		applicant.changeStatus(ApplicantStatus.ACCEPTED);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("이미 수락된 모집 공고입니다.");

		applicant.changeStatus(ApplicantStatus.REFUSED);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
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
						4,
						Platform.GATHER
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

		final Position position1 = createPosition();
		final ApplyRecruitmentRequest request = createRequest();

		final Applicant applicant = recruitmentService.apply(applier, recruitment.getId(), request);

		// when
		// then
		applicant.changeStatus(ApplicantStatus.UNCHECKED);
		final Applicant cancelledApplier = recruitmentService.cancel(applier, recruitment.getId());

		assertThat(cancelledApplier.getApplicantStatus()).isEqualTo(ApplicantStatus.CANCELLED);

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
						4,
						Platform.GATHER
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

		final Position position1 = createPosition();
		final ApplyRecruitmentRequest request = createRequest();

		final Applicant applicant = recruitmentService.apply(applier, recruitment.getId(), request);

		// when
		// then
		applicant.changeStatus(ApplicantStatus.ACCEPTED);
		final Applicant cancelledApplier = recruitmentService.cancel(applier, recruitment.getId());

		assertThat(cancelledApplier.getApplicantStatus()).isEqualTo(ApplicantStatus.CANCELLED);

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
						4,
						Platform.GATHER
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

		final Position position1 = createPosition();
		final ApplyRecruitmentRequest request = createRequest();

		final Applicant applicant = recruitmentService.apply(applier, recruitment.getId(), request);

		study.changeStatus(StudyStatus.RECRUITED);

		// when
		// then
		applicant.changeStatus(ApplicantStatus.UNCHECKED);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

		applicant.changeStatus(ApplicantStatus.CANCELLED);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

		applicant.changeStatus(ApplicantStatus.REFUSED);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

		applicant.changeStatus(ApplicantStatus.REMOVED);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 모집 중인 스터디가 아닙니다.");

		applicant.changeStatus(ApplicantStatus.ACCEPTED);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
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
						4,
						Platform.GATHER
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

		final Position position1 = createPosition();
		final ApplyRecruitmentRequest request = createRequest();

		final Applicant applicant = recruitmentService.apply(applier, recruitment.getId(), request);

		// when
		// then
		applicant.changeStatus(ApplicantStatus.REFUSED);
		assertThatThrownBy(() -> recruitmentService.apply(applier, recruitment.getId(), request))
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
						4,
						Platform.GATHER
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
		assertThat(editedRecruitment.getTitle()).isEqualTo("edited text");
		assertThat(editedRecruitment.getCallUrl()).isEqualTo("edited callUrl");
		assertThat(editedRecruitment.getRecruitmentEndDateTime()).isEqualTo(editedRecruitmentEndDateTime);

		// same as prev states
		assertThat(editedRecruitment.getId()).isEqualTo(recruitment.getId());
		assertThat(editedRecruitment.getContent()).isEqualTo(recruitment.getContent());
		assertThat(editedRecruitment.getHits()).isEqualTo(recruitment.getHits());
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
						4,
						Platform.GATHER
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

}
