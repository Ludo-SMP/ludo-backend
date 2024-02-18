package com.ludo.study.studymatchingplatform.study.repository;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.Participant;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ParticipantRepositoryImpl {

	private final ParticipantJpaRepository participantJpaRepository;

	public Participant save(final Participant participant) {
		return participantJpaRepository.save(participant);
	}

}
