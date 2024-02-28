package com.ludo.study.studymatchingplatform.study.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.fixture.RecruitmentFixture;
import com.ludo.study.studymatchingplatform.study.fixture.StudyFixture;
import com.ludo.study.studymatchingplatform.study.fixture.UserFixture;
import com.ludo.study.studymatchingplatform.user.domain.Social;
import com.ludo.study.studymatchingplatform.user.domain.User;

@SpringBootTest
class StudyTest {

	@Nested
	@DisplayName("유저 스토리: 스터디장은 지원자를 수락할 수 있어야 한다")
	class AcceptApplicantTest {
		@Test
		@DisplayName("[Exception] 스터디 장이 아닌 경우 예외 발생")
		void notStudyOwner() {
			// given
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com");
			User notOwner = UserFixture.createUser(Social.NAVER, "anonymous", "anonymous@naver.com");
			User applicant = UserFixture.createUser(Social.NAVER, "applicant", "applicant@naver.com");

			Study study = StudyFixture.createStudy(owner, "스터디 A", 5, StudyStatus.RECRUITING);
			Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "모집공고", "내용", 0, null, null);

			// when then
			assertThatThrownBy(() -> study.acceptApplicant(notOwner, applicant, recruitment.getId()))
					.hasMessageContaining("스터디 장이 아닙니다.");
		}

		/**
		 * test scenario:
		 * 스터디 지원자 수락 API -> /studies/{study-id}/recruitments/{recruitment-id}
		 * 중 recruitment-id를 임의로 바꿔서 요청을 보내는 경우
		 */
		@Test
		@DisplayName("[Exception] 다른 스터디 모집공고로 지원한 경우 예외 발생")
		void otherRecruitment() {
			// given
			long otherRecruitmentId = 100;
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com");
			User applicantUser = UserFixture.createUser(Social.NAVER, "applicant", "applicant@naver.com");

			Study ownerStudy = StudyFixture.createStudy(owner, "스터디 A", 5, StudyStatus.RECRUITING);

			Recruitment ownerStudyRecruitment = RecruitmentFixture.createRecruitment(ownerStudy, "모집공고", "내용", 0, null,
					null);
			ownerStudy.registerRecruitment(ownerStudyRecruitment);

			// when, then
			assertThatThrownBy(() -> ownerStudy.acceptApplicant(owner, applicantUser, otherRecruitmentId))
					.hasMessage("해당 스터디의 모집공고가 아닙니다.");
		}

		@Test
		@DisplayName("[Exception] 스터디에 남은 자리가 없는 경우 예외 발생")
		void notEnoughParticipantRemainCount() {
			// given
			int participantCount = 5;
			int participantLimit = 5;
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com");
			User applicantUser = UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com");
			Study study = StudyFixture.createStudy(owner, "스터디 A", participantLimit,
					StudyStatus.RECRUITING);
			while (participantCount-- > 0) {
				study.addParticipant(null);
			}

			// when then
			assertThatThrownBy(() -> study.acceptApplicant(owner, applicantUser, null))
					.hasMessage("남아있는 자리가 없습니다.");
		}

		@Test
		@DisplayName("[Exception] 스터디 상태가 모집중이 아닌 경우 예외 발생")
		void notRecruitingStatus() {
			// given
			StudyStatus studyStatus = StudyStatus.RECRUITED;
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com");
			User applicantUser = UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com");
			Study study = StudyFixture.createStudy(owner, "스터디 A", 5, studyStatus);

			// when then
			assertThatThrownBy(() -> study.acceptApplicant(owner, applicantUser, null))
					.hasMessage("현재 모집 중인 스터디가 아닙니다.");
		}

		@Test
		@DisplayName("[Exception] 지원하지 않은 지원자의 경우 예외 발생")
		void wrongApplicant() {
			// given
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
			Study study = StudyFixture.createStudy(owner, "스터디 A", 5, StudyStatus.RECRUITING);
			Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "모집공고", "내용", 5, null, null);
			study.registerRecruitment(recruitment);
			User notApplicantUser = UserFixture.createUser(Social.NAVER, "other", "other@gmail.com");

