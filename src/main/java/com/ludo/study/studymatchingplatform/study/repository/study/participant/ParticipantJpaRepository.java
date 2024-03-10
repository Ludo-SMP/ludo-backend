package com.ludo.study.studymatchingplatform.study.repository.study.participant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.study.Participant;
import com.ludo.study.studymatchingplatform.study.domain.id.ParticipantId;

public interface ParticipantJpaRepository extends JpaRepository<Participant, ParticipantId> {

}
