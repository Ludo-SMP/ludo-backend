package com.ludo.study.studymatchingplatform.study.service.dto.response.study.participant;

import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.position.PositionResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ParticipantUserWithIsReviewed(
        Long id,
        String nickname,
        String email,
        Role role,
        PositionResponse position,
        Integer totalAttendance,
        LocalDate recentAttendanceDate,
        Boolean isReviewedParticipant
) {

    public static ParticipantUserWithIsReviewed from
            (final Participant participant, final Boolean isReviewedParticipant) {
        final User user = participant.getUser();
        final PositionResponse response = PositionResponse.from(
                participant.getPosition());

        return ParticipantUserWithIsReviewed.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(participant.getRole())
                .position(response)
                .totalAttendance(participant.getAttendance())
                .recentAttendanceDate(participant.getRecentAttendanceDate())
                .isReviewedParticipant(isReviewedParticipant)
                .build();

    }

}