package com.ludo.study.studymatchingplatform.study.service.study;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
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
	public StudyResponse create(final WriteStudyRequest request, final User user) {
		final Category category = findCategoryById(request.categoryId());
		final Study study = request.toStudy(user, category, request.platform());
		final Position ownerPosition = findPositionById(request.positionId());

		// 생성된 스터디의 endDateTime 이 현재보다 이전일 경우 진행 완료 상태로 변경
		study.modifyStatusToCompleted();

		studyRepository.save(study);
		participantService.add(study, user, ownerPosition, Role.OWNER);
		return StudyResponse.from(study);
	}

	private Position findPositionById(final Long positionId) {
		return positionRepository.findById(positionId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 포지션입니다."));
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() ->
						new EntityNotFoundException("존재하지 않는 카테고리입니다. categoryId = " + categoryId));
	}

}
