package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.study.repository.CategoryJpaRepository;
import com.ludo.study.studymatchingplatform.study.repository.StudyJpaRepository;
import com.ludo.study.studymatchingplatform.study.service.builder.StudyBuilder;
import com.ludo.study.studymatchingplatform.study.service.dto.StudyCreateDto;
import com.ludo.study.studymatchingplatform.study.service.dto.request.StudyCreateRequest;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserJpaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyCreateService {

	private final UserJpaRepository userJpaRepository;
	private final StudyJpaRepository studyJpaRepository;
	private final CategoryJpaRepository categoryJpaRepository;

	public Long create(final StudyCreateRequest request) {
		final User owner = findUserById(request.userId());
		final Category category = findCategoryById(request.categoryId());
		final StudyCreateDto studyCreateDto = StudyBuilder.convertToStudyCreateDto(request);
		final Study study = createStudy(owner, studyCreateDto, category);
		final Study savedStudy = studyJpaRepository.save(study);
		return savedStudy.getId();
	}

	private User findUserById(final Long id) {
		return userJpaRepository.findById(id)
				.orElseThrow(() -> new AuthenticationException("존재하지 않는 회원입니다."));
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryJpaRepository.findById(categoryId)
				.orElseThrow(() ->
						new EntityNotFoundException("존재하지 않는 카테고리입니다. categoryId = " + categoryId));
	}

	private Study createStudy(final User owner, final StudyCreateDto studyCreateDto, final Category category) {
		return new Study(category, owner, studyCreateDto.title(), Way.valueOf(studyCreateDto.way().name()),
				studyCreateDto.participantLimit(), studyCreateDto.participantCount(),
				studyCreateDto.startDateTime(), studyCreateDto.endDateTime());
	}

}
