package com.ludo.study.studymatchingplatform.user.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.Platform;
import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.Role;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.fixture.ApplicantFixture;
import com.ludo.study.studymatchingplatform.study.fixture.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.ParticipantFixture;
import com.ludo.study.studymatchingplatform.study.fixture.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StudyFixture;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.RecruitmentService;
import com.ludo.study.studymatchingplatform.study.service.StudyStatusService;
import com.ludo.study.studymatchingplatform.study.service.dto.request.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

@SpringBootTest
class MyPageServiceTest {

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
	private RecruitmentService recruitmentService;

	@Autowired
	private ParticipantRepositoryImpl participantRepository;

	@Autowired
	private ApplicantRepositoryImpl applicantRepository;

	@Autowired
	private StudyStatusService studyStatusService;

	User createUser(final String nickname, final String email, final Social social) {
		return userRepository.save(
				User.builder()
						.nickname(nickname)
						.email(email)
						.social(social)
						.build());
	}

	Study createStudy(final User user) {
		final Category category = categoryRepository.save(
				CategoryFixture.createCategory("testCategory"));

		return studyRepository.save(
				StudyFixture.createStudy(
						"study",
						category,
						user,
						4,
						Platform.GATHER
				)
		);
	}

	Recruitment createRecruitment(final User user, final Study study) {
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
				.recruitmentLimit(4)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

		return recruitmentService.write(user, request);
	}

	@Test
	void 사용자는_마이페이지를_조회할_수_있어야_한다() {
		// given
		// 사용자1, 스터디1, 모집 공고1 생성
		final User user1 = createUser("user1", "aaa@kakao.com", Social.KAKAO);
		final Study study1 = createStudy(user1);
		final Recruitment recruitment1 = createRecruitment(user1, study1);

		// 사용자2, 스터디2, 모집 공고2 생성
		final User user2 = createUser("user2", "bbb@google.com", Social.GOOGLE);
		final Study study2 = createStudy(user2);
		final Recruitment recruitment2 = createRecruitment(user2, study2);

		// 사용자3, 스터디3, 모집 공고3 생성
		final User user3 = createUser("user3", "ccc@naver.com", Social.NAVER);
		final Study study3 = createStudy(user3);
		final Recruitment recruitment3 = createRecruitment(user3, study3);

		// 사용자4, 스터디4, 모집 공고4 생성
		final User user4 = createUser("user4", "ddd@naver.com", Social.NAVER);
		final Study study4 = createStudy(user4);
		final Recruitment recruitment4 = createRecruitment(user4, study4);

		// when
		// 사용자1 -> 스터디1 참여
		final Position position1 = positionRepository.findById(1L);
		final Participant participant1 = participantRepository.save(
				ParticipantFixture.createParticipant(study1, user1, position1, Role.OWNER)
		);
		study1.addParticipant(participant1);

		// 사용자1 -> 스터디2 참여
		final Position position2 = positionRepository.findById(2L);
		final Participant participant2 = participantRepository.save(
				ParticipantFixture.createParticipant(study2, user1, position2, Role.MEMBER)
		);
		study2.addParticipant(participant2);

		// 스터디2 완료됨 상태로 변경
		final Study updatedStudy2 = studyStatusService.changeStatus(study2.getId(), StudyStatus.COMPLETED, user2);

		// 사용자1 -> 모집 공고3 지원
		final Position position3 = positionRepository.findById(3L);
		recruitment3.addApplicant(applicantRepository.save(
				ApplicantFixture.createApplicant(recruitment3, user1, position3)));

		// 사용자1 -> 모집 공고4 지원
		final Position position4 = positionRepository.findById(4L);
		recruitment4.addApplicant(applicantRepository.save(
				ApplicantFixture.createApplicant(recruitment4, user1, position4)));

		// then
		assertThat(study2.getParticipants()).hasSize(1);
		assertThat(study2.getParticipants()).hasSize(1);
		assertThat(updatedStudy2.getStatus()).isEqualTo(StudyStatus.COMPLETED);
		assertThat(recruitment3.getApplicants()).hasSize(1);
		assertThat(recruitment4.getApplicants()).hasSize(1);
	}

}
