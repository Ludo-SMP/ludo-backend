package com.ludo.study.studymatchingplatform.user.service.dto.response;

import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.study.Participant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;

import lombok.Builder;

@Builder
public record MyPageResponse(

		UserResponse user,
		List<ParticipateStudyResponse> participateStudies,
		List<ApplicantRecruitmentResponse> applicantRecruitments,
		List<CompletedStudyResponse> completedStudies

) {

	public static MyPageResponse from(final List<Participant> participants,
									  final List<Applicant> applicants,
									  final List<Participant> completedStudies) {
		final UserResponse user = UserResponse.from(participants.get(0).getUser());

		final List<ParticipateStudyResponse> studies = participants.stream()
				.map(participant ->
						ParticipateStudyResponse.from(participant.getStudy(), participant.getPosition()))
				.toList();

		final List<ApplicantRecruitmentResponse> recruitments = applicants.stream()
				.map(ApplicantRecruitmentResponse::from)
				.toList();

		final List<CompletedStudyResponse> completed = completedStudies.stream()
				.map(completedStudy ->
						CompletedStudyResponse.from(completedStudy.getStudy(), completedStudy.getPosition()))
				.toList();

		return MyPageResponse.builder()
				.user(user)
				.participateStudies(studies)
				.applicantRecruitments(recruitments)
				.completedStudies(completed)
				.build();
	}

}
