package com.ludo.study.studymatchingplatform.study.repository.stack;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.StackCategory;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackCategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.stack.StackFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackCategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;

@SpringBootTest
class StackRepositoryImplTest {

	@Autowired
	StackCategoryRepositoryImpl stackCategoryRepository;

	@Autowired
	StackRepositoryImpl stackRepository;

	@ParameterizedTest
	@CsvSource(value = {"백엔드:spring", "프론트엔드:nextJs", "데이터베이스:mySql", "언어:java", "데브옵스:jenkins"}, delimiter = ':')
	@Transactional
	void 현재_비즈니스_요구사항에_포함되는_기술스택은_조회를_성공한다(String stackCategoryName, String stackName) {
		// given
		StackCategory stackCategory = StackCategoryFixture.createStackCategory(stackCategoryName);
		Stack stack = StackFixture.createStack(stackName, stackCategory);
		saveAllStackCategory(stackCategory);
		saveAllStack(stack);
		// when
		List<Stack> stacks = stackRepository.findAll();
		// then
		assertThat(stacks)
				.extracting("name")
				.contains(stackName);
	}

	@ParameterizedTest
	@CsvSource(value = {"데이터:airflow", "협업툴:confluence", "테스팅툴:junit"}, delimiter = ':')
	@Transactional
	void 현재_비즈니스_요구사항에_포함되지_않는_기술스택은_조회하지_않는다(String notIncludedStackCategoryName, String stackName) {
		// given
		StackCategory notIncludedStackCategory = StackCategoryFixture.createStackCategory(notIncludedStackCategoryName);
		Stack stack = StackFixture.createStack(stackName, notIncludedStackCategory);
		saveAllStackCategory(notIncludedStackCategory);
		saveAllStack(stack);
		// when
		List<Stack> stacks = stackRepository.findAll();
		// then
		assertThat(stacks)
				.extracting("name")
				.doesNotContain(stackName);
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
