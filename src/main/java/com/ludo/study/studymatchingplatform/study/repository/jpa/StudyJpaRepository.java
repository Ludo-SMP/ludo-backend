package com.ludo.study.studymatchingplatform.study.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepository;

public interface StudyJpaRepository extends StudyRepository, JpaRepository<Study, Long> {
	
}
