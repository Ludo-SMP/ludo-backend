package com.ludo.study.studymatchingplatform.study.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentPreviewResponse;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

@SpringBootTest
class RecruitmentsFindServiceTest {

	private static final int TOTAL_RECRUITMENT = 45;
	private static final int DEFAULT_PAGING_SIZE = 20;
	private static final int REMAIN_PAGING_SIZE = 5;

	@Autowired
	RecruitmentsFindService recruitmentsFindService;

	@Autowired
	RecruitmentRepositoryImpl recruitmentRepository;

	@Autowired
	StudyRepositoryImpl studyRepository;

	@Autowired
	UserRepositoryImpl userRepository;

	@Autowired
	CategoryRepositoryImpl categoryRepository;

	@BeforeEach
	void init() {
		User user = saveUser();
		Category category = saveCategory();
		saveRecruitments(category, user);
	}

	@Test
	@Transactional
	void 모집공고를_20개씩_조회한다() {
		List<RecruitmentPreviewResponse> firstSearchResults = recruitmentsFindService.findRecruitments(null, 20);
		assertThat(firstSearchResults).hasSize(DEFAULT_PAGING_SIZE);

		Long lastId = getLastId(firstSearchResults);
		List<RecruitmentPreviewResponse> secondSearchResults = recruitmentsFindService.findRecruitments(lastId, 20);
		assertThat(secondSearchResults).hasSize(DEFAULT_PAGING_SIZE);

		lastId = getLastId(secondSearchResults);
		List<RecruitmentPreviewResponse> thirdSearchResults = recruitmentsFindService.findRecruitments(lastId, 20);
		assertThat(thirdSearchResults).hasSize(REMAIN_PAGING_SIZE);
	}

	@Test
	@Transactional
	void 모집공고를_생성날짜기준_내림차순_조회한다() {
		List<RecruitmentPreviewResponse> firstSearchResults = recruitmentsFindService.findRecruitments(null, 20);
		assertThat(firstSearchResults)
			.extracting("title")
			.containsExactly(
				"모집공고45", "모집공고44", "모집공고43", "모집공고42", "모집공고41",
				"모집공고40", "모집공고39", "모집공고38", "모집공고37", "모집공고36",
				"모집공고35", "모집공고34", "모집공고33", "모집공고32", "모집공고31",
				"모집공고30", "모집공고29", "모집공고28", "모집공고27", "모집공고26");

		Long lastId = getLastId(firstSearchResults);
		List<RecruitmentPreviewResponse> secondSearchResults = recruitmentsFindService.findRecruitments(lastId, 20);
		assertThat(secondSearchResults)
			.extracting("title")
			.containsExactly("모집공고25", "모집공고24", "모집공고23", "모집공고22", "모집공고21",
				"모집공고20", "모집공고19", "모집공고18", "모집공고17", "모집공고16",
				"모집공고15", "모집공고14", "모집공고13", "모집공고12", "모집공고11",
				"모집공고10", "모집공고9", "모집공고8", "모집공고7", "모집공고6");

		lastId = getLastId(secondSearchResults);
		List<RecruitmentPreviewResponse> thirdSearchResults = recruitmentsFindService.findRecruitments(lastId, 20);
		assertThat(thirdSearchResults)
			.extracting("title")
			.containsExactly("모집공고5", "모집공고4", "모집공고3", "모집공고2", "모집공고1");
	}

	private Long getLastId(List<RecruitmentPreviewResponse> searchResults) {
		return searchResults.get(searchResults.size() - 1).id();
	}

	private User saveUser() {
		User user = UserFixture.createUser(Social.GOOGLE, "아카", "archa@gmail.com");
		return userRepository.save(user);
	}

	private Category saveCategory() {
		Category project = CategoryFixture.createCategory("프로젝트");
		return categoryRepository.save(project);
	}

	private void saveRecruitments(Category category, User user) {
		for (int count = 1; count <= TOTAL_RECRUITMENT; count++) {
			Study study = saveStudy(category, user, count);
			RecruitmentStack spring = createRecruitmentStack();
			saveRecruitment(count, study, spring);
		}
	}

	private Study saveStudy(Category category, User user, int count) {
		String studyTitle = "스터디" + count;
		Study study = StudyFixture.createStudy(StudyStatus.RECRUITING, studyTitle, Way.ONLINE, category, user);
		studyRepository.save(study);
		return study;
	}

	private RecruitmentStack createRecruitmentStack() {
		return RecruitmentStackFixture.createRecruitmentStack(
			StackFixture.createStack("spring")
		);
	}

	private void saveRecruitment(int count, Study study, RecruitmentStack spring) {
		String recruitmentTitle = "모집공고" + count;
		Recruitment recruitment = RecruitmentFixture.createRecruitment(study, recruitmentTitle, "내용", 1, "call",
			spring);
		recruitmentRepository.save(recruitment);
	}
}