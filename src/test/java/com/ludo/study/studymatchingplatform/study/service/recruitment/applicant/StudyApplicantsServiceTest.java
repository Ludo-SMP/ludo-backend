package com.ludo.study.studymatchingplatform.study.service.recruitment.applicant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
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
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantStudyResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantUserResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;
import com.ludo.study.studymatchingplatform.study.service.study.StudyService;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.service.dto.response.UserResponse;

@ExtendWith(MockitoExtension.class)
class StudyApplicantsServiceTest {

	@Mock
	private StudyRepositoryImpl studyRepository;

	@Mock
	private ApplicantRepositoryImpl applicantRepository;

	@InjectMocks
	private StudyService studyService;

	@DisplayName("[Success] 스터디 팀장이 지원자 목록 조회시 지원자들의 정보를 반환한다.")
	@Test
	void findApplicantsInfoFromOwnerTest() {
		// given
		final User owner = createUser(1L);
		final Category category = createCategory(1L);
		final Position position = createPosition(1L);
		final Study study = createStudy(1L, owner, category);
		final Participant participant = createParticipant(study, owner, position, Role.OWNER);
		final Recruitment recruitment = createRecruitment(1L, study);
		study.addParticipant(participant);
		study.registerRecruitment(recruitment);

		final List<Applicant> applicants = createApplicants(recruitment, position);
		recruitment.addApplicant(applicants.get(0));
		recruitment.addApplicant(applicants.get(1));
		recruitment.addApplicant(applicants.get(2));

		when(studyRepository.findByIdWithRecruitment(anyLong()))
				.thenReturn(Optional.of(study));
		when(applicantRepository.findStudyApplicantInfoByRecruitmentId(anyLong()))
				.thenReturn(applicants);

		// when
		final ApplicantResponse applicantResponse = studyService.findApplicantsInfo(owner, 1L);

		// then
		final ApplicantResponse expectedResponse = new ApplicantResponse(
				new ApplicantStudyResponse(1L,
						new UserResponse.InnerUserResponse(1L, "닉네임", "이메일"),
						StudyStatus.RECRUITING, "타이틀", 3, 1),
				List.of(new ApplicantUserResponse(2L, "닉네임", "이메일",
								new PositionResponse(1L, "포지션")),
						new ApplicantUserResponse(3L, "닉네임", "이메일",
								new PositionResponse(1L, "포지션")),
						new ApplicantUserResponse(4L, "닉네임", "이메일",
								new PositionResponse(1L, "포지션")))
		);

		assertThat(applicantResponse)
				.usingRecursiveComparison()
				.isEqualTo(expectedResponse);
	}

	@DisplayName("[Success] 스터디 팀장이 지원자가 없는 상태에서 지원자 목록 조회시 비어있는 지원자 목록을 반환한다.")
	@Test
	void findEmptyApplicantsInfoFromOwnerTest() {
		// given
		final User owner = createUser(1L);
		final Category category = createCategory(1L);
		final Position position = createPosition(1L);
		final Study study = createStudy(1L, owner, category);
		final Participant participant = createParticipant(study, owner, position, Role.OWNER);
		final Recruitment recruitment = createRecruitment(1L, study);
		study.addParticipant(participant);
		study.registerRecruitment(recruitment);

		when(studyRepository.findByIdWithRecruitment(anyLong()))
				.thenReturn(Optional.of(study));
		// when
		final ApplicantResponse applicantResponse = studyService.findApplicantsInfo(owner, 1L);

		// then
		final ApplicantResponse expectedResponse = new ApplicantResponse(
				new ApplicantStudyResponse(1L,
						new UserResponse.InnerUserResponse(1L, "닉네임", "이메일"),
						StudyStatus.RECRUITING, "타이틀", 3, 1),
				List.of()
		);

		assertThat(applicantResponse)
				.usingRecursiveComparison()
				.isEqualTo(expectedResponse);
	}

