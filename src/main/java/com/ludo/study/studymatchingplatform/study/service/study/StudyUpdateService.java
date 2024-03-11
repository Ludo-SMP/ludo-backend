package com.ludo.study.studymatchingplatform.study.service.study;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.StudyUpdateRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyUpdateService {

	private final StudyRepositoryImpl studyRepository;
	private final CategoryRepositoryImpl categoryRepository;
	private final PositionRepositoryImpl positionRepository;
	private final ParticipantRepositoryImpl participantRepository;

	public StudyResponse update(final User user, final Long studyId, final StudyUpdateRequest request) {
		final Category category = findCategoryById(request.categoryId());
		final Study study = findStudyById(studyId);
		study.ensureStudyEditable(user);
		study.update(request.title(), category, request.participantLimit(), request.way(), request.platform(),
				request.startDateTime(), request.endDateTime());
		study.ensureModifiableStatus();

		final Position ownerPosition = findPositionById(request.positionId());
		final Participant participant = findParticipantByIds(study.getId(), user.getId());
		participant.updatePosition(ownerPosition);
		studyRepository.save(study);
		participantRepository.save(participant);
		return StudyResponse.from(study);
	}

	private Study findStudyById(final Long studyId) {
		return studyRepository.findById(studyId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 스터디입니다."));
	}

	private Position findPositionById(final Long positionId) {
		return positionRepository.findById(positionId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 포지션입니다."));
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() ->
						new EntityNotFoundException("존재하지 않는 카테고리입니다"));
	}

	private Participant findParticipantByIds(final Long studyId, final Long userId) {
		return participantRepository.find(studyId, userId)
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참가자 입니다."));
	}

}
