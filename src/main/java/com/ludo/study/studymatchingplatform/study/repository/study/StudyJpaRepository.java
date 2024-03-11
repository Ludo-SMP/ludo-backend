package com.ludo.study.studymatchingplatform.study.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;

public interface StudyJpaRepository extends JpaRepository<Study, Long> {

}
