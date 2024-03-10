package com.ludo.study.studymatchingplatform.study.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
class RecruitmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

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
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private CookieProvider cookieProvider;

	private Cookie authCookie;

	@BeforeEach
	void beforeEach() {
		final User user = UserFixture.createUser(Social.GOOGLE, "user", "email@gmail.com");
		userRepository.save(user);

		final List<Category> categories = List.of(
				CategoryFixture.createCategory("category1"),
				CategoryFixture.createCategory("category2"),
				CategoryFixture.createCategory("category3")
		);
		categories.forEach(category -> categoryRepository.save(category));

		final List<StackCategory> stackCategories = List.of(
				StackCategoryFixture.createStackCategory("stackCategory1"),
				StackCategoryFixture.createStackCategory("stackCategory2"),
				StackCategoryFixture.createStackCategory("stackCategory3")
		);
		stackCategories.forEach(stackCategory -> stackCategoryRepository.save(stackCategory));

		final List<Stack> stacks = List.of(
				StackFixture.createStack("stack1", stackCategories.get(0)),
				StackFixture.createStack("stack2", stackCategories.get(1)),
				StackFixture.createStack("stack3", stackCategories.get(2))
		);
		stacks.forEach(stack -> stackRepository.save(stack));

		final List<Position> positions = List.of(
				PositionFixture.createPosition("position1"),
				PositionFixture.createPosition("position2"),
				PositionFixture.createPosition("position3")
		);
		positions.forEach(position -> positionRepository.save(position));

		final Study study =
				StudyFixture.createStudy("study", categories.get(0), user, 3, Platform.GATHER);
		studyRepository.save(study);

		final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user.getId()));
		authCookie = cookieProvider.createAuthCookie(accessToken, 300000);
	}

	// TODO
	// @DisplayName("write recruitment")
	// @Test
	// @Transactional
	// void writeRecruitment() throws Exception {
	//
	// 	final WriteRecruitmentRequest body = WriteRecruitmentRequest.builder()
	// 			.studyId(1L)
	// 			.title("recruitment")
	// 			.content("I want to study")
	// 			.stackIds(Set.of(1L, 2L, 3L))
	// 			.positionIds(Set.of(1L, 2L, 3L))
	// 			.recruitmentLimit(4)
	// 			.callUrl("x.com")
	// 			.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
	// 			.build();
	//
	// 	mockMvc.perform(MockMvcRequestBuilders.post("/api/studies/1/recruitments")
	// 					.cookie(authCookie)
	// 					.contentType(MediaType.APPLICATION_JSON)
	// 					.content(mapper.writeValueAsString(body)))
	// 			.andExpect(status().isCreated())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.id").value(1L))
	// 			.andExpect(jsonPath("$.title").value("recruitment"))
	// 			.andExpect(jsonPath("$.recruitmentLimit").value(4))
	// 			.andExpect(jsonPath("$.callUrl").value("x.com"))
	// 			.andExpect(jsonPath("$.content").value("I want to study"));
	//
	// }

}
