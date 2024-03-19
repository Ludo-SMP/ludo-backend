package com.ludo.study.studymatchingplatform.study.service.recruitment.applicant;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudyApplicantDecisionServiceTest {

	// @Autowired
	// StudyApplicantDecisionService applicantDecisionService;
	//
	// @Autowired
	// UserRepositoryImpl userRepository;
	//
	// @Autowired
	// StudyRepositoryImpl studyRepository;
	//
	// @Autowired
	// CategoryRepositoryImpl categoryRepository;
	//
	// @Autowired
	// RecruitmentRepositoryImpl recruitmentRepository;
	//
	// @Autowired
	// ParticipantRepositoryImpl participantRepository;
	//
	// @Autowired
	// ApplicantRepositoryImpl applicantRepository;
	//
	// @Autowired
	// PositionRepositoryImpl positionRepository;
	//
	// @Autowired
	// EntityManager entityManager;
	//
	// private static final String CATEGORY_NAME = "프로젝트";
	// private static final String POSITION_NAME = "백엔드";
	//
	// @Test
	// @Transactional
	// @DisplayName("[Success] 스터디 지원자 수락 성공")
	// void applicantAcceptTest() {
	// 	// given
	// 	User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
	// 	User other = UserFixture.createUser(Social.NAVER, "other", "other@gmail.com");
	// 	saveUsers(owner, other);
	//
	// 	Category project = categoryRepository.findByName(CATEGORY_NAME).get();
	//
	// 	Study study = StudyFixture.createStudy("스터디A", project, owner, 5, Platform.GATHER);
	// 	Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "지원공고", "내용", 5, "callurl", null);
	// 	study.registerRecruitment(recruitment);
	//
	// 	Applicant applicant = ApplicantFixture.createApplicant(null,
	// 			other, positionRepository.findByName(POSITION_NAME).get());
	// 	recruitment.addApplicant(applicant);
	//
	// 	studyRepository.save(study);
	// 	recruitmentRepository.save(recruitment);
	// 	applicantRepository.save(applicant);
	//
	// 	entityManager.flush();
	//
	// 	// when
	// 	StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(study.getId(),
	// 			other.getId());
	// 	ParticipantUserResponse applyAcceptResponse = applicantDecisionService.applicantAccept(owner,
	// 			studyApplicantDecisionRequest);
	//
	// 	// then
	// 	assertApplyAcceptResponse(applyAcceptResponse, other);
	// 	Applicant findApplicant = applicantRepository.find(recruitment.getId(), other.getId()).get();
	// 	assertApplicantStatus(findApplicant, ApplicantStatus.ACCEPTED);
	//
	// 	Optional<Participant> findParticipant = participantRepository.find(study.getId(), other.getId());
	// 	assertParticipant(findParticipant, other, study);
	// }
	//
	// @Test
	// @Transactional
	// @DisplayName("[Success] 스터디 지원자 거절 성공")
	// void applicantRejectTest() {
	// 	// given
	// 	User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
	// 	User other = UserFixture.createUser(Social.NAVER, "other", "other@gmail.com");
	// 	saveUsers(owner, other);
	//
	// 	Category project = categoryRepository.findByName(CATEGORY_NAME).get();
	//
	// 	Study study = StudyFixture.createStudy("스터디A", project, owner, 5, Platform.GATHER);
	// 	Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "지원공고", "내용", 5, "callurl", null);
	// 	study.registerRecruitment(recruitment);
	//
	// 	Applicant applicant = ApplicantFixture.createApplicant(other, null);
	// 	recruitment.addApplicant(applicant);
	//
	// 	studyRepository.save(study);
	// 	recruitmentRepository.save(recruitment);
	// 	applicantRepository.save(applicant);
	//
	// 	entityManager.flush();
	//
	// 	// when
	// 	StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(study.getId(),
	// 			other.getId());
	// 	applicantDecisionService.applicantReject(owner, studyApplicantDecisionRequest);
	//
	// 	// then
	// 	Applicant findApplicant = applicantRepository.find(recruitment.getId(), other.getId()).get();
	// 	assertApplicantStatus(findApplicant, ApplicantStatus.REFUSED);
	// }
	//
	// private void assertApplyAcceptResponse(ParticipantUserResponse applyAcceptResponse, User other) {
	// 	assertThat(applyAcceptResponse.participant().nickname()).isEqualTo("other");
	// 	assertThat(applyAcceptResponse.participant().email()).isEqualTo("other@gmail.com");
	// 	assertThat(applyAcceptResponse.participant().id()).isEqualTo(other.getId());
	// 	assertThat(applyAcceptResponse.participant().role()).isEqualTo(Role.MEMBER);
	// 	assertThat(applyAcceptResponse.participant().position().getName()).isEqualTo(POSITION_NAME);
	// }
	//
	// private static void assertApplicantStatus(Applicant findApplicant, ApplicantStatus applicantStatus) {
	// 	assertThat(findApplicant.getApplicantStatus()).isEqualTo(applicantStatus);
	// }
	//
	// private static void assertParticipant(Optional<Participant> findParticipant, User user, Study study) {
	// 	assertThat(findParticipant).isPresent();
	// 	Participant participant = findParticipant.get();
	// 	assertThat(participant.getUser()).isEqualTo(user);
	// 	assertThat(participant.getStudy()).isEqualTo(study);
	// 	assertThat(participant.getPosition().getName()).isEqualTo(POSITION_NAME);
	// 	assertThat(study.getParticipants()).isNotEmpty();
	// }
	//
	// private void saveUsers(User... users) {
	// 	for (User user : users) {
	// 		userRepository.save(user);
	// 	}
	// }

}