			// when then
			assertThatThrownBy(() -> study.acceptApplicant(owner, notApplicantUser, recruitment.getId()))
					.hasMessage("지원자 목록에 존재하지 않는 사용자입니다.");
		}

		@Test
		@DisplayName("[Exception] 스터디 장과 지원자가 같은 경우 예외 발생")
		void equalsOwnerAndApplicantUser() {
			// given
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
			Study study = StudyFixture.createStudy(owner, "스터디 A", 5, StudyStatus.RECRUITING);
			Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "모집공고", "내용", 5, null, null);

			// when then
			assertThatThrownBy(() -> study.acceptApplicant(owner, owner, recruitment.getId()))
					.hasMessage("스터디 장과 지원자가 같습니다.");
		}

		@Test
		@DisplayName("[Success] 예외 검증 조건을 통과하면 지원자 수락")
		void success() {
			// given
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
			Study study = StudyFixture.createStudy(owner, "스터디 A", 5, StudyStatus.RECRUITING);
			Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "모집공고", "내용", 5, null, null);

			study.registerRecruitment(recruitment);

			// when
			User applicantUser = UserFixture.createUser(Social.NAVER, "other", "other@gmail.com");
			Applicant applicant = Applicant.of(recruitment, applicantUser);
			recruitment.addApplicant(applicant);
			assertThat(study.getParticipantCount()).isZero();
			assertThat(recruitment.getApplicants()).isNotEmpty();
			assertThat(recruitment.getApplicants().get(0).getApplicantStatus()).isEqualTo(ApplicantStatus.UNCHECKED);

			// then
			assertThatCode(() -> study.acceptApplicant(owner, applicantUser, recruitment.getId()))
					.doesNotThrowAnyException();
			assertThat(study.getParticipantCount()).isEqualTo(1);
			assertThat(recruitment.getApplicants().get(0).getApplicantStatus()).isEqualTo(ApplicantStatus.ACCEPTED);

			assertThat(study.getParticipants().get(0).getUser()).isEqualTo(applicantUser);
		}

		@Test
		@DisplayName("[Success] 지원자를 수락하여 전체 인원 수에 도달하면 모집완료 상태로 바뀐다.")
		void recruitedWhenParticipantCountEqualsLimit() {
			// given
			int curParticipantCount = 4;
			int participantLimit = 5;
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
			Study study = StudyFixture.createStudy(owner, "스터디 A", participantLimit,
					StudyStatus.RECRUITING);
			Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "모집공고", "내용", 5, null, null);
			study.registerRecruitment(recruitment);

			// 현재 스터디 참가자 4명이 존재할 때
			while (curParticipantCount-- > 0) {
				User user = UserFixture.createUser(Social.NAVER, "닉네임", "email@google.com");
				Applicant applicant = Applicant.of(recruitment, user);
				recruitment.addApplicant(applicant);
				study.acceptApplicant(owner, user, recruitment.getId());
			}

			User applicantUser = UserFixture.createUser(Social.NAVER, "other", "other@gmail.com");
			Applicant applicant = Applicant.of(recruitment, applicantUser);
			recruitment.addApplicant(applicant);

			// when
			study.acceptApplicant(owner, applicantUser, recruitment.getId());

			// then
			assertThatThrownBy(study::ensureRecruiting).hasMessage("현재 모집 중인 스터디가 아닙니다.");
		}
	}

	@Nested
	@DisplayName("유저스토리: 스터디장은 지원자를 거절할 수 있어야 한다")
	class RejectApplicantTest {
		@Test
		@DisplayName("[Exception] 스터디 장이 아닌 경우 예외 발생")
		void notStudyOwner() {
			// given
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com");
			User notOwner = UserFixture.createUser(Social.NAVER, "anonymous", "anonymous@naver.com");
			User applicant = UserFixture.createUser(Social.NAVER, "applicant", "applicant@naver.com");

			Study study = StudyFixture.createStudy(owner, "스터디 A", 5, StudyStatus.RECRUITING);
			Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "모집공고", "내용", 0, null, null);

			// when then
			assertThatThrownBy(() -> study.rejectApplicant(notOwner, applicant, recruitment.getId()))
					.hasMessageContaining("스터디 장이 아닙니다.");
		}

		@Test
		@DisplayName("[Exception] 다른 스터디 모집공고로 지원한 경우 예외 발생")
		void otherRecruitment() {
			// given
			long otherRecruitmentId = 100;
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@naver.com");
			User applicantUser = UserFixture.createUser(Social.NAVER, "applicant", "applicant@naver.com");

			Study ownerStudy = StudyFixture.createStudy(owner, "스터디 A", 5, StudyStatus.RECRUITING);

			Recruitment ownerStudyRecruitment = RecruitmentFixture.createRecruitment(ownerStudy, "모집공고", "내용", 0, null,
					null);
			ownerStudy.registerRecruitment(ownerStudyRecruitment);

			// when, then
			assertThatThrownBy(() -> ownerStudy.rejectApplicant(owner, applicantUser, otherRecruitmentId))
					.hasMessage("해당 스터디의 모집공고가 아닙니다.");
		}

		@Test
		@DisplayName("[Exception] 지원하지 않은 지원자의 경우 예외 발생")
		void wrongApplicant() {
			// given
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
			Study study = StudyFixture.createStudy(owner, "스터디 A", 5, StudyStatus.RECRUITING);
			Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "모집공고", "내용", 5, null, null);
			study.registerRecruitment(recruitment);
			User notApplicantUser = UserFixture.createUser(Social.NAVER, "other", "other@gmail.com");

			// when then
			assertThatThrownBy(() -> study.acceptApplicant(owner, notApplicantUser, recruitment.getId()))
					.hasMessage("지원자 목록에 존재하지 않는 사용자입니다.");
		}

		@Test
		@DisplayName("[Exception] 스터디 장과 지원자가 같은 경우 예외 발생")
		void equalsOwnerAndApplicantUser() {
			// given
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
			Study study = StudyFixture.createStudy(owner, "스터디 A", 5, StudyStatus.RECRUITING);
			Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "모집공고", "내용", 5, null, null);

			// when then
			assertThatThrownBy(() -> study.rejectApplicant(owner, owner, recruitment.getId()))
					.hasMessage("스터디 장과 지원자가 같습니다.");
		}

		@Test
		@DisplayName("[Success] 예외 검증 조건을 통과하면 지원자를 거절한다")
		void success() {
			// given
			User owner = UserFixture.createUser(Social.NAVER, "archa", "archa@gmail.com");
			Study study = StudyFixture.createStudy(owner, "스터디 A", 5, StudyStatus.RECRUITING);
			Recruitment recruitment = RecruitmentFixture.createRecruitment(study, "모집공고", "내용", 5, null, null);

			study.registerRecruitment(recruitment);

			// when
			User applicantUser = UserFixture.createUser(Social.NAVER, "other", "other@gmail.com");
			Applicant applicant = Applicant.of(recruitment, applicantUser);
			recruitment.addApplicant(applicant);
			assertThat(recruitment.getApplicants()).isNotEmpty();
			assertThat(recruitment.getApplicants().get(0).getApplicantStatus()).isEqualTo(ApplicantStatus.UNCHECKED);

			// then
			assertThatCode(() -> study.rejectApplicant(owner, applicantUser, recruitment.getId()))
					.doesNotThrowAnyException();
			assertThat(study.getParticipants()).isEmpty();
			assertThat(recruitment.getApplicants()).isNotEmpty();
			assertThat(recruitment.getApplicants().get(0).getApplicantStatus()).isEqualTo(ApplicantStatus.REJECTED);
		}
	}

}