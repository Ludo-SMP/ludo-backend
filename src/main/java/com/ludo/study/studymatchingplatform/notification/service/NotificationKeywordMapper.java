package com.ludo.study.studymatchingplatform.notification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordCategory;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordPosition;
import com.ludo.study.studymatchingplatform.notification.domain.keyword.NotificationKeywordStack;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.stack.Stack;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationKeywordMapper {

	private final CategoryRepositoryImpl categoryRepository;
	private final StackRepositoryImpl stackRepository;
	private final PositionRepositoryImpl positionRepository;

	public List<NotificationKeywordCategory> toKeywordCategories(final User user, final List<Long> categoryIds) {

		final List<NotificationKeywordCategory> notificationKeywordCategories = new ArrayList<>();

		categoryIds.forEach(categoryId ->
		{
			final Category category = categoryRepository.findById(categoryId)
					.orElseThrow(() -> new NotFoundException(String.format("id=%d 에 해당하는 카테고리가 없습니다.", categoryId)));

			notificationKeywordCategories.add(NotificationKeywordCategory.of(user, category));
		});

		return notificationKeywordCategories;
	}

	public List<NotificationKeywordStack> toKeywordStacks(final User user, final List<Long> stackIds) {

		final List<NotificationKeywordStack> notificationKeywordStacks = new ArrayList<>();

		stackIds.forEach(stackId ->
		{
			final Stack stack = stackRepository.findById(stackId)
					.orElseThrow(() -> new NotFoundException(String.format("id=%d 에 해당하는 기술스택이 없습니다.", stackId)));

			notificationKeywordStacks.add(NotificationKeywordStack.of(user, stack));
		});

		return notificationKeywordStacks;
	}

	public List<NotificationKeywordPosition> toKeywordPositions(final User user, final List<Long> positionIds) {

		final List<NotificationKeywordPosition> notificationKeywordPositions = new ArrayList<>();

		positionIds.forEach(positionId ->
		{
			final Position position = positionRepository.findById(positionId)
					.orElseThrow(() -> new NotFoundException(String.format("id=%d 에 해당하는 포지션이 없습니다.", positionId)));

			notificationKeywordPositions.add(NotificationKeywordPosition.of(user, position));
		});
		return notificationKeywordPositions;
	}
}
