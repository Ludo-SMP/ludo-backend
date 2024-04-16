package com.ludo.study.studymatchingplatform.study.service.recruitment.applicant;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.applicant.ApplicantFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.applicant.StudyApplicantDecisionRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.participant.ParticipantUserResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import jakarta.persistence.EntityManager;

@SpringBootTest
@ActiveProfiles("test")
class StudyApplicantDecisionServiceTest {

	@Autowired
	StudyApplicantDecisionService applicantDecisionService;

	@Autowired
	UserRepositoryImpl userRepository;

	@Autowired
	StudyRepositoryImpl studyRepository;

	@Autowired
	CategoryRepositoryImpl categoryRepository;

	@Autowired
	RecruitmentRepositoryImpl recruitmentRepository;

	@Autowired
	ParticipantRepositoryImpl participantRepository;

	@Autowired
	ApplicantRepositoryImpl applicantRepository;

	@Autowired
	PositionRepositoryImpl positionRepository;

	@Autowired
	EntityManager entityManager;

	private static final String CATEGORY_NAME = "프로젝트";
	private static final String POSITION_NAME = "백엔드";

	@Test
	@Transactional
	@DisplayName("[Success] 스터디 지원자 수락 성공")
	void applicantAcceptTest() {
		// given
		User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
		User other = UserFixture.createUser(Social.NAVER, "other", "other@gmail.com");
		saveUsers(owner, other);

		Category project = categoryRepository.findByName(CATEGORY_NAME).get();

		Study study = StudyFixture.createStudy("스터디A", project, owner, 5, Platform.GATHER);
		Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "지원공고", "내용", 5, "callurl", null);
		study.registerRecruitment(recruitment);

		Applicant applicant = ApplicantFixture.createApplicant(null,
				other, positionRepository.findByName(POSITION_NAME).get());
		recruitment.addApplicant(applicant);

		studyRepository.save(study);
		recruitmentRepository.save(recruitment);
		applicantRepository.save(applicant);

		entityManager.flush();

		// when
		StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(study.getId(),
				other.getId());
		ParticipantUserResponse applyAcceptResponse = applicantDecisionService.applicantAccept(owner,
				studyApplicantDecisionRequest);

		// then
		assertApplyAcceptResponse(applyAcceptResponse, other);
		// Applicant findApplicant = applicantRepository.find(recruitment.getId(), other.getId()).get();
		assertApplicantStatus(applicant, ApplicantStatus.ACCEPTED);

		Optional<Participant> findParticipant = participantRepository.find(study.getId(), other.getId());
		assertParticipant(findParticipant, other, study);
	}

	@Test
	@Transactional
	@DisplayName("[Success] 스터디 지원자 거절 성공")
	void applicantRejectTest() {
		// given
		User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
		User other = UserFixture.createUser(Social.NAVER, "other", "other@gmail.com");
		saveUsers(owner, other);

		Category project = categoryRepository.findByName(CATEGORY_NAME).get();

		Study study = StudyFixture.createStudy("스터디A", project, owner, 5, Platform.GATHER);
		Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "지원공고", "내용", 5, "callurl", null);
		study.registerRecruitment(recruitment);

		Applicant applicant = ApplicantFixture.createApplicant(other, null);
		recruitment.addApplicant(applicant);

		studyRepository.save(study);
		recruitmentRepository.save(recruitment);
		applicantRepository.save(applicant);

		entityManager.flush();

		// when
		StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(study.getId(),
				other.getId());
		applicantDecisionService.applicantReject(owner, studyApplicantDecisionRequest);

		// then
		// Applicant findApplicant = applicantRepository.find(recruitment.getId(), other.getId()).get();
		assertApplicantStatus(applicant, ApplicantStatus.REFUSED);
	}

	private void assertApplyAcceptResponse(ParticipantUserResponse applyAcceptResponse, User other) {
		assertThat(applyAcceptResponse.nickname()).isEqualTo("other");
		assertThat(applyAcceptResponse.email()).isEqualTo("other@gmail.com");
		assertThat(applyAcceptResponse.id()).isEqualTo(other.getId());
		assertThat(applyAcceptResponse.role()).isEqualTo(Role.MEMBER);
		assertThat(applyAcceptResponse.position().getName()).isEqualTo(POSITION_NAME);
	}

	private static void assertApplicantStatus(Applicant findApplicant, ApplicantStatus applicantStatus) {
		assertThat(findApplicant.getApplicantStatus()).isEqualTo(applicantStatus);
	}

	private static void assertParticipant(Optional<Participant> findParticipant, User user, Study study) {
		assertThat(findParticipant).isPresent();
		Participant participant = findParticipant.get();
		assertThat(participant.getUser()).isEqualTo(user);
		assertThat(participant.getStudy()).isEqualTo(study);
		assertThat(participant.getPosition().getName()).isEqualTo(POSITION_NAME);
		assertThat(study.getParticipants()).isNotEmpty();
	}

	private void saveUsers(User... users) {
		for (User user : users) {
			userRepository.save(user);
		}
	}

}
