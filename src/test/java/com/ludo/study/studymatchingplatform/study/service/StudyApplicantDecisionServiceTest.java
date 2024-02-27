package com.ludo.study.studymatchingplatform.study.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.fixture.ApplicantFixture;
import com.ludo.study.studymatchingplatform.study.fixture.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.UserFixture;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.StudyApplicantDecisionRequest;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import jakarta.persistence.EntityManager;

@SpringBootTest
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
	EntityManager entityManager;

	@Test
	@Transactional
	void successTest() {
		// given
		User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
		User other = UserFixture.createUser(Social.NAVER, "other", "other@gmail.com");
		saveUsers(owner, other);

		Category project = saveCategory();

		Study study = StudyFixture.createStudy("스터디A", project, owner, 5);
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
				recruitment.getId(), other.getId());
		applicantDecisionService.applicantAccept(owner, studyApplicantDecisionRequest);

		// then
		Applicant findApplicant = applicantRepository.find(recruitment.getId(), other.getId()).get();
		assertApplicant(findApplicant);

		Optional<Participant> findParticipant = participantRepository.find(study.getId(), other.getId());
		assertParticipant(findParticipant, other, study);
	}

	private static void assertApplicant(Applicant findApplicant) {
		assertThat(findApplicant.getApplicantStatus()).isEqualTo(ApplicantStatus.ACCEPTED);
	}

	private static void assertParticipant(Optional<Participant> findParticipant, User other, Study study) {
		assertThat(findParticipant).isPresent();
		assertThat(findParticipant.get().getUser()).isEqualTo(other);
		assertThat(findParticipant.get().getStudy()).isEqualTo(study);
		assertThat(study.getParticipants()).isNotEmpty();
	}

	private void saveUsers(User... users) {
		for (User user : users) {
			userRepository.save(user);
		}
	}

	private Category saveCategory() {
		Category project = CategoryFixture.createCategory(CategoryFixture.PROJECT);
		categoryRepository.save(project);
		return project;
	}

}