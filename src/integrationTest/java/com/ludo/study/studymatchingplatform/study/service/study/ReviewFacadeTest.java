package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
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
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteReviewRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.ReviewResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.InvalidReviewPeriodException;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentService;
import com.ludo.study.studymatchingplatform.study.service.recruitment.applicant.StudyApplicantDecisionService;
import com.ludo.study.studymatchingplatform.study.service.study.participant.ParticipantService;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ReviewFacadeTest {

    @Autowired
    private ReviewService reviewService;

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

    @Autowired
    private ParticipantRepositoryImpl participantRepository;

    @Autowired
    private StudyApplicantDecisionService studyApplicantDecisionService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private EntityManager em;

    @Autowired
    private ReviewFacade reviewFacade;

    // FIXME: mocking
//
//    @DisplayName("[Exception] 이미 리뷰를 작성한 스터디원에게는 재작성이 불가능하다.")
//    @Test
//    void writeDuplicateReview() {
//        // given
//        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;
//        final LocalDateTime endDateTime = now.minusDays(3);
//
//        final User reviewer = User.builder()
//                .id(1L)
//                .nickname("reviewer")
//                .email("reviewer@aa.aa")
//                .build();
//        final User reviewee = User.builder()
//                .id(2L)
//                .nickname("reviewee")
//                .email("reviewee@aa.aa")
//                .build();
//        final Study study = Study.builder()
//                .owner(reviewer)
//                .endDateTime(endDateTime)
//                .build();
//        final Position position = Position.builder()
//                .name("position")
//                .build();
//        study.addParticipant(reviewer, position, Role.OWNER);
//        study.addParticipant(reviewee, position, Role.MEMBER);
//
//        final WriteReviewRequest request = WriteReviewRequest.builder()
//                .revieweeId(reviewee.getId())
//                .activenessScore(5L)
//                .professionalismScore(5L)
//                .communicationScore(5L)
//                .togetherScore(5L)
//                .recommendScore(5L)
//                .build();
//
//        // when
//        // then
//        reviewFacade.write(request, study.getId(), userA);
//        assertThatThrownBy(() -> reviewFacade.write(request, study.getId(), userA))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("이미 리뷰를 작성 하셨습니다.");
//
//
//    }
//
//    @DisplayName("[Exception] 존재하지 않는 스터디의 리뷰 작성은 불가능하다.")
//    @Test
//    void writeReviewForStudyNotExist() {
//        final User userA = userRepository.save(
//                User.builder()
//                        .nickname("userA")
//                        .email("userA@aa.aa")
//                        .social(Social.KAKAO)
//                        .build());
//        final User userB = userRepository.save(
//                User.builder()
//                        .nickname("userB")
//                        .email("userB@aa.aa")
//                        .social(Social.KAKAO)
//                        .build());
//        final WriteReviewRequest request = WriteReviewRequest.builder()
//                .revieweeId(userB.getId())
//                .activenessScore(5L)
//                .professionalismScore(5L)
//                .communicationScore(5L)
//                .togetherScore(5L)
//                .recommendScore(5L)
//                .build();
//
//        assertThatThrownBy(() -> reviewFacade.write(request, 2147483647L, userA))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("존재하지 않는 스터디입니다. 리뷰를 작성할 수 없습니다.");
//    }

    @DisplayName("[Success] 스터디가 종료된지 3일 ~ 14일 내에 리뷰를 작성할 수 있다.")
    @ParameterizedTest
    @MethodSource("validEndDateTimes")
    void writeReview(final LocalDateTime endDateTime) {

        // given
        final User owner = userRepository.save(
                User.builder()
                        .nickname("owner")
                        .email("a@aa.aa")
                        .social(Social.KAKAO)
                        .build());

        final Category category = categoryRepository.save(
                CategoryFixture.createCategory("category1"));

        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;

        final LocalDateTime startDateTime = now.minusDays(20);

        final Study study = saveStudy(category, owner, startDateTime, endDateTime);
        final StackCategory stackCategory = stackCategoryRepository.save(
                StackCategoryFixture.createStackCategory("stackCategory")
        );

        final Stack stack = stackRepository.save(
                StackFixture.createStack("stack", stackCategory)
        );
        final Position position = positionRepository.save(
                PositionFixture.createPosition("position")
        );

        final User userA = userRepository.save(
                User.builder()
                        .nickname("userA")
                        .email("userA@aa.aa")
                        .social(Social.KAKAO)
                        .build());
        final User userB = userRepository.save(
                User.builder()
                        .nickname("userB")
                        .email("userB@aa.aa")
                        .social(Social.KAKAO)
                        .build());


        participantService.add(study, userA, position, Role.MEMBER);
        participantService.add(study, userB, position, Role.MEMBER);

        final Participant participantB = study.getParticipant(userB);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(participantB.getUser().getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();


        // when
        final ReviewResponse review = reviewFacade.write(request, study.getId(), userA);

        // then
        assertThat(review.reviewer().email())
                .isEqualTo(userA.getEmail());

    }

    private static Stream<LocalDateTime> validEndDateTimes() {
        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;
        return Stream.of(
                now.minusDays(3),
                now.minusDays(13),
                now.minusDays(14)
        );

    }


    @DisplayName("[Exception] 스터디가 종료된지 3일 ~ 14일 내가 아니라면 리뷰를 작성할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidEndDateTimes")
    void writeReviewFailure(final LocalDateTime endDateTime) {

        // given
        final User owner = userRepository.save(
                User.builder()
                        .nickname("owner")
                        .email("a@aa.aa")
                        .social(Social.KAKAO)
                        .build());

        final Category category = categoryRepository.save(
                CategoryFixture.createCategory("category1"));

        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;
        final LocalDateTime startDateTime = now.minusDays(20);

        final Study study = saveStudy(category, owner, startDateTime, endDateTime);
        final StackCategory stackCategory = stackCategoryRepository.save(
                StackCategoryFixture.createStackCategory("stackCategory")
        );

        final Stack stack = stackRepository.save(
                StackFixture.createStack("stack", stackCategory)
        );
        final Position position = positionRepository.save(
                PositionFixture.createPosition("position")
        );

        final User userA = userRepository.save(
                User.builder()
                        .nickname("userA")
                        .email("userA@aa.aa")
                        .social(Social.KAKAO)
                        .build());
        final User userB = userRepository.save(
                User.builder()
                        .nickname("userB")
                        .email("userB@aa.aa")
                        .social(Social.KAKAO)
                        .build());


        participantService.add(study, userA, position, Role.MEMBER);
        participantService.add(study, userB, position, Role.MEMBER);

        final Participant participantB = study.getParticipant(userB);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(participantB.getUser().getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> reviewFacade.write(request, study.getId(), userA))
                .isInstanceOf(InvalidReviewPeriodException.class);
    }

    @DisplayName("[Exception] 스터디가 종료된 후 3일이 지나기 전에는 리뷰를 작성할 수 없으며, 사용자에게 이를 인지하기 위한 적절한 안내 메세지를 보여준다.")
    @Test
    void writeReviewFailureBeforeThreeDaysAfterStudyDone() {

        // given
        final User owner = userRepository.save(
                User.builder()
                        .nickname("owner")
                        .email("a@aa.aa")
                        .social(Social.KAKAO)
                        .build());

        final Category category = categoryRepository.save(
                CategoryFixture.createCategory("category1"));

        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;
        final LocalDateTime startDateTime = now.minusDays(20);
        final LocalDateTime endDateTime = now.minusDays(3).plusSeconds(1);

        final Study study = saveStudy(category, owner, startDateTime, endDateTime);
        final StackCategory stackCategory = stackCategoryRepository.save(
                StackCategoryFixture.createStackCategory("stackCategory")
        );

        final Stack stack = stackRepository.save(
                StackFixture.createStack("stack", stackCategory)
        );
        final Position position = positionRepository.save(
                PositionFixture.createPosition("position")
        );

        final User userA = userRepository.save(
                User.builder()
                        .nickname("userA")
                        .email("userA@aa.aa")
                        .social(Social.KAKAO)
                        .build());
        final User userB = userRepository.save(
                User.builder()
                        .nickname("userB")
                        .email("userB@aa.aa")
                        .social(Social.KAKAO)
                        .build());


        participantService.add(study, userA, position, Role.MEMBER);
        participantService.add(study, userB, position, Role.MEMBER);

        final Participant participantB = study.getParticipant(userB);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(participantB.getUser().getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> reviewFacade.write(request, study.getId(), userA))
                .isInstanceOf(InvalidReviewPeriodException.class)
                .hasMessage("아직 리뷰 작성 기간이 되지 않았습니다.\n리뷰 작성은 스터디 완료 3일 후인 2000년 01월 01일 00시 00분부터 14일 후인 2000년 01월 12일 00시 00분까지 가능합니다.");

    }

    @DisplayName("[Exception] 스터디가 종료된 후 14일이 지난 후에는 리뷰를 작성할 수 없으며, 사용자에게 이를 인지하기 위한 적절한 안내 메세지를 보여준다.")
    @Test
    void writeReviewFailureAfterFourteenDaysAfterStudyDone() {

        // given
        final User owner = userRepository.save(
                User.builder()
                        .nickname("owner")
                        .email("a@aa.aa")
                        .social(Social.KAKAO)
                        .build());

        final Category category = categoryRepository.save(
                CategoryFixture.createCategory("category1"));

        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;
        final LocalDateTime startDateTime = now.minusDays(20);
        final LocalDateTime endDateTime = now.minusDays(14).minusSeconds(1);

        final Study study = saveStudy(category, owner, startDateTime, endDateTime);
        final StackCategory stackCategory = stackCategoryRepository.save(
                StackCategoryFixture.createStackCategory("stackCategory")
        );

        final Stack stack = stackRepository.save(
                StackFixture.createStack("stack", stackCategory)
        );
        final Position position = positionRepository.save(
                PositionFixture.createPosition("position")
        );

        final User userA = userRepository.save(
                User.builder()
                        .nickname("userA")
                        .email("userA@aa.aa")
                        .social(Social.KAKAO)
                        .build());
        final User userB = userRepository.save(
                User.builder()
                        .nickname("userB")
                        .email("userB@aa.aa")
                        .social(Social.KAKAO)
                        .build());


        participantService.add(study, userA, position, Role.MEMBER);
        participantService.add(study, userB, position, Role.MEMBER);

        final Participant participantB = study.getParticipant(userB);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(participantB.getUser().getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> reviewFacade.write(request, study.getId(), userA))
                .isInstanceOf(InvalidReviewPeriodException.class)
                .hasMessage("리뷰 작성 기간이 지났습니다.\n리뷰 작성은 스터디 완료 3일 후인 1999년 12월 20일 23시 59분부터 14일 후인 2000년 01월 01일 00시 00분까지 가능합니다.");

    }

    private static Stream<LocalDateTime> invalidEndDateTimes() {
        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;
        return Stream.of(
                now.minusDays(3).plusSeconds(1),
                now.minusDays(14).minusSeconds(1)
        );

    }

    @DisplayName("[Exception] 이미 리뷰를 작성한 스터디원에게는 재작성이 불가능하다.")
    @Test
    void writeDuplicateReview() {

        // given
        final User owner = userRepository.save(
                User.builder()
                        .nickname("owner")
                        .email("a@aa.aa")
                        .social(Social.KAKAO)
                        .build());

        final Category category = categoryRepository.save(
                CategoryFixture.createCategory("category1"));

        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;
        final LocalDateTime startDateTime = now.minusDays(20);
        final LocalDateTime endDateTime = now.minusDays(3);

        final Study study = saveStudy(category, owner, startDateTime, endDateTime);
        final StackCategory stackCategory = stackCategoryRepository.save(
                StackCategoryFixture.createStackCategory("stackCategory")
        );

        final Stack stack = stackRepository.save(
                StackFixture.createStack("stack", stackCategory)
        );
        final Position position = positionRepository.save(
                PositionFixture.createPosition("position")
        );

        final User userA = userRepository.save(
                User.builder()
                        .nickname("userA")
                        .email("userA@aa.aa")
                        .social(Social.KAKAO)
                        .build());
        final User userB = userRepository.save(
                User.builder()
                        .nickname("userB")
                        .email("userB@aa.aa")
                        .social(Social.KAKAO)
                        .build());


        participantService.add(study, userA, position, Role.MEMBER);
        participantService.add(study, userB, position, Role.MEMBER);

        final Participant participantB = study.getParticipant(userB);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(participantB.getUser().getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        reviewFacade.write(request, study.getId(), userA);
        assertThatThrownBy(() -> reviewFacade.write(request, study.getId(), userA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 리뷰를 작성 하셨습니다.");


    }

    @DisplayName("[Exception] 존재하지 않는 스터디의 리뷰 작성은 불가능하다.")
    @Test
    void writeReviewForStudyNotExist() {
        final User userA = userRepository.save(
                User.builder()
                        .nickname("userA")
                        .email("userA@aa.aa")
                        .social(Social.KAKAO)
                        .build());
        final User userB = userRepository.save(
                User.builder()
                        .nickname("userB")
                        .email("userB@aa.aa")
                        .social(Social.KAKAO)
                        .build());
        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(userB.getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        assertThatThrownBy(() -> reviewFacade.write(request, 2147483647L, userA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 스터디입니다. 리뷰를 작성할 수 없습니다.");
    }

    @DisplayName("[Exception] Reviewee가 스터디원이 아닌 경우, 리뷰 작성이 불가능하다.")
    @Test
    void writeReviewToNonParticipants() {

        // given
        final User owner = userRepository.save(
                User.builder()
                        .nickname("owner")
                        .email("a@aa.aa")
                        .social(Social.KAKAO)
                        .build());

        final Category category = categoryRepository.save(
                CategoryFixture.createCategory("category1"));

        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;
        final LocalDateTime startDateTime = now.minusDays(20);
        final LocalDateTime endDateTime = now.minusDays(3);

        final Study study = saveStudy(category, owner, startDateTime, endDateTime);
        final StackCategory stackCategory = stackCategoryRepository.save(
                StackCategoryFixture.createStackCategory("stackCategory")
        );

        final Stack stack = stackRepository.save(
                StackFixture.createStack("stack", stackCategory)
        );
        final Position position = positionRepository.save(
                PositionFixture.createPosition("position")
        );

        final User userA = userRepository.save(
                User.builder()
                        .nickname("userA")
                        .email("userA@aa.aa")
                        .social(Social.KAKAO)
                        .build());
        final User userB = userRepository.save(
                User.builder()
                        .nickname("userB")
                        .email("userB@aa.aa")
                        .social(Social.KAKAO)
                        .build());


        participantService.add(study, userA, position, Role.MEMBER);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(userB.getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        assertThatThrownBy(() -> reviewFacade.write(request, study.getId(), userA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("스터디에 참여 중인 사용자에게만 리뷰를 작성할 수 있습니다.");
    }

    @DisplayName("[Exception] Reviewer가 스터디원이 아닌 경우, 리뷰 작성이 불가능하다.")
    @Test
    void writeReviewIfNotParticipating() {

        // given
        final User owner = userRepository.save(
                User.builder()
                        .nickname("owner")
                        .email("a@aa.aa")
                        .social(Social.KAKAO)
                        .build());

        final Category category = categoryRepository.save(
                CategoryFixture.createCategory("category1"));

        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;
        final LocalDateTime startDateTime = now.minusDays(20);
        final LocalDateTime endDateTime = now.minusDays(3);

        final Study study = saveStudy(category, owner, startDateTime, endDateTime);
        final StackCategory stackCategory = stackCategoryRepository.save(
                StackCategoryFixture.createStackCategory("stackCategory")
        );

        final Stack stack = stackRepository.save(
                StackFixture.createStack("stack", stackCategory)
        );
        final Position position = positionRepository.save(
                PositionFixture.createPosition("position")
        );

        final User userA = userRepository.save(
                User.builder()
                        .nickname("userA")
                        .email("userA@aa.aa")
                        .social(Social.KAKAO)
                        .build());
        final User userB = userRepository.save(
                User.builder()
                        .nickname("userB")
                        .email("userB@aa.aa")
                        .social(Social.KAKAO)
                        .build());


        participantService.add(study, userB, position, Role.MEMBER);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(userB.getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        assertThatThrownBy(() -> reviewFacade.write(request, study.getId(), userA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("참여 중인 스터디가 아닙니다. 리뷰를 작성할 수 없습니다.");
    }

    @DisplayName("[Exception] 자기 자신에게는 리뷰 작성이 불가능하다.")
    @Test
    void writeReviewForSelf() {
        // given
        final User owner = userRepository.save(
                User.builder()
                        .nickname("owner")
                        .email("a@aa.aa")
                        .social(Social.KAKAO)
                        .build());

        final Category category = categoryRepository.save(
                CategoryFixture.createCategory("category1"));

        final LocalDateTime now = FixedLocalDateTimePicker.DEFAULT_FIXED_LOCAL_DATE_TIME;
        final LocalDateTime startDateTime = now.minusDays(20);
        final LocalDateTime endDateTime = now.minusDays(3);

        final Study study = saveStudy(category, owner, startDateTime, endDateTime);
        final StackCategory stackCategory = stackCategoryRepository.save(
                StackCategoryFixture.createStackCategory("stackCategory")
        );

        final Stack stack = stackRepository.save(
                StackFixture.createStack("stack", stackCategory)
        );
        final Position position = positionRepository.save(
                PositionFixture.createPosition("position")
        );

        final User userA = userRepository.save(
                User.builder()
                        .nickname("userA")
                        .email("userA@aa.aa")
                        .social(Social.KAKAO)
                        .build());
        final User userB = userRepository.save(
                User.builder()
                        .nickname("userB")
                        .email("userB@aa.aa")
                        .social(Social.KAKAO)
                        .build());


        participantService.add(study, userA, position, Role.MEMBER);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(userA.getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        assertThatThrownBy(() -> reviewFacade.write(request, study.getId(), userA))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신에게는 리뷰를 작성할 수 없습니다.");
    }


    private Study saveStudy(Category category, User owner) {
        return studyRepository.save(
                StudyFixture.createStudy(
                        "study",
                        category,
                        owner,
                        4,
                        Platform.GATHER
                )
        );
    }

    private Study saveStudy(Category category, User owner, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return studyRepository.save(
                Study.builder()
                        .title("study")
                        .category(category)
                        .owner(owner)
                        .platform(Platform.GATHER)
                        .startDateTime(startDateTime)
                        .endDateTime(endDateTime)
                        .way(Way.ONLINE)
                        .status(StudyStatus.COMPLETED)
                        .participantLimit(4)
                        .participantCount(1)
                        .build()
        );
    }


    private User saveOwner() {
        return userRepository.save(
                User.builder()
                        .nickname("owner")
                        .email("a@aa.aa")
                        .social(Social.KAKAO)
                        .build()
        );
    }
}