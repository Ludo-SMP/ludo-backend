package com.ludo.study.studymatchingplatform.study.domain.study.participant;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.study.domain.id.ParticipantId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "study_user_lnk")
public class Participant extends BaseEntity {

    @EmbeddedId
    @Builder.Default
    private ParticipantId id = new ParticipantId();

    @ManyToOne(fetch = LAZY)
    @MapsId("studyId")
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @ManyToOne(fetch = LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "char(10)")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    // 누적 출석
    @Builder.Default
    private int attendance = 0;

    // 누적 유효 출석
    @Builder.Default
    private int validAttendance = 0;

    // 스터디 합류일
    @Builder.Default
    private LocalDateTime enrollmentDateTime = LocalDateTime.now();

    public static Participant from(final Study study, final User user, final Position position, final Role role) {
        final Participant participant = new Participant();
        participant.study = study;
        participant.user = user;
        participant.role = role;
        participant.position = position;
        return participant;
    }

    public Role getRole() {
        // TODO: Role spec not determined clearly
        // TODO: First of all, I reflected it in my own way
        if (study.isOwner(this)) {
            return Role.OWNER;
        }
        return Role.MEMBER;
    }

    public boolean matchesUser(final User user) {
        return matchesUser(user.getId());
    }

    public boolean matchesUser(final Long userId) {
        final boolean isMatchesUser = Objects.equals(this.user.getId(), userId);
        return isMatchesUser && !isDeleted();
    }

    public void leave(final Study study) {
        study.removeParticipant(this);
        this.study = null;
        this.softDelete();
    }

    public void updatePosition(final Position position) {
        this.position = position;
    }

}
