package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.participant.ParticipantFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CreateStudyServiceTest {

    @Mock
    private StudyRepositoryImpl studyRepository;

    @Mock
    private CategoryRepositoryImpl categoryRepository;

    @Mock
    private PositionRepositoryImpl positionRepository;

    @Mock
    private UserRepositoryImpl userRepository;

    @Mock
    private ParticipantRepositoryImpl participantRepository;

    @InjectMocks
    private StudyCreateService studyCreateService;

    private static User owner;
    private static Category category;
    private static Position position;

    @BeforeAll
    static void setUp() {
        owner = UserFixture.createUserWithId(1L, Social.KAKAO, "닉네임", "이메일");
        category = CategoryFixture.createCategory(1L, "카테고리");
        position = PositionFixture.createPosition(1L, "포지션");
    }

    @DisplayName("[Success] 사용자는 스터디를 생성할 수 있다.")
    @Test
    void createStudyTest() {
        // given
        final WriteStudyRequest request = createWriteStudyRequest();
        final Study study = createStudy(request);
        final Participant participant = createParticipant(study);

        given(userRepository.findById(any()))
                .willReturn(Optional.of(owner));
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(category));
        given(positionRepository.findById(any()))
                .willReturn(Optional.of(position));
        given(participantRepository.save(any()))
                .willReturn(participant);

        // when
        when(studyRepository.save(any()))
                .thenReturn(study);
        when(participantRepository.save(any()))
                .thenReturn(participant);

        // then
        assertDoesNotThrow(() -> studyCreateService.create(request, owner));
    }

    @DisplayName("[Exception] 스터디 생성시 존재하지 않는 사용자일 경우 예외가 발생한다.")
    @Test
    void createStudyUserNotFoundTest() {
        // given
        final WriteStudyRequest request = createWriteStudyRequest();

        // when
        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> studyCreateService.create(request, owner))
                .isInstanceOf(SocialAccountNotFoundException.class);
    }

    @DisplayName("[Exception] 스터디 생성시 존재하지 않는 카테고리를 입력할 경우 예외가 발생한다.")
    @Test
    void createStudyCategoryNotFoundTest() {
        // given
        final WriteStudyRequest request = createWriteStudyRequest();

        given(userRepository.findById(any()))
                .willReturn(Optional.of(owner));

        // when
        when(categoryRepository.findById(any()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> studyCreateService.create(request, owner))
                .isInstanceOf(SocialAccountNotFoundException.class);
    }

    @DisplayName("[Exception] 스터디 생성시 존재하지 않는 포지션을 입력할 경우 예외가 발생한다.")
    @Test
    void createStudyPositionNotFoundTest() {
        // given
        final WriteStudyRequest request = createWriteStudyRequest();

        given(userRepository.findById(any()))
                .willReturn(Optional.of(owner));
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(category));

        // when
        when(positionRepository.findById(any()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> studyCreateService.create(request, owner))
                .isInstanceOf(SocialAccountNotFoundException.class);
    }

    @DisplayName("[Exception] 스터디 생성시 존재하지 않는 플랫폼을 입력할 경우 예외가 발생한다.")
    @Test
    void createStudyPlatformNotFoundTest() {
        // given
        final WriteStudyRequest request = WriteStudyRequest.builder()
                .title("스터디 생성")
                .categoryId(1L)
                .positionId(1L)
                .way("ONLINE")
                .platform("테스트 플랫폼")
                .platformUrl("www.platformUrl.com")
                .participantLimit(3)
                .startDateTime(LocalDateTime.of(2024, 4, 23, 11, 11))
                .endDateTime(LocalDateTime.of(2024, 5, 23, 11, 11))
                .build();

        given(userRepository.findById(any()))
                .willReturn(Optional.of(owner));
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(category));
        given(positionRepository.findById(any()))
                .willReturn(Optional.of(position));

        // when
        // then
        assertThatThrownBy(() -> studyCreateService.create(request, owner))
                .isInstanceOf(SocialAccountNotFoundException.class);
    }

    @DisplayName("[Exception] 스터디 생성시 존재하지 않는 진행 방식을 입력할 경우 예외가 발생한다.")
    @Test
    void createStudyWayNotFoundTest() {
        // given
        final WriteStudyRequest request = WriteStudyRequest.builder()
                .title("스터디 생성")
                .categoryId(1L)
                .positionId(1L)
                .way("테스트 진행 방식")
                .platform("GATHER")
                .platformUrl("www.platformUrl.com")
                .participantLimit(3)
                .startDateTime(LocalDateTime.of(2024, 4, 23, 11, 11))
                .endDateTime(LocalDateTime.of(2024, 5, 23, 11, 11))
                .build();

        given(userRepository.findById(any()))
                .willReturn(Optional.of(owner));
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(category));
        given(positionRepository.findById(any()))
                .willReturn(Optional.of(position));

        // when
        // then
        assertThatThrownBy(() -> studyCreateService.create(request, owner))
                .isInstanceOf(SocialAccountNotFoundException.class);
    }

    @DisplayName("[Success] 스터디 생성시 참여자 리스트에 팀장이 포함되어야 한다.")
    @Test
    void createStudyParticipantEmptyTest() {
        // given
        final WriteStudyRequest request = createWriteStudyRequest();
        final Study study = createStudy(request);
        final Participant participant = createParticipant(study);
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(category));
        given(positionRepository.findById(any()))
                .willReturn(Optional.of(position));
        given(studyRepository.save(any()))
                .willReturn(study);
        given(participantRepository.save(any()))
                .willReturn(participant);

        // when
        when(userRepository.findById(owner.getId()))
                .thenReturn(Optional.of(owner));

        // then
        final StudyResponse response = studyCreateService.create(request, owner);
        assertThat(response.participants()).isNotEmpty();
    }

    private WriteStudyRequest createWriteStudyRequest() {
        return WriteStudyRequest.builder()
                .title("스터디 생성")
                .categoryId(1L)
                .positionId(1L)
                .way("ONLINE")
                .platform("GATHER")
                .platformUrl("www.platformUrl.com")
                .participantLimit(3)
                .startDateTime(LocalDateTime.of(2024, 4, 23, 11, 11))
                .endDateTime(LocalDateTime.of(2024, 5, 23, 11, 11))
                .build();
    }

    private Study createStudy(final WriteStudyRequest request) {
        return request.toStudy(owner, category, Way.ONLINE, Platform.GATHER);
    }

    private Participant createParticipant(final Study study) {
        return ParticipantFixture.createParticipant(study, owner, position, Role.OWNER);
    }

}
