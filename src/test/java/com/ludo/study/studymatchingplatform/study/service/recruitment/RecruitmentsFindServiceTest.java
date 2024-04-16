package com.ludo.study.studymatchingplatform.study.service.recruitment;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.RecruitmentStackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCond;
import com.ludo.study.studymatchingplatform.study.repository.dto.request.RecruitmentFindCursor;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentPreviewResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import jakarta.persistence.EntityManager;

@SpringBootTest
@ActiveProfiles("test")
class RecruitmentsFindServiceTest {

	private static final int TOTAL_RECRUITMENT = 45;
	private static final int DEFAULT_PAGING_SIZE = 21;
	private static final int REMAIN_PAGING_SIZE = 3;

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

	@Autowired
	PositionRepositoryImpl positionRepository;

	@Autowired
	StackCategoryRepositoryImpl stackCategoryRepository;

	@Autowired
	StackRepositoryImpl stackRepository;

	@Autowired
	EntityManager em;

	@Nested
	@DisplayName("검색 필터 조건이 없는 경우 모집 공고 조회 테스트")
	class RecruitmentFindNoFilterCond {
		@BeforeEach
		void init() {
			User user = saveUser();
			Category category = saveCategory();
			saveRecruitments(category, user);
			em.flush();
			em.clear();
		}

		@Test
		@Transactional
		@DisplayName("[Success] 모집 공고를 페이징 크기 만큼 조회")
		void findRecruitmentsPagingSize() {
			RecruitmentFindCursor recruitmentFindCursor1 = new RecruitmentFindCursor(null, DEFAULT_PAGING_SIZE);
			RecruitmentFindCond recruitmentFindCond = new RecruitmentFindCond(null, null, null, null);

			List<RecruitmentPreviewResponse> firstSearchResults = recruitmentsFindService
					.findRecruitments(recruitmentFindCursor1, recruitmentFindCond);
			assertThat(firstSearchResults).hasSize(DEFAULT_PAGING_SIZE);

			Long lastId = getLastId(firstSearchResults);
			RecruitmentFindCursor recruitmentFindCursor2 = new RecruitmentFindCursor(lastId, DEFAULT_PAGING_SIZE);
			List<RecruitmentPreviewResponse> secondSearchResults = recruitmentsFindService
					.findRecruitments(recruitmentFindCursor2, recruitmentFindCond);
			assertThat(secondSearchResults).hasSize(DEFAULT_PAGING_SIZE);

			lastId = getLastId(secondSearchResults);
			RecruitmentFindCursor recruitmentFindCursor3 = new RecruitmentFindCursor(lastId, DEFAULT_PAGING_SIZE);
			List<RecruitmentPreviewResponse> thirdSearchResults = recruitmentsFindService
					.findRecruitments(recruitmentFindCursor3, recruitmentFindCond);
			assertThat(thirdSearchResults).hasSize(REMAIN_PAGING_SIZE);
		}

		@Test
		@Transactional
		@DisplayName("[Success] 모집 공고 수정 날짜 기준 내림차순 조회")
		void findRecruitmentsByRecentlyUpdate() {
			RecruitmentFindCursor recruitmentFindCursor1 = new RecruitmentFindCursor(null, DEFAULT_PAGING_SIZE);
			RecruitmentFindCond recruitmentFindCond = new RecruitmentFindCond(null, null, null, null);
			List<RecruitmentPreviewResponse> firstSearchResults = recruitmentsFindService
					.findRecruitments(recruitmentFindCursor1, recruitmentFindCond);
			assertThat(firstSearchResults)
					.extracting("title")
					.containsExactly(
							"모집공고45", "모집공고44", "모집공고43", "모집공고42", "모집공고41",
							"모집공고40", "모집공고39", "모집공고38", "모집공고37", "모집공고36",
							"모집공고35", "모집공고34", "모집공고33", "모집공고32", "모집공고31",
							"모집공고30", "모집공고29", "모집공고28", "모집공고27", "모집공고26", "모집공고25");

			Long lastId = getLastId(firstSearchResults);
			RecruitmentFindCursor recruitmentFindCursor2 = new RecruitmentFindCursor(lastId, DEFAULT_PAGING_SIZE);
			List<RecruitmentPreviewResponse> secondSearchResults = recruitmentsFindService
					.findRecruitments(recruitmentFindCursor2, recruitmentFindCond);
			assertThat(secondSearchResults)
					.extracting("title")
					.containsExactly(
							"모집공고24", "모집공고23", "모집공고22", "모집공고21", "모집공고20",
							"모집공고19", "모집공고18", "모집공고17", "모집공고16", "모집공고15",
							"모집공고14", "모집공고13", "모집공고12", "모집공고11", "모집공고10",
							"모집공고9", "모집공고8", "모집공고7", "모집공고6", "모집공고5", "모집공고4");

			lastId = getLastId(secondSearchResults);
			RecruitmentFindCursor recruitmentFindCursor3 = new RecruitmentFindCursor(lastId, DEFAULT_PAGING_SIZE);
			List<RecruitmentPreviewResponse> thirdSearchResults = recruitmentsFindService.findRecruitments(
					recruitmentFindCursor3, recruitmentFindCond);
			assertThat(thirdSearchResults)
					.extracting("title")
					.containsExactly("모집공고3", "모집공고2", "모집공고1");
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
			Study study = StudyFixture.createStudy(StudyStatus.RECRUITING, studyTitle, Way.ONLINE, category, user, 0, 5,
					Platform.GATHER);
			studyRepository.save(study);
			return study;
		}

		private RecruitmentStack createRecruitmentStack() {
			StackCategory backend = StackCategoryFixture.createStackCategory("백엔드");
			return RecruitmentStackFixture.createRecruitmentStack(
					StackFixture.createStack("spring", backend)
			);
		}

		private void saveRecruitment(int count, Study study, RecruitmentStack spring) {
			String recruitmentTitle = "모집공고" + count;
			Recruitment recruitment = RecruitmentFixture.createRecruitment(study, recruitmentTitle, "내용", 1, "call",
					null);
			recruitmentRepository.save(recruitment);
		}

	}

}
