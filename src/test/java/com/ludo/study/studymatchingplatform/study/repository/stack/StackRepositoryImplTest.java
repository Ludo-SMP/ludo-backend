package com.ludo.study.studymatchingplatform.study.repository.stack;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.fixture.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StackFixture;

@SpringBootTest
class StackRepositoryImplTest {

	@Autowired
	StackCategoryRepositoryImpl stackCategoryRepository;

	@Autowired
	StackRepositoryImpl stackRepository;

	@Test
	@Transactional
	void findAllTest() {
		// given - 현 비즈니스 요구사항에 부합하는 기술스택 카테고리
		StackCategory backend = StackCategoryFixture.createStackCategory("백엔드");
		StackCategory frontend = StackCategoryFixture.createStackCategory("프론트엔드");
		StackCategory database = StackCategoryFixture.createStackCategory("데이터베이스");
		StackCategory language = StackCategoryFixture.createStackCategory("언어");
		StackCategory devops = StackCategoryFixture.createStackCategory("데브옵스");
		// given - 현 비즈니스 요구사항 이외의 기술스택 카테고리
		StackCategory data = StackCategoryFixture.createStackCategory("데이터");
		StackCategory tool = StackCategoryFixture.createStackCategory("협업툴");
		StackCategory testing = StackCategoryFixture.createStackCategory("테스팅툴");
		saveAllStackCategory(backend, frontend, database, language, devops, data, tool, testing);

		Stack spring = StackFixture.createStack("spring", backend);
		Stack nextJs = StackFixture.createStack("nextJs", frontend);
		Stack mySql = StackFixture.createStack("mySQL", database);
		Stack java = StackFixture.createStack("java", language);
		Stack jenkins = StackFixture.createStack("jenkins", devops);
		Stack airflow = StackFixture.createStack("airflow", data);
		Stack confluence = StackFixture.createStack("confluence", tool);
		Stack junit = StackFixture.createStack("junit", testing);
		saveAllStack(spring, nextJs, mySql, java, jenkins, airflow, confluence, junit);

		List<Stack> stacks = stackRepository.findAll();
		assertThat(stacks)
				.extracting("name")
				.contains("spring", "nextJs", "mySQL", "java", "jenkins");

		assertThat(stacks)
				.extracting("name")
				.doesNotContain("airflow", "confluence", "junit");
	}

	private void saveAllStackCategory(StackCategory... stackCategories) {
		for (StackCategory stackCategory : stackCategories) {
			stackCategoryRepository.save(stackCategory);
		}
	}

	private void saveAllStack(Stack... stacks) {
		for (Stack stack : stacks) {
			stackRepository.save(stack);
		}
	}

}