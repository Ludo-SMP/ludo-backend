package com.ludo.study.studymatchingplatform.user.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MyPageServiceTest {
	//
	// @Autowired
	// UserRepositoryImpl userRepository;
	//
	// @Autowired
	// CategoryRepositoryImpl categoryRepository;
	//
	// @Autowired
	// StudyRepositoryImpl studyRepository;
	//
	// @Autowired
	// PositionRepositoryImpl positionRepository;
	//
	// @Autowired
	// StackRepositoryImpl stackRepository;
	//
	// @Autowired
	// StackCategoryRepositoryImpl stackCategoryRepository;
	//
	// @Autowired
	// RecruitmentService recruitmentService;
	//
	// @Autowired
	// ParticipantRepositoryImpl participantRepository;
	//
	// @Autowired
	// ApplicantRepositoryImpl applicantRepository;
	//
	// @Autowired
	// StudyStatusService studyStatusService;
	//
	// @Autowired
	// RecruitmentRepositoryImpl recruitmentRepository;
	//
	// User createUser(final String nickname, final String email, final Social social) {
	// 	return userRepository.save(
	// 			User.builder()
	// 					.nickname(nickname)
	// 					.email(email)
	// 					.social(social)
	// 					.build());
	// }
	//
	// Study createStudy(final User user) {
	// 	Category category = categoryRepository.save(
	// 			CategoryFixture.createCategory("testCategory"));
	//
	// 	return studyRepository.save(
	// 			StudyFixture.createStudy(
	// 					"study",
	// 					category,
	// 					user,
	// 					4,
	// 					Platform.GATHER
	// 			)
	// 	);
	// }
	//
	// Recruitment createRecruitment(final User user, final Study study) {
	// 	StackCategory stackCategory = stackCategoryRepository.save(
	// 			StackCategoryFixture.createStackCategory("stackCategory")
	// 	);
	//
	// 	Stack stack = stackRepository.save(
	// 			StackFixture.createStack("stack", stackCategory)
	// 	);
	// 	Position position = positionRepository.save(
	// 			PositionFixture.createPosition("position")
	// 	);
	//
	// 	WriteRecruitmentRequest request = WriteRecruitmentRequest.builder()
	// 			.studyId(study.getId())
	// 			.title("recruitment")
	// 			.content("I want to study")
	// 			.stackIds(Set.of(stack.getId()))
	// 			.positionIds(Set.of(position.getId()))
	// 			.recruitmentLimit(4)
	// 			.callUrl("x.com")
	// 			.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
	// 			.build();
	//
	// 	return recruitmentService.write(user, request);
	// }
	//
	// @Test
	// @Transactional
	// void 사용자는_마이페이지를_조회할_수_있어야_한다() {
	// 	// given
	// 	// 사용자1, 스터디1, 모집 공고1 생성
	// 	User user1 = createUser("user1", "aaa@kakao.com", Social.KAKAO);
	// 	Study study1 = createStudy(user1);
	// 	Recruitment recruitment1 = createRecruitment(user1, study1);
	//
	// 	// 사용자2, 스터디2, 모집 공고2 생성
	// 	User user2 = createUser("user2", "bbb@google.com", Social.GOOGLE);
	// 	Study study2 = createStudy(user2);
	// 	Recruitment recruitment2 = createRecruitment(user2, study2);
	//
	// 	// 사용자3, 스터디3, 모집 공고3 생성
	// 	User user3 = createUser("user3", "ccc@naver.com", Social.NAVER);
	// 	Study study3 = createStudy(user3);
	// 	Recruitment recruitment3 = createRecruitment(user3, study3);
	//
	// 	// 사용자4, 스터디4, 모집 공고4 생성
	// 	User user4 = createUser("user4", "ddd@naver.com", Social.NAVER);
	// 	Study study4 = createStudy(user4);
	// 	Recruitment recruitment4 = createRecruitment(user4, study4);
	//
	// 	// when
	// 	// 사용자1 -> 스터디1 참여
	// 	Position position1 = positionRepository.findById(1L);
	// 	Participant participant1 = participantRepository.save(
	// 			ParticipantFixture.createParticipant(study1, user1, position1, Role.OWNER)
	// 	);
	// 	study1.addParticipant(participant1);
	//
	// 	// 사용자1 -> 스터디2 참여
	// 	Position position2 = positionRepository.findById(2L);
	// 	Participant participant2 = participantRepository.save(
	// 			ParticipantFixture.createParticipant(study2, user1, position2, Role.MEMBER)
	// 	);
	// 	study2.addParticipant(participant2);
	//
	// 	// 스터디2 완료됨 상태로 변경
	// 	studyStatusService.changeStatus(updatedStudy2.getId(), StudyStatus.COMPLETED, user2);
	//
	// 	// 사용자1 -> 모집 공고3 지원
	// 	Position position3 = positionRepository.findById(3L);
	// 	recruitment3.addApplicant(applicantRepository.save(
	// 			ApplicantFixture.createApplicant(recruitment3, user1, position3)));
	//
	// 	// 사용자1 -> 모집 공고4 지원
	// 	Position position4 = positionRepository.findById(4L);
	// 	recruitment4.addApplicant(applicantRepository.save(
	// 			ApplicantFixture.createApplicant(recruitment4, user1, position4)));
	//
	// 	// then
	// 	assertThat(study2.getParticipants()).hasSize(1);
	// 	assertThat(study2.getParticipants()).hasSize(1);
	// 	assertThat(study2.getStatus()).isEqualTo(StudyStatus.COMPLETED);
	// 	assertThat(recruitment3.getApplicants()).hasSize(1);
	// 	assertThat(recruitment4.getApplicants()).hasSize(1);
	// }

}
