package com.ludo.study.studymatchingplatform.study.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.application.dto.RecruitmentDetailsResponse;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.fixture.CategoryFixture;
import com.ludo.study.studymatchingplatform.study.fixture.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.RecruitmentStackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StackFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.UserFixture;
import com.ludo.study.studymatchingplatform.study.persistence.RecruitmentRepository;
import com.ludo.study.studymatchingplatform.user.domain.Platform;

@SpringBootTest
class RecruitmentFindServiceTest {

	@Autowired
	RecruitmentFindService recruitmentFindService;

	@Autowired
	RecruitmentRepository recruitmentRepository;

	@Test
	@Transactional
	void 모집공고_상세를_조회한다() {
		Study study = createStudy("스터디 A", Way.ONLINE, Platform.NAVER, "아카", "archa@naver.com", "프로젝트");
		RecruitmentStack spring = RecruitmentStackFixture.createRecruitmentStack(
			StackFixture.createStack("spring"));
		RecruitmentStack react = RecruitmentStackFixture.createRecruitmentStack(
			StackFixture.createStack("react"));
		Recruitment recruitment = createRecruitment("지원공고 입니다.", study, spring, react);

		Recruitment save = recruitmentRepository.save(recruitment);
		RecruitmentDetailsResponse recruitmentDetailsResponse = recruitmentFindService.findRecruitmentById(
			save.getId());

		assertThat(recruitmentDetailsResponse.getTitle()).isEqualTo("지원공고 입니다.");
		assertThat(recruitmentDetailsResponse.getOwnerNickname()).isEqualTo("아카");
		assertThat(recruitmentDetailsResponse.getWay()).isEqualTo(Way.ONLINE.toString());
		assertThat(recruitmentDetailsResponse.getCategory()).isEqualTo("프로젝트");
	}

	private Study createStudy(String title, Way way, Platform platform,
		String nickname, String email, String category
	) {
		return StudyFixture.createStudy(title, way,
			UserFixture.createUser(platform, nickname, email),
			CategoryFixture.createCategory(category));
	}

	private Recruitment createRecruitment(String title, Study study, RecruitmentStack... recruitmentStacks) {
		return RecruitmentFixture.createRecruitment(title, study, recruitmentStacks);
	}

	@Test
	void 모집공고_상세_더미데이터를_조회한다() {
		RecruitmentDetailsResponse recruitmentDetailsResponse = recruitmentFindService.findRecruitmentById(1L);
		assertStudyInfo(recruitmentDetailsResponse);
		assertRecruitmentInfo(recruitmentDetailsResponse);
	}

	void assertStudyInfo(RecruitmentDetailsResponse recruitmentDetailsResponse) {
		assertThat(recruitmentDetailsResponse.getOwnerNickname()).isEqualTo("아카");
		assertThat(recruitmentDetailsResponse.getCategory()).isEqualTo("프로젝트");
		assertThat(recruitmentDetailsResponse.getWay()).isEqualTo(Way.ONLINE.toString());
		assertThat(recruitmentDetailsResponse.getStartDateTime()).isEqualTo("2024-03-01T18:00");
		assertThat(recruitmentDetailsResponse.getEndDateTime()).isEqualTo("2024-03-20T18:00");
	}

	void assertRecruitmentInfo(RecruitmentDetailsResponse recruitmentDetailsResponse) {
		assertThat(recruitmentDetailsResponse.getTitle()).isEqualTo("Recruitment Title 1");
		assertThat(recruitmentDetailsResponse.getContent()).isEqualTo("Content 1");
		assertThat(recruitmentDetailsResponse.getStacks()).contains("spring", "react");
		assertThat(recruitmentDetailsResponse.getPositions()).contains("백엔드", "프론트엔드");
		assertThat(recruitmentDetailsResponse.getPlatformUrl()).isEqualTo("https://example.com/call1");
		assertThat(recruitmentDetailsResponse.getApplicantCount()).isSameAs(5);
		assertThat(recruitmentDetailsResponse.getRecruitmentEndDateTime()).isEqualTo("2024-02-10T18:00");
		assertThat(recruitmentDetailsResponse.getCreatedDateTime()).isEqualTo("2024-01-20T12:30");
	}

}
