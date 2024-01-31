package com.ludo.study.studymatchingplatform.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.Study;

public interface StudyJpaRepository extends JpaRepository<Study, Long>, StudyRepository {

}
