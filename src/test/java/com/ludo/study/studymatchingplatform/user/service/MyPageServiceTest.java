package com.ludo.study.studymatchingplatform.user.service;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.id.ApplicantId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.applicant.ApplicantFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.participant.ParticipantFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.service.dto.response.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

    @Mock
    private StudyRepositoryImpl studyRepository;

    @Mock
    private ParticipantRepositoryImpl participantRepository;

    @Mock
    private ApplicantRepositoryImpl applicantRepository;

    @Mock
    private UtcDateTimePicker utcDateTimePicker;

    @InjectMocks
    private MyPageService myPageService;

    @DisplayName("[Success] 회원 가입 후 마이 페이지 조회시 해당 사용자의 정보와 비어있는 스터디 리스트를 반환한다.")
    @Test
    void retrieveMyPageInfoWithEmptyStudyList() {
        // given
        final User user = createUser(1L);
        final LocalDateTime testDateTime = createTestDateTime();
        // when
        final MyPageResponse myPageResponse = myPageService.retrieveMyPage(user, testDateTime);
        // then
        final MyPageResponse expectedResponse = new MyPageResponse(
                new UserResponse(1L, "닉네임", "이메일"),
                List.of(), List.of(), List.of());

        // 실제 객체와 expected 객체의 필드를 제귀적으로 비교한다.
        // 프로덕션 코드에 테스트를 위한 코드가 포함되지 않도록 만들 수 있음
        Assertions.assertThat(myPageResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @DisplayName("[Success] 스터디 생성 후 마이 페이지 조회시 참여중인 스터디 리스트에 생성한 항목을 포함한다.")
    @Test
    void retrieveMyPageInfoWithParticipatedStudies() {
        // given
        final User owner = createUser(1L);
        final Category category = createCategory(1L);
        final Position position = createPosition(1L);
        final Study study = createStudy(1L, owner, category,
                LocalDateTime.of(2024, 3, 18, 11, 11),
                LocalDateTime.of(2024, 3, 21, 11, 11));
        final Participant participantOwner = createParticipant(study, owner, position, Role.OWNER);
        study.addParticipant(participantOwner);
        final List<Participant> participants = study.getParticipants();

        // when
        when(participantRepository.findByUserId(anyLong()))
                .thenReturn(participants);
        final MyPageResponse myPageResponse = myPageService.retrieveMyPage(owner, createTestDateTime());

        // then
        final MyPageResponse expectedResponse = new MyPageResponse(
                new UserResponse(1L, "닉네임", "이메일"),
                List.of(new ParticipateStudyResponse(1L, "타이틀",
                        new PositionResponse(1L, "포지션"), StudyStatus.RECRUITING,
                        LocalDateTime.of(2024, 3, 18, 11, 11),
                        LocalDateTime.of(2024, 3, 21, 11, 11),
                        1, Boolean.TRUE, Boolean.FALSE)), List.of(), List.of());

        Assertions.assertThat(myPageResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @DisplayName("[Success] 모집 공고 지원 후 마이 페이지 조회시 내가 지원한 스터디 리스트에 해당 모집 공고 항목을 포함한다.")
    @Test
    void retrieveMyPageInfoWithApplicationRecruitments() {
        // given
        final User owner = createUser(1L);
        final Category category = createCategory(1L);
        final Position position = createPosition(1L);
        final Study study = createStudy(1L, owner, category,
                LocalDateTime.of(2024, 3, 18, 11, 11),
                LocalDateTime.of(2024, 3, 21, 11, 11));
        final Participant participantOwner = createParticipant(study, owner, position, Role.OWNER);
        final Recruitment recruitment = createRecruitment(1L, study);
        study.addParticipant(participantOwner);
        study.registerRecruitment(recruitment);

        final User member = createUser(2L);
        final List<Applicant> applicants = createApplicants(recruitment, member, position);
        recruitment.addApplicant(applicants.get(0));

        when(applicantRepository.findMyPageApplyRecruitmentInfoByUserId(anyLong()))
                .thenReturn(applicants);

        // when
        final MyPageResponse myPageResponse = myPageService.retrieveMyPage(member, createTestDateTime());

        // then
        final MyPageResponse expectedResponse = new MyPageResponse(
                new UserResponse(2L, "닉네임", "이메일"),
                List.of(), List.of(new ApplicantRecruitmentResponse(1L, 1L, "모집 공고",
                new PositionResponse(1L, "포지션"), ApplicantStatus.UNCHECKED)), List.of());

        Assertions.assertThat(myPageResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @DisplayName("[Success] 참가한 스터디가 진행 완료된 경우 마이 페이지 조회시 진행 완료된 스터디 리스트로 항목을 이동한다.")
    @Test
    void retrieveMyPageInfoWithCompletedStudies() {
        // given
        final User owner = createUser(1L);
        final Category category = createCategory(1L);
        final Position position = createPosition(1L);
        final Study study = createStudy(1L, owner, category,
                LocalDateTime.of(2024, 3, 18, 11, 11),
                LocalDateTime.of(2024, 3, 21, 11, 11));
        final Participant participantOwner = createParticipant(study, owner, position, Role.OWNER);
        study.addParticipant(participantOwner);

        study.update("타이틀", category, 3, Way.ONLINE, Platform.GATHER, "www.gather.com",
                LocalDateTime.of(2024, 3, 13, 11, 11),
                LocalDateTime.of(2024, 3, 14, 11, 11));

        final LocalDateTime testDateTime = createTestDateTime();
        study.modifyStatusToCompleted(testDateTime);
        final List<Participant> completedStudies = study.getParticipants();

        when(participantRepository.findCompletedStudyByUserId(anyLong()))
                .thenReturn(completedStudies);

        // when
        final MyPageResponse myPageResponse = myPageService.retrieveMyPage(owner, testDateTime);

        // then
        final MyPageResponse expectedResponse = new MyPageResponse(
                new UserResponse(1L, "닉네임", "이메일"),
                List.of(), List.of(), List.of(new CompletedStudyResponse(1L, "타이틀",
                new PositionResponse(1L, "포지션"), StudyStatus.COMPLETED,
                LocalDateTime.of(2024, 3, 13, 11, 11),
                LocalDateTime.of(2024, 3, 14, 11, 11),
                1, Boolean.TRUE, Boolean.FALSE)));

        Assertions.assertThat(myPageResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @DisplayName("[Success] 스터디가 진행 기간 이전 모집 마감 상태로 변경된 경우"
            + " 마이 페이지 조회시 참여중인 스터디 리스트에 모집 마감 상태로 항목을 포함한다.")
    @Test
    void retrieveMyPageInfoWithParticipatedStudiesForRecruitedStatus() {
        // given
        final User owner = createUser(1L);
        final Category category = createCategory(1L);
        final Position position = createPosition(1L);
        final Study study = createStudy(1L, owner, category,
                LocalDateTime.of(2024, 3, 18, 11, 11),
                LocalDateTime.of(2024, 3, 21, 11, 11));
        final Participant participantOwner = createParticipant(study, owner, position, Role.OWNER);
        study.addParticipant(participantOwner);
        final LocalDateTime testDateTime = createTestDateTime();
        study.modifyStatus(StudyStatus.RECRUITED, testDateTime);
        final List<Participant> participants = study.getParticipants();

        when(participantRepository.findByUserId(anyLong()))
                .thenReturn(participants);

        // when
        final MyPageResponse myPageResponse = myPageService.retrieveMyPage(owner, testDateTime);

        // then
        final MyPageResponse expectedResponse = new MyPageResponse(
                new UserResponse(1L, "닉네임", "이메일"),
                List.of(new ParticipateStudyResponse(1L, "타이틀",
                        new PositionResponse(1L, "포지션"), StudyStatus.RECRUITED,
                        LocalDateTime.of(2024, 3, 18, 11, 11),
                        LocalDateTime.of(2024, 3, 21, 11, 11),
                        1, Boolean.TRUE, Boolean.FALSE)), List.of(), List.of());

        Assertions.assertThat(myPageResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @DisplayName("[Success] 스터디가 진행 기간 동안 모집 마감 상태로 변경된 경우"
            + " 마이 페이지 조회시 참여중인 스터디 리스트에 진행중 상태로 항목을 포함한다.")
    @Test
    void retrieveMyPageInfoWithParticipatedStudiesForProgressStatus() {
        // given
        final User owner = createUser(1L);
        final Category category = createCategory(1L);
        final Position position = createPosition(1L);
        final Study study = createStudy(1L, owner, category,
                LocalDateTime.of(2024, 3, 15, 11, 11),
                LocalDateTime.of(2024, 3, 21, 11, 11));
        final Participant participantOwner = createParticipant(study, owner, position, Role.OWNER);
        study.addParticipant(participantOwner);
        final LocalDateTime testDateTime = createTestDateTime();
        study.modifyStatus(StudyStatus.RECRUITED, testDateTime);
        final List<Participant> participants = study.getParticipants();

        when(participantRepository.findByUserId(anyLong()))
                .thenReturn(participants);

        // when
        final MyPageResponse myPageResponse = myPageService.retrieveMyPage(owner, testDateTime);

        // then
        final MyPageResponse expectedResponse = new MyPageResponse(
                new UserResponse(1L, "닉네임", "이메일"),
                List.of(new ParticipateStudyResponse(1L, "타이틀",
                        new PositionResponse(1L, "포지션"), StudyStatus.PROGRESS,
                        LocalDateTime.of(2024, 3, 15, 11, 11),
                        LocalDateTime.of(2024, 3, 21, 11, 11),
                        1, Boolean.TRUE, Boolean.FALSE)), List.of(), List.of());

        Assertions.assertThat(myPageResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @DisplayName("[Success] 모집 공고 지원 거절 후 기록을 삭제할 경우 내가 지원한 스터디 리스트에 해당 모집 공고 항목을 제외한다.")
    @Test
    void retrieveMyPageInfoWithRejectedApplicationRecruitments() {
        // given
        final User owner = createUser(1L);
        final Category category = createCategory(1L);
        final Position position = createPosition(1L);
        final Study study = createStudy(1L, owner, category,
                LocalDateTime.of(2024, 3, 18, 11, 11),
                LocalDateTime.of(2024, 3, 21, 11, 11));
        final Participant participantOwner = createParticipant(study, owner, position, Role.OWNER);
        final Recruitment recruitment = createRecruitment(1L, study);
        study.addParticipant(participantOwner);
        study.registerRecruitment(recruitment);

        final User member = createUser(2L);
        final List<Applicant> applicants = createApplicants(recruitment, member, position);
        recruitment.addApplicant(applicants.get(0));

        when(applicantRepository.findMyPageApplyRecruitmentInfoByUserId(anyLong()))
                .thenReturn(applicants);
        when(applicantRepository.findById(any()))
                .thenReturn(Optional.of(applicants.get(0)));
        when(utcDateTimePicker.now())
                .thenReturn(LocalDateTime.of(2024, 3, 16, 11, 11));

        // when
        recruitment.rejectApplicant(member);
        myPageService.deleteApplyHistory(member, 1L);
        final MyPageResponse myPageResponse = myPageService.retrieveMyPage(member, createTestDateTime());

        // then
        final MyPageResponse expectedResponse = new MyPageResponse(
                new UserResponse(2L, "닉네임", "이메일"),
                List.of(), List.of(), List.of());

        Assertions.assertThat(myPageResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    private User createUser(final Long id) {
        return UserFixture.createUserWithId(id, Social.KAKAO, "닉네임", "이메일");
    }

    private Category createCategory(final Long id) {
        return CategoryFixture.createCategory(id, "카테고리");
    }

    private Study createStudy(final Long id, final User owner, final Category category,
                              final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        return StudyFixture.createStudy(
                id, "타이틀", Way.ONLINE, category, owner, 1, 3,
                startDateTime, endDateTime);

    }

    private Recruitment createRecruitment(final Long id, final Study study) {
        return RecruitmentFixture.createRecruitment(
                id, study, "모집 공고", "콘텐츠", 2, "URL", LocalDateTime.now().plusDays(1));
    }

    private Position createPosition(final Long id) {
        return PositionFixture.createPosition(id, "포지션");
    }

    private Participant createParticipant(final Study study, final User user, final Position position,
                                          final Role role) {
        return ParticipantFixture.createParticipant(study, user, position, role);
    }

    private List<Applicant> createApplicants(final Recruitment recruitment, final User user, final Position position) {
        final ApplicantId id = new ApplicantId(recruitment.getId(), user.getId());
        final Applicant applicant = ApplicantFixture.createApplicant(id, recruitment, user, position);
        return List.of(applicant);
    }

    private LocalDateTime createTestDateTime() {
        return LocalDateTime.of(2024, 3, 16, 11, 11);
    }

}
