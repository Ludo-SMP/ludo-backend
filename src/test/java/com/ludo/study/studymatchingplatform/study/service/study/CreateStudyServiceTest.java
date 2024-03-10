package com.ludo.study.studymatchingplatform.study.service.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.study.StudyCreateService;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

@SpringBootTest
@Transactional
class CreateStudyServiceTest {

	@Autowired
	private UserRepositoryImpl userRepository;

	@Autowired
	private CategoryRepositoryImpl categoryRepository;

	@Autowired
	private StudyCreateService studyCreateService;

	@Autowired
	private PositionRepositoryImpl positionRepository;

	// @Test
	// void 사용자는_스터디를_생성할_수_있어야_한다() {
	// 	// given
	// 	final User owner = userRepository.save(
	// 			User.builder()
	// 					.nickname("owner")
	// 					.email("aaa@kakao.com")
	// 					.social(Social.KAKAO)
	// 					.build());
	//
	// 	final Category category = categoryRepository.save(
	// 			CategoryFixture.createCategory("category1"));
	//
	// 	// 포지션 생성
	// 	final Position position1 = positionRepository.save(
	// 			PositionFixture.createPosition("position")
	// 	);
	//
	// 	final WriteStudyRequest request = WriteStudyRequest.builder()
	// 			.title("create study test.")
	// 			.categoryId(category.getId())
	// 			.positionId(position1.getId())
	// 			.way(Way.ONLINE)
	// 			.platform(Platform.GATHER)
	// 			.participantLimit(3)
	// 			.startDateTime(LocalDateTime.now())
	// 			.endDateTime(LocalDateTime.now().plusMonths(3))
	// 			.build();
	//
	// 	// when
	// 	final Study study = studyCreateService.create(request, owner);
	//
	// 	// then
	// 	assertThat(study.getTitle()).isEqualTo("create study test.");
	// 	assertThat(study.getOwner().getId()).isEqualTo(owner.getId());
	// 	assertThat(study.getCategory().getId()).isEqualTo(1);
	// 	assertThat(study.getWay()).isEqualTo(Way.ONLINE);
	// 	assertThat(study.getStatus()).isEqualTo(StudyStatus.RECRUITING);
	// 	assertThat(study.getPlatform()).isEqualTo(Platform.GATHER);
	// 	assertThat(study.getParticipantLimit()).isEqualTo(3);
	// 	assertThat(study.getParticipantCount()).isEqualTo(1);
	// 	assertThat(study.getParticipants().size()).isEqualTo(1);
	// 	assertThat(study.getRecruitment()).isNull();
	// }

}
