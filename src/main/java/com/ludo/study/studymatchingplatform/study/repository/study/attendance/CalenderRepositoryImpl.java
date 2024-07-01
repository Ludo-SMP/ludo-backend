package com.ludo.study.studymatchingplatform.study.repository.study.attendance;

import static com.ludo.study.studymatchingplatform.study.domain.study.attendance.QCalender.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.study.domain.study.attendance.Calender;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CalenderRepositoryImpl {

	private final JPAQueryFactory q;

	private final CalenderJpaRepository calenderJpaRepository;

	public Calender save(final Calender calender) {
		return calenderJpaRepository.save(calender);
	}

	public Optional<List<Calender>> findByStudyId(final Long studyId) {
		return Optional.ofNullable(q.selectFrom(calender)
				.where(calender.study.id.eq(studyId))
				.fetch());
	}

	public void deleteExistingCalendars(final List<Calender> calenders) {
		calenderJpaRepository.deleteAll(calenders);
	}

}