	@DisplayName("[Success] 스터디 팀원이 지원자 목록 조회시 지원자들의 정보를 반환한다.")
	@Test
	void findApplicantsInfoFromMemberTest() {
		// given
		final User owner = createUser(1L);
		final User member = createUser(5L);
		final Category category = createCategory(1L);
		final Position position = createPosition(1L);
		final Study study = createStudy(1L, owner, category);
		final Participant participantOwner = createParticipant(study, owner, position, Role.OWNER);
		final Participant participantMember = createParticipant(study, member, position, Role.MEMBER);
		final Recruitment recruitment = createRecruitment(1L, study);
		study.addParticipant(participantOwner);
		study.addParticipant(participantMember);
		study.registerRecruitment(recruitment);

		final List<Applicant> applicants = createApplicants(recruitment, position);
		recruitment.addApplicant(applicants.get(0));
		recruitment.addApplicant(applicants.get(1));
		recruitment.addApplicant(applicants.get(2));

		when(studyRepository.findByIdWithRecruitment(anyLong()))
				.thenReturn(Optional.of(study));
		when(applicantRepository.findStudyApplicantInfoByRecruitmentId(anyLong()))
				.thenReturn(applicants);

		// when
		final ApplicantResponse applicantResponse = studyService.findApplicantsInfo(member, 1L);

		// then
		final ApplicantResponse expectedResponse = new ApplicantResponse(
				new ApplicantStudyResponse(1L,
						new UserResponse.InnerUserResponse(1L, "닉네임", "이메일"),
						StudyStatus.RECRUITING, "타이틀", 3, 2),
				List.of(new ApplicantUserResponse(2L, "닉네임", "이메일",
								new PositionResponse(1L, "포지션")),
						new ApplicantUserResponse(3L, "닉네임", "이메일",
								new PositionResponse(1L, "포지션")),
						new ApplicantUserResponse(4L, "닉네임", "이메일",
								new PositionResponse(1L, "포지션")))
		);

		assertThat(applicantResponse)
				.usingRecursiveComparison()
				.isEqualTo(expectedResponse);
	}

	@DisplayName("[Success] 스터디 팀원이 지원자가 없는 상태에서 지원자 목록 조회시 비어있는 지원자 목록을 반환한다.")
	@Test
	void findEmptyApplicantsInfoFromMemberTest() {
		// given
		final User owner = createUser(1L);
		final User member = createUser(5L);
		final Category category = createCategory(1L);
		final Position position = createPosition(1L);
		final Study study = createStudy(1L, owner, category);
		final Participant participantOwner = createParticipant(study, owner, position, Role.OWNER);
		final Participant participantMember = createParticipant(study, member, position, Role.MEMBER);
		final Recruitment recruitment = createRecruitment(1L, study);
		study.addParticipant(participantOwner);
		study.addParticipant(participantMember);
		study.registerRecruitment(recruitment);

		when(studyRepository.findByIdWithRecruitment(anyLong()))
				.thenReturn(Optional.of(study));
		// when
		final ApplicantResponse applicantResponse = studyService.findApplicantsInfo(member, 1L);

		// then
		final ApplicantResponse expectedResponse = new ApplicantResponse(
				new ApplicantStudyResponse(1L,
						new UserResponse.InnerUserResponse(1L, "닉네임", "이메일"),
						StudyStatus.RECRUITING, "타이틀", 3, 2),
				List.of()
		);

		assertThat(applicantResponse)
				.usingRecursiveComparison()
				.isEqualTo(expectedResponse);
	}

	@DisplayName("[Exception] 스터디 참가자가 아닌 사용자가 지원자 목록 조회시 반환한다.")
	@Test
	void findApplicantsInfoFromOtherTest() {
		// given
		final User owner = createUser(1L);
		final User other = createUser(5L);
		final Category category = createCategory(1L);
		final Position position = createPosition(1L);
		final Study study = createStudy(1L, owner, category);
		final Participant participantOwner = createParticipant(study, owner, position, Role.OWNER);
		final Recruitment recruitment = createRecruitment(1L, study);
		study.addParticipant(participantOwner);
		study.registerRecruitment(recruitment);

		final List<Applicant> applicants = createApplicants(recruitment, position);
		recruitment.addApplicant(applicants.get(0));
		recruitment.addApplicant(applicants.get(1));
		recruitment.addApplicant(applicants.get(2));

		when(studyRepository.findByIdWithRecruitment(anyLong()))
				.thenReturn(Optional.of(study));

		// when
		// then
		assertThatThrownBy(() -> studyService.findApplicantsInfo(other, 1L))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("현재 참여 중인 스터디원이 아닙니다.");
	}

	private User createUser(final Long id) {
		return UserFixture.createUserWithId(id, Social.KAKAO, "닉네임", "이메일");
	}

	private Category createCategory(final Long id) {
		return CategoryFixture.createCategory(id, "카테고리");
	}

	private Study createStudy(final Long id, final User owner, final Category category) {
		return StudyFixture.createStudy(id, "타이틀", Way.ONLINE, category, owner, 1, 3);
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

	private List<Applicant> createApplicants(final Recruitment recruitment, final Position position) {
		final Applicant applicant1 = ApplicantFixture.createApplicant(recruitment, createUser(2L), position);
		final Applicant applicant2 = ApplicantFixture.createApplicant(recruitment, createUser(3L), position);
		final Applicant applicant3 = ApplicantFixture.createApplicant(recruitment, createUser(4L), position);
		return List.of(applicant1, applicant2, applicant3);
	}

}
