package com.ludo.study.studymatchingplatform.study.service.recruitment.position;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Contact;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.fixture.recruitment.position.PositionFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.study.category.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.recruitment.RecruitmentService;
import com.ludo.study.studymatchingplatform.user.domain.user.Social;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class RecruitmentPositionServiceTest {

	@Autowired
	private StudyRepositoryImpl studyRepository;

	@Autowired
	private UserRepositoryImpl userRepository;

	@Autowired
	private CategoryRepositoryImpl categoryRepository;

	@Autowired
	private PositionRepositoryImpl positionRepository;

	@Autowired
	private RecruitmentService recruitmentService;

	@Autowired
	private RecruitmentPositionService recruitmentPositionService;

	@DisplayName("[Success] 모집 공고에 등록하지 않은 포지션을 추가할 수 있다.")
	@Test
	void createRecruitmentPositionSuccess() {

		// given
		final User owner = userRepository.save(
				User.builder()
						.nickname("owner")
						.email("a@aa.aa")
						.social(Social.KAKAO)
						.build());

		final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

		final Study study = studyRepository.save(
				StudyFixture.createStudy(
						"study",
						category,
						owner,
						4,
						Platform.GATHER

				)
		);

		final WriteRecruitmentRequest request = WriteRecruitmentRequest.builder()
				.title("recruitment")
				.content("I want to study")
				.stackIds(Set.of())
				.positionIds(Set.of())
				.applicantCount(4)
				.contact(Contact.KAKAO)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

		final Recruitment recruitment = recruitmentService.write(owner, request, study.getId());

		final Position position1 = positionRepository.save(PositionFixture.createPosition("position1"));
		final Position position2 = positionRepository.save(PositionFixture.createPosition("position2"));
		final Position position3 = positionRepository.save(PositionFixture.createPosition("position3"));

		// when
		recruitmentPositionService.addMany(recruitment,
				Set.of(position1.getId(), position2.getId(), position3.getId()));
		final List<RecruitmentPosition> recruitmentPositions = recruitment.getRecruitmentPositions();

		// then
		assertThat(recruitmentPositions).hasSize(3);
		assertThat(recruitmentPositions).extracting("position")
				.containsAll(List.of(position1, position2, position3));

	}

	@DisplayName("[Exception] 모집 공고에 이미 등록된 포지션을 중복해서 추가하면 예외 발생")
	@Test
	void createRecruitmentPositionFailure() {

		// given
		final User owner = userRepository.save(
				User.builder()
						.nickname("owner")
						.email("a@aa.aa")
						.social(Social.KAKAO)
						.build());

		final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

		final Study study = studyRepository.save(
				StudyFixture.createStudy(
						"study",
						category,
						owner,
						4,
						Platform.GATHER
				)
		);

		final Position position1 = positionRepository.save(PositionFixture.createPosition("position1"));

		final WriteRecruitmentRequest request = WriteRecruitmentRequest.builder()
				.title("recruitment")
				.content("I want to study")
				.stackIds(Set.of())
				.positionIds(Set.of(position1.getId()))
				.applicantCount(4)
				.contact(Contact.KAKAO)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

		final Recruitment recruitment = recruitmentService.write(owner, request, study.getId());

		// when
		// then
		assertThatThrownBy(
				() -> recruitmentPositionService.addMany(recruitment,
						Set.of(position1.getId())))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("이미 존재하는 포지션입니다.");

	}

	@DisplayName("[Success] update 시, 모집 공고에 등록되지 않은 스택을 전달하면 추가된다.")
	@Test
	void updateRecruitmentStackSuccess1() {

		// given
		final User owner = userRepository.save(
				User.builder()
						.nickname("owner")
						.email("a@aa.aa")
						.social(Social.KAKAO)
						.build());

		final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

		final Study study = studyRepository.save(
				StudyFixture.createStudy(
						"study",
						category,
						owner,
						4,
						Platform.GATHER
				)
		);

		final Position position1 = positionRepository.save(PositionFixture.createPosition("position1"));
		final Position position2 = positionRepository.save(PositionFixture.createPosition("position2"));
		final Position position3 = positionRepository.save(PositionFixture.createPosition("position3"));

		final WriteRecruitmentRequest request = WriteRecruitmentRequest.builder()
				.title("recruitment")
				.content("I want to study")
				.stackIds(Set.of())
				.positionIds(Set.of())
				.applicantCount(4)
				.contact(Contact.KAKAO)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

		final Recruitment recruitment = recruitmentService.write(owner, request, study.getId());

		// when
		recruitmentPositionService.update(recruitment, Set.of(position1.getId(), position2.getId(), position3.getId()));
		final List<RecruitmentPosition> recruitmentPositions = recruitment.getRecruitmentPositions();

		// then
		assertThat(recruitmentPositions).hasSize(3);
		assertThat(recruitmentPositions).extracting("position")
				.containsAll(List.of(position1, position2, position3));

	}

	@DisplayName("[Success] update 시, 모집 공고에 등록된 포지션을 전달하지 않으면 제거된다.")
	@Test
	void updateRecruitmentPositionSuccess2() {

		// given
		final User owner = userRepository.save(
				User.builder()
						.nickname("owner")
						.email("a@aa.aa")
						.social(Social.KAKAO)
						.build());

		final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

		final Study study = studyRepository.save(
				StudyFixture.createStudy(
						"study",
						category,
						owner,
						4,
						Platform.GATHER
				)
		);

		final Position position1 = positionRepository.save(PositionFixture.createPosition("position1"));
		final Position position2 = positionRepository.save(PositionFixture.createPosition("position2"));
		final Position position3 = positionRepository.save(PositionFixture.createPosition("position3"));

		final WriteRecruitmentRequest request = WriteRecruitmentRequest.builder()
				.title("recruitment")
				.content("I want to study")
				.stackIds(Set.of())
				.positionIds(Set.of(position1.getId(), position2.getId(), position3.getId()))
				.applicantCount(4)
				.contact(Contact.KAKAO)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

		final Recruitment recruitment = recruitmentService.write(owner, request, study.getId());

		// when
		recruitmentPositionService.update(recruitment, Set.of());
		final List<RecruitmentPosition> recruitmentPositions = recruitment.getRecruitmentPositions();

		// then
		assertThat(recruitmentPositions).hasSize(0);

	}

	@DisplayName("[Success] update 시, 모집 공고에 등록된 포지션을 전달하면 아무런 변화도 일어나지 않는다.")
	@Test
	void updateRecruitmentPositionSuccess3() {

		// given
		final User owner = userRepository.save(
				User.builder()
						.nickname("owner")
						.email("a@aa.aa")
						.social(Social.KAKAO)
						.build());

		final Category category = categoryRepository.save(
				CategoryFixture.createCategory("category1"));

		final Study study = studyRepository.save(
				StudyFixture.createStudy(
						"study",
						category,
						owner,
						4,
						Platform.GATHER
				)
		);

		final Position position1 = positionRepository.save(PositionFixture.createPosition("position1"));

		final WriteRecruitmentRequest request = WriteRecruitmentRequest.builder()
				.title("recruitment")
				.content("I want to study")
				.stackIds(Set.of())
				.positionIds(Set.of(position1.getId()))
				.applicantCount(4)
				.contact(Contact.KAKAO)
				.callUrl("x.com")
				.recruitmentEndDateTime(LocalDateTime.now().plusMonths(3))
				.build();

		final Recruitment recruitment = recruitmentService.write(owner, request, study.getId());

		// when
		recruitmentPositionService.update(recruitment, Set.of(position1.getId()));
		final List<RecruitmentPosition> recruitmentPositions = recruitment.getRecruitmentPositions();

		// then
		assertThat(recruitmentPositions).hasSize(1);
		assertThat(recruitmentPositions).extracting("position")
				.containsAll(List.of(position1));

	}
}
