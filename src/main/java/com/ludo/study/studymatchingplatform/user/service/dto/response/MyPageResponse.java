package com.ludo.study.studymatchingplatform.user.service.dto.response;

import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.Builder;

@Builder
public record MyPageResponse(

		UserResponse user,
		List<ParticipateStudyResponse> participateStudies,
		List<ApplicantRecruitmentResponse> applicantRecruitments,
		List<CompletedStudyResponse> completedStudies

) {

	public static MyPageResponse from(final User user,
									  final List<Participant> participants,
									  final List<Applicant> applicants,
									  final List<Participant> completedStudies) {
		final UserResponse userResponse = UserResponse.from(user);
		return MyPageResponse.builder()
				.user(userResponse)
				.participateStudies(makeParticipateStudies(user, participants))
				.applicantRecruitments(makeApplicantRecruitments(applicants))
				.completedStudies(makeCompletedStudies(user, completedStudies))
				.build();
	}

	private static List<ParticipateStudyResponse> makeParticipateStudies(final User user,
																		 final List<Participant> participants) {
		return participants.stream()
				.map(participant ->
						ParticipateStudyResponse.from(user, participant.getStudy(), participant.getPosition()))
				.toList();
	}

	private static List<ApplicantRecruitmentResponse> makeApplicantRecruitments(final List<Applicant> applicants) {
		return applicants.stream()
				.map(ApplicantRecruitmentResponse::from)
				.toList();
	}

	private static List<CompletedStudyResponse> makeCompletedStudies(final User user,
																	 final List<Participant> completedStudies) {
		return completedStudies.stream()
				.map(completedStudy ->
						CompletedStudyResponse.from(user, completedStudy.getStudy(), completedStudy.getPosition()))
				.toList();
	}

}
