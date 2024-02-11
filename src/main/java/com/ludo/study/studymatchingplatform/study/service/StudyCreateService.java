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
// 캐시 제거 적용 필요
public class StudyCreateService {

	private final UserRepositoryImpl userRepository;
	private final StudyRepositoryImpl studyRepository;
	private final CategoryRepositoryImpl categoryRepository;
	private final ParticipantRepositoryImpl participantRepository;

	public Long create(final StudyCreateRequest request, final String email) {
		final User owner = findUserByEmail(email);
		final Category category = findCategoryById(Long.valueOf(request.categoryId()));
		final StudyCreateDto studyCreateDto = StudyBuilder.convertToStudyCreateDto(request);
		final Study study = createStudy(owner, studyCreateDto, category);
		final Participant participant = createParticipant(study, owner);

		// 스터디 생성자도 참여자로 포함
		study.addParticipant(participant);
		final Study savedStudy = studyRepository.save(study);
		participantRepository.save(participant);

		return savedStudy.getId();
	}

	private User findUserByEmail(final String email) {
		return userRepository.findByEmail(email)
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
