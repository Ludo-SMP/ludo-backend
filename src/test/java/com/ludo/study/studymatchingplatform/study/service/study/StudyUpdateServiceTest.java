package com.ludo.study.studymatchingplatform.study.service.study;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.StudyUpdateRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

@ExtendWith(MockitoExtension.class)
class StudyUpdateServiceTest {

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
	private StudyUpdateService studyUpdateService;

	private static User owner;
	private static Category category;
	private static Position position;

	@BeforeAll
	static void setUp() {
		owner = UserFixture.createUserWithId(1L, Social.KAKAO, "닉네임", "이메일");
		category = CategoryFixture.createCategory(1L, "카테고리");
		position = PositionFixture.createPosition(1L, "포지션");
	}

	@DisplayName("[Success] 사용자는 스터디를 수정할 수 있다.")
	@Test
	void createStudyTest() {
		// given
		final WriteStudyRequest request = createWriteStudyRequest();
		final StudyUpdateRequest updateRequest = createStudyUpdateRequest();
		final Study study = createStudy(request);
		final Participant participant = createParticipant(study);

		given(userRepository.findById(any()))
				.willReturn(Optional.of(owner));
		given(categoryRepository.findById(any()))
				.willReturn(Optional.of(category));
		given(positionRepository.findById(any()))
				.willReturn(Optional.of(position));
		given(studyRepository.save(any()))
				.willReturn(study);
		given(studyRepository.findById(any()))
				.willReturn(Optional.of(study));
		given(participantRepository.save(any()))
				.willReturn(participant);
		given(participantRepository.find(any(), any()))
				.willReturn(Optional.of(participant));

		// when
		final StudyResponse response = StudyResponse.from(study);
		final StudyResponse updatedResponse = studyUpdateService.update(owner, any(), updateRequest);

		// then
		Assertions.assertThat(response).isNotEqualTo(updatedResponse);
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

	private StudyUpdateRequest createStudyUpdateRequest() {
		return StudyUpdateRequest.builder()
				.title("스터디 수정")
				.categoryId(1L)
				.positionId(1L)
				.way("OFFLINE")
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
