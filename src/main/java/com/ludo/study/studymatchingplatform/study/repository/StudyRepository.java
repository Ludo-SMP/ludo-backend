package com.ludo.study.studymatchingplatform.study.repository;

import com.ludo.study.studymatchingplatform.study.domain.Study;

public interface StudyRepository {

	// Optional<Study> findById(long id);
	Study save(final Study study);

}