package com.ludo.study.studymatchingplatform.study.service.recruitment;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.RecruitmentPositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentDetailsResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class RecruitmentDetailFindServiceTest {

	@Autowired
	RecruitmentDetailsFindService recruitmentDetailsFindService;

	@Autowired
	RecruitmentRepositoryImpl recruitmentRepository;

	@Autowired
	StudyRepositoryImpl studyRepository;

	@Autowired
	UserRepositoryImpl userRepository;

	@Autowired
	CategoryRepositoryImpl categoryRepository;

	@Autowired
	StackCategoryRepositoryImpl stackCategoryRepository;

	@Autowired
	EntityManager em;

	private static final String CATEGORY = "프로젝트";
	private static final String STUDY_TITLE = "스터디1";
	private static final String RECRUITMENT_TITLE = "지원공고 입니다.";
	private static final String NICKNAME = "아카";
	private static final String EMAIL = "archa@naver.com";
	private static final String STACK_SPRING = "spring";
	private static final String STACK_REACT = "react";
	private static final String CALL_URL = "call_url";
	private static final String CONTENT = "내용";

	@Test
	@Transactional
	void 모집공고_상세를_조회한다() {
		// given
		Recruitment saveRecruitment = saveRecruitment();
		// when
		RecruitmentDetailsResponse recruitmentDetailsResponse = recruitmentDetailsFindService
				.findRecruitmentDetails(saveRecruitment.getId());
		// then
		assertThat(recruitmentDetailsResponse.recruitment().title()).isEqualTo(RECRUITMENT_TITLE);
		assertThat(recruitmentDetailsResponse.recruitment().positions())
				.extracting("name")
				.contains("백엔드");
		assertThat(recruitmentDetailsResponse.recruitment().stacks())
				.extracting("name")
				.contains("spring", "react");
		assertThat(recruitmentDetailsResponse.study().owner().nickname()).isEqualTo(NICKNAME);
		assertThat(recruitmentDetailsResponse.study().way()).isEqualTo(Way.ONLINE.toString());
		assertThat(recruitmentDetailsResponse.study().category().name()).isEqualTo(CATEGORY);

		Recruitment findRecruitment = recruitmentRepository.findById(saveRecruitment.getId()).get();
		assertModifiedDateNotChange(saveRecruitment, findRecruitment);
	}

	private void assertModifiedDateNotChange(Recruitment saveRecruitment, Recruitment findRecruitment) {
		assertThat(saveRecruitment.getModifiedDateTime()).isEqualTo(findRecruitment.getModifiedDateTime());
	}

	@Test
	@Transactional
	void 모집공고_상세를_조회하면_조회수_증가() {
		// given
		Recruitment saveRecruitment = saveRecruitment();
		int expectedHits = 6;
		// when
		recruitmentDetailsFindService.findRecruitmentDetails(saveRecruitment.getId());
		// then
		Recruitment findRecruitment = recruitmentRepository.findById(saveRecruitment.getId()).get();
		assertThat(findRecruitment.getHits()).isEqualTo(expectedHits);
		assertModifiedDateNotChange(saveRecruitment, findRecruitment);
	}

	private Recruitment saveRecruitment() {
		User user = UserFixture.createUser(Social.NAVER, NICKNAME, EMAIL);

		Category category = CategoryFixture.createCategory(CATEGORY);

		Study study = StudyFixture.createStudy(StudyStatus.RECRUITING, STUDY_TITLE, Way.ONLINE,
				category, user, 5, 10, Platform.GATHER);

		Recruitment recruitment = RecruitmentFixture.createRecruitment(study, RECRUITMENT_TITLE, CONTENT, 5, CALL_URL,
				null);

		RecruitmentPosition backend = RecruitmentPositionFixture.createRecruitmentPosition(
				PositionFixture.createPosition("백엔드"));
		backend.registerRecruitment(recruitment);

		StackCategory backendStackCategory = StackCategoryFixture.createStackCategory("백엔드 기술스택");
		StackCategory frontendStackCategory = StackCategoryFixture.createStackCategory("프론트엔드 기술스택");

		Stack spring = StackFixture.createStack(STACK_SPRING, backendStackCategory);
		Stack react = StackFixture.createStack(STACK_REACT, frontendStackCategory);

		RecruitmentStack recruitmentSpring = RecruitmentStack.from(recruitment, spring);
		RecruitmentStack recruitmentReact = RecruitmentStack.from(recruitment, react);

		recruitmentSpring.registerRecruitmentAndStack(recruitment, spring);
		recruitmentReact.registerRecruitmentAndStack(recruitment, react);

		userRepository.save(user);
		categoryRepository.save(category);
		studyRepository.save(study);
		stackCategoryRepository.save(backendStackCategory);
		stackCategoryRepository.save(frontendStackCategory);
		Recruitment saveRecruitment = recruitmentRepository.save(recruitment);

		em.flush();
		em.clear();
		return saveRecruitment;
	}

}
