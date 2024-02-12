package com.ludo.study.studymatchingplatform.study.repository;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.Participant;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ParticipantRepositoryImpl {

	private final ParticipantJpaRepository participantJpaRepository;

	public void save(final Participant participant) {
		participantJpaRepository.save(participant);
	}

}
