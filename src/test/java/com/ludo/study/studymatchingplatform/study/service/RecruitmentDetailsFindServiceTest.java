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
		System.out.println(saveRecruitment);
		System.out.println(saveRecruitment.getId());
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
		Study study = StudyFixture.createStudy(StudyStatus.RECRUITING, STUDY_TITLE, Way.ONLINE, category, user);

		RecruitmentStack spring = RecruitmentStackFixture.createRecruitmentStack(
			StackFixture.createStack(STACK_SPRING));
		RecruitmentStack react = RecruitmentStackFixture.createRecruitmentStack(
			StackFixture.createStack(STACK_REACT));

		Recruitment recruitment = RecruitmentFixture.createRecruitment(study, RECRUITMENT_TITLE, CONTENT, 5, CALL_URL,
			spring, react);

		userRepository.save(user);
		categoryRepository.save(category);
		studyRepository.save(study);
		return recruitmentRepository.save(recruitment);
	}

	@Test
	void 모집공고_상세_테스트데이터를_조회한다() {
		RecruitmentDetailsResponse recruitmentDetailsResponse = recruitmentDetailsFindService.findRecruitmentDetails(
			1L);
		assertStudyInfo(recruitmentDetailsResponse);
		assertRecruitmentInfo(recruitmentDetailsResponse);
	}

	void assertStudyInfo(RecruitmentDetailsResponse recruitmentDetailsResponse) {
		assertThat(recruitmentDetailsResponse.ownerNickname()).isEqualTo(NICKNAME);
		assertThat(recruitmentDetailsResponse.category()).isEqualTo(CATEGORY);
		assertThat(recruitmentDetailsResponse.way()).isEqualTo(Way.ONLINE.toString());
		assertThat(recruitmentDetailsResponse.startDateTime()).isEqualTo("2025-03-01T18:00");
		assertThat(recruitmentDetailsResponse.endDateTime()).isEqualTo("2025-03-20T18:00");
	}

	void assertRecruitmentInfo(RecruitmentDetailsResponse recruitmentDetailsResponse) {
		assertThat(recruitmentDetailsResponse.title()).isEqualTo("Recruitment Title 1");
		assertThat(recruitmentDetailsResponse.content()).isEqualTo("Content 1");
		assertThat(recruitmentDetailsResponse.stacks()).contains("spring", "react");
		assertThat(recruitmentDetailsResponse.positions()).contains("백엔드", "프론트엔드");
		assertThat(recruitmentDetailsResponse.platformUrl()).isEqualTo("https://example.com/call1");
		assertThat(recruitmentDetailsResponse.applicantCount()).isSameAs(5);
		assertThat(recruitmentDetailsResponse.recruitmentEndDateTime()).isEqualTo("2025-02-10T18:00");
		assertThat(recruitmentDetailsResponse.createdDateTime()).isEqualTo("2025-01-20T12:30");
	}

}
