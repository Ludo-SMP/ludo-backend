package com.ludo.study.studymatchingplatform.study.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Platform;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;
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
import com.ludo.study.studymatchingplatform.study.service.dto.mapper.RecruitmentPreviewResponseMapper;
import com.ludo.study.studymatchingplatform.study.service.dto.response.PopularRecruitmentsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponse;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

@SpringBootTest
@Transactional
class PopularRecruitmentsFindServiceTest {

	@Autowired
	PopularRecruitmentsFindService popularRecruitmentsFindService;

	@Autowired
	RecruitmentRepositoryImpl recruitmentRepository;

	@Autowired
	StudyRepositoryImpl studyRepository;

	@Autowired
	UserRepositoryImpl userRepository;

	@Autowired
	CategoryRepositoryImpl categoryRepository;

	@Autowired
	RecruitmentPreviewResponseMapper recruitmentPreviewResponseMapper;

	@BeforeEach
	void init() {
		User user = UserFixture.createUser(Social.GOOGLE, "아카", "hihi@google.com");
		Category project = CategoryFixture.createCategory("프로젝트");
		Category algorithm = CategoryFixture.createCategory("코딩테스트");
		Category interview = CategoryFixture.createCategory("모의면접");

		StackCategory backend = StackCategoryFixture.createStackCategory("백엔드");
		RecruitmentStack spring = RecruitmentStackFixture.createRecruitmentStack(
				StackFixture.createStack("spring", backend)
		);
		userRepository.save(user);
		categoryRepository.save(project);
		categoryRepository.save(algorithm);
		categoryRepository.save(interview);

		Study studyA = StudyFixture.createStudy(StudyStatus.RECRUITING, "스터디A", Way.ONLINE, project, user, 5, 5,
				Platform.GATHER);
		Study studyB = StudyFixture.createStudy(StudyStatus.RECRUITING, "스터디B", Way.ONLINE, project, user, 5, 5,
				Platform.GATHER);
		Study studyC = StudyFixture.createStudy(StudyStatus.RECRUITING, "스터디C", Way.ONLINE, project, user, 5, 5,
				Platform.GATHER);
		Study studyD = StudyFixture.createStudy(StudyStatus.RECRUITING, "스터디D", Way.ONLINE, project, user, 5, 5,
				Platform.GATHER);
		Study studyE = StudyFixture.createStudy(StudyStatus.RECRUITING, "스터디E", Way.ONLINE, algorithm, user, 5, 5,
				Platform.GATHER);
		Study studyF = StudyFixture.createStudy(StudyStatus.RECRUITING, "스터디F", Way.ONLINE, algorithm, user, 5, 5,
				Platform.GATHER);
		Study studyG = StudyFixture.createStudy(StudyStatus.RECRUITING, "스터디G", Way.ONLINE, interview, user, 5, 5,
				Platform.GATHER);

		Recruitment recruitmentA = RecruitmentFixture.createRecruitment(studyA, "모집공고A", "내용입니다.", 499, "call_url",
				null);
		Recruitment recruitmentB = RecruitmentFixture.createRecruitment(studyB, "모집공고B", "내용입니다.", 500, "call_url",
				null);
		Recruitment recruitmentC = RecruitmentFixture.createRecruitment(studyC, "모집공고C", "내용입니다.", 501, "call_url",
				null);
		Recruitment recruitmentD = RecruitmentFixture.createRecruitment(studyD, "모집공고D", "내용입니다.", 498, "call_url",
				null);
		Recruitment recruitmentE = RecruitmentFixture.createRecruitment(studyE, "모집공고E", "내용입니다.", 799, "call_url",
				null);
		Recruitment recruitmentF = RecruitmentFixture.createRecruitment(studyF, "모집공고F", "내용입니다.", 800, "call_url",
				null);
		Recruitment recruitmentG = RecruitmentFixture.createRecruitment(studyG, "모집공고G", "내용입니다.", 777, "call_url",
				null);

		studyRepository.save(studyA);
		studyRepository.save(studyB);
		studyRepository.save(studyC);
		studyRepository.save(studyD);
		studyRepository.save(studyE);
		studyRepository.save(studyF);
		studyRepository.save(studyG);
		recruitmentRepository.save(recruitmentA);
		recruitmentRepository.save(recruitmentB);
		recruitmentRepository.save(recruitmentC);
		recruitmentRepository.save(recruitmentD);
		recruitmentRepository.save(recruitmentE);
		recruitmentRepository.save(recruitmentF);
		recruitmentRepository.save(recruitmentG);
	}

	@Test
	void 인기있는_프로젝트_모집공고_조회() {
		PopularRecruitmentsResponse result = popularRecruitmentsFindService.findPopularRecruitments();
		List<RecruitmentPreviewResponse> popularProjectRecruitments = result.popularProjectRecruitments();

		assertThat(popularProjectRecruitments)
				.size()
				.isLessThanOrEqualTo(3);

		assertThat(popularProjectRecruitments)
				.extracting("title")
				.containsExactly("모집공고C", "모집공고B", "모집공고A");
	}

	@Test
	void 인기있는_코딩테스트_모집공고_조회() {
		PopularRecruitmentsResponse result = popularRecruitmentsFindService.findPopularRecruitments();
		List<RecruitmentPreviewResponse> popularCodingRecruitments = result.popularCodingRecruitments();

		assertThat(popularCodingRecruitments)
				.size()
				.isLessThanOrEqualTo(3);

		assertThat(popularCodingRecruitments)
				.extracting("title")
				.containsExactly("모집공고F", "모집공고E");
	}

	@Test
	void 인기있는_모의면접_모집공고_조회() {
		PopularRecruitmentsResponse result = popularRecruitmentsFindService.findPopularRecruitments();
		List<RecruitmentPreviewResponse> popularInterviewRecruitments = result.popularInterviewRecruitments();

		assertThat(popularInterviewRecruitments)
				.size()
				.isLessThanOrEqualTo(3);

		assertThat(popularInterviewRecruitments)
				.extracting("title")
				.containsExactly("모집공고G");
	}

}