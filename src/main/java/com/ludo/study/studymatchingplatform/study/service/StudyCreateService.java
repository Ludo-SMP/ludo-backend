package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Participant;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.Way;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.builder.StudyBuilder;
import com.ludo.study.studymatchingplatform.study.service.dto.StudyCreateDto;
import com.ludo.study.studymatchingplatform.study.service.dto.request.StudyCreateRequest;
import com.ludo.study.studymatchingplatform.study.service.exception.AuthenticationException;
import com.ludo.study.studymatchingplatform.user.domain.User;
import com.ludo.study.studymatchingplatform.user.repository.UserRepositoryImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyCreateService {

	private final UserRepositoryImpl userRepository;
	private final StudyRepositoryImpl studyRepository;
	private final CategoryRepositoryImpl categoryRepository;
	private final ParticipantRepositoryImpl participantRepository;

	public void create(final StudyCreateRequest request) {
		final User owner = findUserById(Long.valueOf(request.userId()));
		final Category category = findCategoryById(Long.valueOf(request.categoryId()));
		final StudyCreateDto studyCreateDto = StudyBuilder.convertToStudyCreateDto(request);
		final Study study = createStudy(owner, studyCreateDto, category);
		final Participant participant = createParticipant(study, owner);
		study.addParticipant(participant);
		studyRepository.save(study);
		participantRepository.save(participant);
	}

	private User findUserById(final Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new AuthenticationException("존재하지 않는 회원입니다."));
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() ->
						new EntityNotFoundException("존재하지 않는 카테고리입니다. categoryId = " + categoryId));
	}

	private Study createStudy(final User owner, final StudyCreateDto studyCreateDto, final Category category) {

		return new Study(category, owner, studyCreateDto.title(), Way.valueOf(studyCreateDto.way().name()),
				studyCreateDto.participantLimit(), studyCreateDto.startDateTime(), studyCreateDto.endDateTime());
	}

	private Participant createParticipant(final Study study, final User owner) {
		return new Participant(study, owner);
	}

}
