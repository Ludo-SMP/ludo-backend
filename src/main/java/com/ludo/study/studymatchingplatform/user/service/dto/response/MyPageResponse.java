package com.ludo.study.studymatchingplatform.user.service.dto.response;

import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;

import lombok.Builder;

@Builder
public record MyPageResponse(

		UserResponse userResponse,
		List<ParticipateStudyResponse> participateStudiesResponse,
		List<ApplicantRecruitmentResponse> applicantRecruitmentsResponse,
		List<CompletedStudyResponse> completedStudiesResponse

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
				.map(applicant ->
						ApplicantRecruitmentResponse.from(applicant))
				.toList();

		final List<CompletedStudyResponse> completed = completedStudies.stream()
				.map(completedStudy ->
						CompletedStudyResponse.from(completedStudy.getStudy(), completedStudy.getPosition()))
				.toList();

		return MyPageResponse.builder()
				.userResponse(user)
				.participateStudiesResponse(studies)
				.applicantRecruitmentsResponse(recruitments)
				.completedStudiesResponse(completed)
				.build();
	}

}
