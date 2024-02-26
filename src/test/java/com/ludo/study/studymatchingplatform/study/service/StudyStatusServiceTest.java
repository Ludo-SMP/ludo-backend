package com.ludo.study.studymatchingplatform.study.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.fixture.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.UserFixture;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

@SpringBootTest
class StudyStatusServiceTest {

	@Autowired
	private UserRepositoryImpl userRepository;

	@Autowired
	private CategoryRepositoryImpl categoryRepository;

	@Autowired
	private StudyRepositoryImpl studyRepository;

	@Autowired
	private RecruitmentRepositoryImpl recruitmentRepository;

	@Autowired
	private StudyStatusService studyStatusService;

	@Test
	@Transactional
	void 스터디장은_스터디의_상태를_변경할_수_있다() {

		// given
		final User owner = userRepository.save(
				UserFixture.createUser(Social.KAKAO, "owner", "aaa@kakao.com"));

		final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

		final Study study = studyRepository.save(
				StudyFixture.createStudy("study", category, owner, 4));

		assertThat(study.getStatus()).isEqualTo(StudyStatus.RECRUITING);

		// when
		final Study updatedStudy = studyStatusService.changeStatus(
				study.getId(), StudyStatus.PROGRESS, owner);

		// then
		assertThat(updatedStudy.getStatus()).isEqualTo(StudyStatus.PROGRESS);
	}

	@Test
	@Transactional
	void 스터디장이_아닌_사용자는_스터디의_상태를_변경할_수_없다() {

		// given
		final User owner = userRepository.save(
				UserFixture.createUser(Social.KAKAO, "owner", "aaa@kakao.com"));

		final User user = userRepository.save(
				UserFixture.createUser(Social.GOOGLE, "user", "bbb@google.com"));

		final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

		final Study study = studyRepository.save(
				StudyFixture.createStudy("study", category, owner, 4));

		assertThat(study.getStatus()).isEqualTo(StudyStatus.RECRUITING);

		// when
		// then
		assertThatThrownBy(() ->
				studyStatusService.changeStatus(study.getId(), StudyStatus.PROGRESS, user))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("스터디를 수정할 권한이 없습니다.");
	}

	@Test
	@Transactional
	void 존재하지_않는_스터디의_상태를_변경할_수_없다() {

		// given
		final User owner = userRepository.save(
				UserFixture.createUser(Social.KAKAO, "owner", "aaa@kakao.com"));

		final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

		final Study study = studyRepository.save(
				StudyFixture.createStudy("study", category, owner, 4));

		assertThat(study.getStatus()).isEqualTo(StudyStatus.RECRUITING);

		// when
		// then
		assertThatThrownBy(() ->
				studyStatusService.changeStatus(study.getId() + 1, StudyStatus.PROGRESS, owner))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("존재하지 않는 스터디 입니다.");
	}

	@Test
	@Transactional
	void 스터디의_상태가_진행중_으로_변경되면_모집공고를_비활성화_한다() {

		// given
		final User owner = userRepository.save(
				UserFixture.createUser(Social.KAKAO, "owner", "aaa@kakao.com"));

		final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

		final Study study = studyRepository.save(
				StudyFixture.createStudy("study", category, owner, 4));

		final Recruitment recruitment = recruitmentRepository.save(
				RecruitmentFixture.createRecruitmentWithoutStacksAndPositions(
						study, "test", "content", "www.aaa.com", null));

		// when
		final Study updatedStudy = studyStatusService.changeStatus(
				study.getId(), StudyStatus.PROGRESS, owner);

		// assertThat(study.getStatus()).isEqualTo(StudyStatus.RECRUITING);

		// then
		assertThat(updatedStudy.getRecruitment().getDeletedDateTime()).isNotEqualTo(null);
	}

}
