package com.ludo.study.studymatchingplatform.study.service;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.Category;
import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.repository.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.user.domain.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// 캐시 제거 적용 필요
public class StudyCreateService {

	private final StudyRepositoryImpl studyRepository;
	private final CategoryRepositoryImpl categoryRepository;
	private final PositionRepositoryImpl positionRepository;
	private final ParticipantService participantService;

	@Transactional
	public Study create(final WriteStudyRequest request, final User user) {
		final Category category = findCategoryById(request.categoryId());
		final Study study = request.toStudy(user, category, request.platform());
		final Position ownerPosition = positionRepository.findById(request.positionId());
		studyRepository.save(study);
		participantService.add(study, user, ownerPosition);
		return study;
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() ->
						new EntityNotFoundException("존재하지 않는 카테고리입니다. categoryId = " + categoryId));
	}

}
