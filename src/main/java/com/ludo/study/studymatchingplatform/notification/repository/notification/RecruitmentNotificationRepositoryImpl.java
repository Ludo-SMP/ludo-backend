package com.ludo.study.studymatchingplatform.notification.repository.notification;

import static com.ludo.study.studymatchingplatform.notification.domain.notification.QRecruitmentNotification.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ludo.study.studymatchingplatform.notification.domain.notification.RecruitmentNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentNotificationRepositoryImpl {

	private final JPAQueryFactory q;
	private final RecruitmentNotificationJpaRepository recruitmentNotificationJpaRepository;

	public List<RecruitmentNotification> saveAll(final List<RecruitmentNotification> recruitmentNotification) {
		return recruitmentNotificationJpaRepository.saveAll(recruitmentNotification);
	}

	public List<RecruitmentNotification> findAllByUserId(final Long userId) {
		return q.selectFrom(recruitmentNotification)
				.where(recruitmentNotification.notifier.id.eq(userId))
				.fetch();
	}

}
