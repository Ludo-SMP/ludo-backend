package com.ludo.study.studymatchingplatform.study.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class RecruitmentControllerTest {

	// @Autowired
	// private MockMvc mockMvc;
	//
	// @Autowired
	// private ObjectMapper mapper;
	//
	// @Autowired
	// private UserRepositoryImpl userRepository;
	//
	// @Autowired
	// private CategoryRepositoryImpl categoryRepository;
	//
	// @Autowired
	// private StudyRepositoryImpl studyRepository;
	//
	// @Autowired
	// private PositionRepositoryImpl positionRepository;
	//
	// @Autowired
	// private StackRepositoryImpl stackRepository;
	//
	// @Autowired
	// private StackCategoryRepositoryImpl stackCategoryRepository;
	//
	// @Autowired
	// private JwtTokenProvider jwtTokenProvider;
	//
	// @Autowired
	// private CookieProvider cookieProvider;
	//
	// @Autowired
	// private RecruitmentRepositoryImpl recruitmentRepository;
	//
	// @Autowired
	// private RecruitmentService recruitmentService;
	//
	// @DisplayName("[Success] Write recruitment")
	// @Test
	// @Transactional
	// void writeRecruitment() throws Exception {
	// 	final User user = UserFixture.createUser(Social.GOOGLE, "user", "email@gmail.com");
	// 	userRepository.save(user);
	//
	// 	final List<Category> categories = List.of(
	// 			CategoryFixture.createCategory("category1"),
	// 			CategoryFixture.createCategory("category2"),
	// 			CategoryFixture.createCategory("category3")
	// 	);
	// 	categories.forEach(category -> categoryRepository.save(category));
	//
	// 	final List<StackCategory> stackCategories = List.of(
	// 			StackCategoryFixture.createStackCategory("stackCategory1"),
	// 			StackCategoryFixture.createStackCategory("stackCategory2"),
	// 			StackCategoryFixture.createStackCategory("stackCategory3")
	// 	);
	// 	stackCategories.forEach(stackCategory -> stackCategoryRepository.save(stackCategory));
	//
	// 	final List<Stack> stacks = List.of(
	// 			StackFixture.createStack("stack1", stackCategories.get(0)),
	// 			StackFixture.createStack("stack2", stackCategories.get(1)),
	// 			StackFixture.createStack("stack3", stackCategories.get(2))
	// 	);
	// 	stacks.forEach(stack -> stackRepository.save(stack));
	//
	// 	final List<Position> positions = List.of(
	// 			PositionFixture.createPosition("position1"),
	// 			PositionFixture.createPosition("position2"),
	// 			PositionFixture.createPosition("position3")
	// 	);
	// 	positions.forEach(position -> positionRepository.save(position));
	//
	// 	final Study study =
	// 			StudyFixture.createStudy("study", categories.get(0), user, 3, Platform.GATHER);
	// 	studyRepository.save(study);
	//
	// 	final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user.getId()));
	// 	final Cookie authCookie = cookieProvider.createAuthCookie(accessToken, 300000);
	//
	// 	final WriteRecruitmentRequest body = WriteRecruitmentRequest.builder()
	// 			.title("recruitment")
	// 			.content("I want to study")
	// 			.stackIds(Set.of(stacks.get(0).getId(), stacks.get(1).getId(), stacks.get(2).getId()))
	// 			.positionIds(Set.of(positions.get(0).getId(), positions.get(1).getId(), positions.get(2).getId()))
	// 			.applicantCount(4)
	// 			.callUrl("gather")
	// 			.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
	// 			.build();
	//
	// 	mockMvc.perform(MockMvcRequestBuilders.post("/api/studies/" + study.getId() + "/recruitments")
	// 					.cookie(authCookie)
	// 					.contentType(MediaType.APPLICATION_JSON)
	// 					.content(mapper.writeValueAsString(body)))
	// 			.andExpect(status().isCreated())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.ok").value(true))
	// 			.andExpect(jsonPath("$.message").isString())
	// 			.andExpect(jsonPath("$.data.recruitment.id").isNumber())
	// 			.andExpect(jsonPath("$.data.recruitment.title").value("recruitment"))
	// 			.andExpect(jsonPath("$.data.recruitment.ownerNickname").value("user"))
	// 			.andExpect(jsonPath("$.data.recruitment.category").value("category1"))
	// 			.andExpect(jsonPath("$.data.recruitment.stacks").isArray())
	// 			.andExpect(jsonPath("$.data.recruitment.content").value("I want to study"));
	//
	// }
	//
	// @DisplayName("[Success] Edit recruitment")
	// @Test
	// @Transactional
	// void editRecruitment() throws Exception {
	// 	final User user = UserFixture.createUser(Social.GOOGLE, "user", "email@gmail.com");
	// 	userRepository.save(user);
	//
	// 	final List<Category> categories = List.of(
	// 			CategoryFixture.createCategory("category1"),
	// 			CategoryFixture.createCategory("category2"),
	// 			CategoryFixture.createCategory("category3")
	// 	);
	// 	categories.forEach(category -> categoryRepository.save(category));
	//
	// 	final List<StackCategory> stackCategories = List.of(
	// 			StackCategoryFixture.createStackCategory("stackCategory1"),
	// 			StackCategoryFixture.createStackCategory("stackCategory2"),
	// 			StackCategoryFixture.createStackCategory("stackCategory3")
	// 	);
	// 	stackCategories.forEach(stackCategory -> stackCategoryRepository.save(stackCategory));
	//
	// 	final List<Stack> stacks = List.of(
	// 			StackFixture.createStack("stack1", stackCategories.get(0)),
	// 			StackFixture.createStack("stack2", stackCategories.get(1)),
	// 			StackFixture.createStack("stack3", stackCategories.get(2))
	// 	);
	// 	stacks.forEach(stack -> stackRepository.save(stack));
	//
	// 	final List<Position> positions = List.of(
	// 			PositionFixture.createPosition("position1"),
	// 			PositionFixture.createPosition("position2"),
	// 			PositionFixture.createPosition("position3")
	// 	);
	// 	positions.forEach(position -> positionRepository.save(position));
	//
	// 	final Study study =
	// 			StudyFixture.createStudy("study2", categories.get(0), user, 3, Platform.GATHER);
	// 	studyRepository.save(study);
	//
	// 	final Recruitment recruitment = RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
	// 			study, "recruitment", "content", "",
	// 			LocalDateTime.now().plus(
	// 					Duration.ofDays(2)));
	// 	recruitmentRepository.save(recruitment);
	// 	study.registerRecruitment(recruitment);
	//
	// 	final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user.getId()));
	// 	final Cookie authCookie = cookieProvider.createAuthCookie(accessToken, 300000);
	//
	// 	final EditRecruitmentRequest body = EditRecruitmentRequest.builder()
	// 			.title("recruitment")
	// 			.content("I want to study")
	// 			.stackIds(Set.of(1L, 2L))
	// 			.positionIds(Set.of(3L))
	// 			.applicantCount(5)
	// 			.callUrl("xxx")
	// 			.recruitmentEndDateTime(LocalDateTime.now().plusMonths(5))
	// 			.build();
	//
	// 	mockMvc.perform(
	// 					MockMvcRequestBuilders.put("/api/studies/" + study.getId() + "/recruitments/" + recruitment.getId())
	// 							.cookie(authCookie)
	// 							.contentType(MediaType.APPLICATION_JSON)
	// 							.content(mapper.writeValueAsString(body)))
	// 			.andExpect(status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.ok").value(true))
	// 			.andExpect(jsonPath("$.message").isString())
	// 			.andExpect(jsonPath("$.data.recruitment.id").isNumber())
	// 			.andExpect(jsonPath("$.data.recruitment.title").value("recruitment"))
	// 			.andExpect(jsonPath("$.data.recruitment.ownerNickname").value("user"))
	// 			.andExpect(jsonPath("$.data.recruitment.category").value("category1"))
	// 			.andExpect(jsonPath("$.data.recruitment.stacks").isArray())
	// 			.andExpect(jsonPath("$.data.recruitment.content").value("I want to study"));
	//
	// }
	//
	// @DisplayName("[Success] Apply recruitment")
	// @Test
	// @Transactional
	// void applyRecruitment() throws Exception {
	// 	final User user = UserFixture.createUser(Social.GOOGLE, "user", "email@gmail.com");
	// 	userRepository.save(user);
	// 	final User owner = UserFixture.createUser(Social.GOOGLE, "owner", "owner@gmail.com");
	// 	userRepository.save(owner);
	//
	// 	final List<Category> categories = List.of(
	// 			CategoryFixture.createCategory("category1"),
	// 			CategoryFixture.createCategory("category2"),
	// 			CategoryFixture.createCategory("category3")
	// 	);
	// 	categories.forEach(category -> categoryRepository.save(category));
	//
	// 	final List<StackCategory> stackCategories = List.of(
	// 			StackCategoryFixture.createStackCategory("stackCategory1"),
	// 			StackCategoryFixture.createStackCategory("stackCategory2"),
	// 			StackCategoryFixture.createStackCategory("stackCategory3")
	// 	);
	// 	stackCategories.forEach(stackCategory -> stackCategoryRepository.save(stackCategory));
	//
	// 	final List<Stack> stacks = List.of(
	// 			StackFixture.createStack("stack1", stackCategories.get(0)),
	// 			StackFixture.createStack("stack2", stackCategories.get(1)),
	// 			StackFixture.createStack("stack3", stackCategories.get(2))
	// 	);
	// 	stacks.forEach(stack -> stackRepository.save(stack));
	//
	// 	final List<Position> positions = List.of(
	// 			PositionFixture.createPosition("position1"),
	// 			PositionFixture.createPosition("position2"),
	// 			PositionFixture.createPosition("position3")
	// 	);
	// 	positions.forEach(position -> positionRepository.save(position));
	//
	// 	final Study study =
	// 			StudyFixture.createStudy("study2", categories.get(0), owner, 3, Platform.GATHER);
	// 	studyRepository.save(study);
	//
	// 	final Recruitment recruitment = RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
	// 			study, "recruitment", "content", "",
	// 			LocalDateTime.now().plus(
	// 					Duration.ofDays(2)));
	// 	recruitmentRepository.save(recruitment);
	// 	study.registerRecruitment(recruitment);
	//
	// 	final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user.getId()));
	// 	final Cookie authCookie = cookieProvider.createAuthCookie(accessToken, 300000);
	// 	final ApplyRecruitmentRequest body = ApplyRecruitmentRequest.builder()
	// 			.positionId(1L)
	// 			.build();
	//
	// 	mockMvc.perform(MockMvcRequestBuilders.post("/api/studies/2/recruitments/2/apply")
	// 					.cookie(authCookie)
	// 					.contentType(MediaType.APPLICATION_JSON)
	// 					.content(mapper.writeValueAsString(body)))
	// 			.andExpect(status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.ok").value(true))
	// 			.andExpect(jsonPath("$.message").isString())
	// 			.andExpect(jsonPath("$.data.applicantId").isString());
	//
	// }
	//
	// @DisplayName("[Success] Delete recruitment")
	// @Test
	// @Transactional
	// void deleteRecruitment() throws Exception {
	//
	// 	final User user = UserFixture.createUser(Social.GOOGLE, "user", "email@gmail.com");
	// 	userRepository.save(user);
	//
	// 	final List<Category> categories = List.of(
	// 			CategoryFixture.createCategory("category1"),
	// 			CategoryFixture.createCategory("category2"),
	// 			CategoryFixture.createCategory("category3")
	// 	);
	// 	categories.forEach(category -> categoryRepository.save(category));
	//
	// 	final List<StackCategory> stackCategories = List.of(
	// 			StackCategoryFixture.createStackCategory("stackCategory1"),
	// 			StackCategoryFixture.createStackCategory("stackCategory2"),
	// 			StackCategoryFixture.createStackCategory("stackCategory3")
	// 	);
	// 	stackCategories.forEach(stackCategory -> stackCategoryRepository.save(stackCategory));
	//
	// 	final List<Stack> stacks = List.of(
	// 			StackFixture.createStack("stack1", stackCategories.get(0)),
	// 			StackFixture.createStack("stack2", stackCategories.get(1)),
	// 			StackFixture.createStack("stack3", stackCategories.get(2))
	// 	);
	// 	stacks.forEach(stack -> stackRepository.save(stack));
	//
	// 	final List<Position> positions = List.of(
	// 			PositionFixture.createPosition("position1"),
	// 			PositionFixture.createPosition("position2"),
	// 			PositionFixture.createPosition("position3")
	// 	);
	// 	positions.forEach(position -> positionRepository.save(position));
	//
	// 	final Study study =
	// 			StudyFixture.createStudy("study2", categories.get(0), user, 3, Platform.GATHER);
	// 	studyRepository.save(study);
	//
	// 	final Recruitment recruitment = RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
	// 			study, "recruitment", "content", "",
	// 			LocalDateTime.now().plus(
	// 					Duration.ofDays(2)));
	// 	recruitmentRepository.save(recruitment);
	// 	study.registerRecruitment(recruitment);
	//
	// 	final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user.getId()));
	// 	final Cookie authCookie = cookieProvider.createAuthCookie(accessToken, 300000);
	//
	// 	mockMvc.perform(
	// 					MockMvcRequestBuilders.delete("/api/studies/" + study.getId() + "/recruitments/" + recruitment.getId())
	// 							.cookie(authCookie)
	// 							.contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.ok").value(true))
	// 			.andExpect(jsonPath("$.message").isString());
	//
	// }
	//
	// @DisplayName("[Success] Cancel apply recruitment")
	// @Test
	// @Transactional
	// void cancelRecruitment() throws Exception {
	// 	final User user = UserFixture.createUser(Social.GOOGLE, "user", "email@gmail.com");
	// 	userRepository.save(user);
	// 	final User owner = UserFixture.createUser(Social.GOOGLE, "owner", "owner@gmail.com");
	// 	userRepository.save(owner);
	//
	// 	final List<Category> categories = List.of(
	// 			CategoryFixture.createCategory("category1"),
	// 			CategoryFixture.createCategory("category2"),
	// 			CategoryFixture.createCategory("category3")
	// 	);
	// 	categories.forEach(category -> categoryRepository.save(category));
	//
	// 	final List<StackCategory> stackCategories = List.of(
	// 			StackCategoryFixture.createStackCategory("stackCategory1"),
	// 			StackCategoryFixture.createStackCategory("stackCategory2"),
	// 			StackCategoryFixture.createStackCategory("stackCategory3")
	// 	);
	// 	stackCategories.forEach(stackCategory -> stackCategoryRepository.save(stackCategory));
	//
	// 	final List<Stack> stacks = List.of(
	// 			StackFixture.createStack("stack1", stackCategories.get(0)),
	// 			StackFixture.createStack("stack2", stackCategories.get(1)),
	// 			StackFixture.createStack("stack3", stackCategories.get(2))
	// 	);
	// 	stacks.forEach(stack -> stackRepository.save(stack));
	//
	// 	final List<Position> positions = List.of(
	// 			PositionFixture.createPosition("position1"),
	// 			PositionFixture.createPosition("position2"),
	// 			PositionFixture.createPosition("position3")
	// 	);
	// 	positions.forEach(position -> positionRepository.save(position));
	//
	// 	final Study study =
	// 			StudyFixture.createStudy("study2", categories.get(0), owner, 3, Platform.GATHER);
	// 	studyRepository.save(study);
	//
	// 	final Recruitment recruitment = RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
	// 			study, "recruitment", "content", "",
	// 			LocalDateTime.now().plus(
	// 					Duration.ofDays(2)));
	// 	recruitmentRepository.save(recruitment);
	// 	study.registerRecruitment(recruitment);
	//
	// 	final ApplyRecruitmentRequest request = ApplyRecruitmentRequest.builder()
	// 			.positionId(1L)
	// 			.build();
	// 	final Applicant applicant = recruitmentService.apply(user, recruitment.getId(), request);
	//
	// 	final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user.getId()));
	// 	final Cookie authCookie = cookieProvider.createAuthCookie(accessToken, 300000);
	//
	// 	mockMvc.perform(MockMvcRequestBuilders.post(
	// 							"/api/studies/" + study.getId() + "/recruitments/" + recruitment.getId() + "/cancel")
	// 					.cookie(authCookie))
	// 			.andExpect(status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.ok").value(true))
	// 			.andExpect(jsonPath("$.message").isString());
	//
	// }

}
