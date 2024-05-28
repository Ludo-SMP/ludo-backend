package com.ludo.study.studymatchingplatform.study.repository.study;

import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyStatisticsJpaRepository extends JpaRepository<StudyStatistics, Long> {
}
