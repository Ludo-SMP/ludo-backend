package com.ludo.study.studymatchingplatform.study.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.fixture.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.RecruitmentStackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.UserFixture;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentDetailsResponse;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

@SpringBootTest
class RecruitmentDetailsFindServiceTest {

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
		RecruitmentDetailsResponse recruitmentDetailsResponse = recruitmentDetailsFindService.findRecruitmentDetails(
				saveRecruitment.getId());
		// then
		assertThat(recruitmentDetailsResponse.title()).isEqualTo(RECRUITMENT_TITLE);
		assertThat(recruitmentDetailsResponse.ownerNickname()).isEqualTo(NICKNAME);
		assertThat(recruitmentDetailsResponse.way()).isEqualTo(Way.ONLINE.toString());
		assertThat(recruitmentDetailsResponse.category()).isEqualTo(CATEGORY);
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
		Recruitment find = recruitmentRepository.findById(saveRecruitment.getId()).get();
		assertThat(find.getHits()).isEqualTo(expectedHits);
	}

	private Recruitment saveRecruitment() {
		Category category = CategoryFixture.createCategory(CATEGORY);
		User user = UserFixture.createUser(Social.NAVER, NICKNAME, EMAIL);
		Study study = StudyFixture.createStudy(StudyStatus.RECRUITING, STUDY_TITLE, Way.ONLINE, category, user, 5, 10);

		RecruitmentStack spring = RecruitmentStackFixture.createRecruitmentStack(
				StackFixture.createStack(STACK_SPRING, StackCategoryFixture.createStackCategory("stackCategory1")));
		RecruitmentStack react = RecruitmentStackFixture.createRecruitmentStack(
				StackFixture.createStack(STACK_REACT, StackCategoryFixture.createStackCategory("stackCategory2")));

		Recruitment recruitment = RecruitmentFixture.createRecruitment(study, RECRUITMENT_TITLE, CONTENT, 5, CALL_URL,
				null, spring, react);

		userRepository.save(user);
		categoryRepository.save(category);
		studyRepository.save(study);
		return recruitmentRepository.save(recruitment);
	}

}
