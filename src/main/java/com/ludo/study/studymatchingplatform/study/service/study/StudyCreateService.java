package com.ludo.study.studymatchingplatform.study.service.study;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.study.participant.ParticipantService;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

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
		participantService.add(study, user, ownerPosition, Role.OWNER);
		return study;
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() ->
						new EntityNotFoundException("존재하지 않는 카테고리입니다. categoryId = " + categoryId));
	}

}
