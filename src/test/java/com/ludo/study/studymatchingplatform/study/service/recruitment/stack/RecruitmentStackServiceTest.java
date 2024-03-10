package com.ludo.study.studymatchingplatform.study.service.recruitment.stack;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.fixture.user.UserFixture;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

@SpringBootTest
class RecruitmentStackServiceTest {

	@Autowired
	private StackRepositoryImpl stackRepository;

	@Autowired
	private StackCategoryRepositoryImpl stackCategoryRepository;

	@Autowired
	private StudyRepositoryImpl studyRepository;

	@Autowired
	private UserRepositoryImpl userRepository;

	@Autowired
	private CategoryRepositoryImpl categoryRepository;

	@Autowired
	private RecruitmentRepositoryImpl recruitmentRepository;

	// TODO
	@Test
	void test() {
		// given
		final User user = UserFixture.createUser(Social.GOOGLE, "user", "user@gmail.com");
		final StackCategory stackCategory = StackCategoryFixture.createStackCategory("stackCategory");
		final Category category = CategoryFixture.createCategory("category");
		categoryRepository.save(category);
		userRepository.save(user);
		stackCategoryRepository.save(stackCategory);
		Stack stack1 = StackFixture.createStack("stack1", stackCategory);
		Stack stack2 = StackFixture.createStack("stack2", stackCategory);
		Stack stack3 = StackFixture.createStack("stack3", stackCategory);
		stackRepository.save(stack1);
		stackRepository.save(stack2);
		stackRepository.save(stack3);

		final Study study = StudyFixture.createStudy("study", category, user, 4, Platform.GATHER);
		studyRepository.save(study);

	}

}
