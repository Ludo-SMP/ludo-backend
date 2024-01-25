package com.ludo.study.studymatchingplatform.study.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.RecruitmentDetailsResponse;
import com.ludo.study.studymatchingplatform.user.domain.Platform;

@SpringBootTest
class RecruitmentDetailsFindServiceTest {

	@Autowired
	RecruitmentDetailsFindService recruitmentDetailsFindService;

	@Autowired
	RecruitmentRepositoryImpl recruitmentRepositoryImpl;

	@Test
	@Transactional
	void 모집공고_상세를_조회한다() {
		Study study = createStudy("스터디 A", Way.ONLINE, Platform.NAVER, "아카", "archa@naver.com", "프로젝트");
		RecruitmentStack spring = RecruitmentStackFixture.createRecruitmentStack(
			StackFixture.createStack("spring"));
		RecruitmentStack react = RecruitmentStackFixture.createRecruitmentStack(
			StackFixture.createStack("react"));
		Recruitment recruitment = createRecruitment("지원공고 입니다.", study, spring, react);

		Recruitment save = recruitmentRepositoryImpl.save(recruitment);
		RecruitmentDetailsResponse recruitmentDetailsResponse = recruitmentDetailsFindService.findRecruitmentDetails(
			save.getId());

		assertThat(recruitmentDetailsResponse.title()).isEqualTo("지원공고 입니다.");
		assertThat(recruitmentDetailsResponse.ownerNickname()).isEqualTo("아카");
		assertThat(recruitmentDetailsResponse.way()).isEqualTo(Way.ONLINE.toString());
		assertThat(recruitmentDetailsResponse.category()).isEqualTo("프로젝트");
	}

	@Test
	@Transactional
	void 모집공고_상세를_조회하면_조회수_증가() {
		Study study = createStudy("스터디 A", Way.ONLINE, Platform.NAVER, "아카", "archa@naver.com", "프로젝트");
		RecruitmentStack spring = RecruitmentStackFixture.createRecruitmentStack(
			StackFixture.createStack("spring"));
		RecruitmentStack react = RecruitmentStackFixture.createRecruitmentStack(
			StackFixture.createStack("react"));
		Recruitment recruitment = createRecruitment("지원공고 입니다.", study, spring, react);

		Recruitment save = recruitmentRepositoryImpl.save(recruitment);
		recruitmentDetailsFindService.findRecruitmentDetails(save.getId());
		Recruitment find = recruitmentRepositoryImpl.findById(save.getId());
		assertThat(find.getHits()).isEqualTo(1);
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
	void 모집공고_상세_테스트데이터를_조회한다() {
		RecruitmentDetailsResponse recruitmentDetailsResponse = recruitmentDetailsFindService.findRecruitmentDetails(
			1L);
		assertStudyInfo(recruitmentDetailsResponse);
		assertRecruitmentInfo(recruitmentDetailsResponse);
	}

	void assertStudyInfo(RecruitmentDetailsResponse recruitmentDetailsResponse) {
		assertThat(recruitmentDetailsResponse.ownerNickname()).isEqualTo("아카");
		assertThat(recruitmentDetailsResponse.category()).isEqualTo("프로젝트");
		assertThat(recruitmentDetailsResponse.way()).isEqualTo(Way.ONLINE.toString());
		assertThat(recruitmentDetailsResponse.startDateTime()).isEqualTo("2024-03-01T18:00");
		assertThat(recruitmentDetailsResponse.endDateTime()).isEqualTo("2024-03-20T18:00");
	}

	void assertRecruitmentInfo(RecruitmentDetailsResponse recruitmentDetailsResponse) {
		assertThat(recruitmentDetailsResponse.title()).isEqualTo("Recruitment Title 1");
		assertThat(recruitmentDetailsResponse.content()).isEqualTo("Content 1");
		assertThat(recruitmentDetailsResponse.stacks()).contains("spring", "react");
		assertThat(recruitmentDetailsResponse.positions()).contains("백엔드", "프론트엔드");
		assertThat(recruitmentDetailsResponse.platformUrl()).isEqualTo("https://example.com/call1");
		assertThat(recruitmentDetailsResponse.applicantCount()).isSameAs(5);
		assertThat(recruitmentDetailsResponse.recruitmentEndDateTime()).isEqualTo("2024-02-10T18:00");
		assertThat(recruitmentDetailsResponse.createdDateTime()).isEqualTo("2024-01-20T12:30");
	}

}
