package com.ludo.study.studymatchingplatform.study.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.study.fixture.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

@SpringBootTest
class StudyCreateServiceTest {

	@Autowired
	private UserRepositoryImpl userRepository;

	@Autowired
	private CategoryRepositoryImpl categoryRepository;

	@Autowired
	private StudyCreateService studyCreateService;

	@Test
	void 로그인한_사용자는_스터디를_생성할_수_있다() {
		// given
		final User user = userRepository.save(
				User.builder()
						.nickname("owner")
						.email("aaa@gmail.com")
						.social(Social.GOOGLE)
						.build());

		final Category category = categoryRepository.save(CategoryFixture.createCategory("testCategory"));

		final WriteStudyRequest request = WriteStudyRequest.builder()
				.title("엄청 멋진 스터디")
				.categoryId(category.getId())
				.way(Way.ONLINE)
				.participantLimit(3)
				.startDateTime(LocalDateTime.of(2024, 3, 12, 16, 0, 0))
				.endDateTime(LocalDateTime.of(2024, 6, 12, 16, 0, 0))
				.build();

		// when
		final Study study = studyCreateService.create(request, user);

		// then
		assertThat(study.getStatus()).isEqualTo(StudyStatus.RECRUITING);
		assertThat(study.getCategory().getId()).isEqualTo(category.getId());
		assertThat(study.getOwner()).isEqualTo(user);
		assertThat(study.getTitle()).isEqualTo("엄청 멋진 스터디");
		assertThat(study.getRecruitment()).isNull();
		assertThat(study.getParticipants().get(0).getUser().getId()).isEqualTo(user.getId());
		assertThat(study.getParticipants().get(0).getStudy().getId()).isEqualTo(study.getId());
		assertThat(study.getParticipantLimit()).isEqualTo(3);
		assertThat(study.getParticipantCount()).isEqualTo(study.getParticipants().size());
		assertThat(study.getStartDateTime()).isEqualTo(
				LocalDateTime.of(2024, 3, 12, 16, 0, 0));
		assertThat(study.getEndDateTime()).isEqualTo(
				LocalDateTime.of(2024, 6, 12, 16, 0, 0));
	}

}
