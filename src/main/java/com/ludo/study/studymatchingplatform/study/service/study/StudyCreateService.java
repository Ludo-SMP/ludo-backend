package com.ludo.study.studymatchingplatform.study.service.study;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.BusinessException;
import com.ludo.study.studymatchingplatform.study.service.exception.NotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// 캐시 제거 적용 필요
public class StudyCreateService {

	private final StudyRepositoryImpl studyRepository;
	private final CategoryRepositoryImpl categoryRepository;
	private final PositionRepositoryImpl positionRepository;
	private final ParticipantRepositoryImpl participantRepository;
	private final UserRepositoryImpl userRepository;

	@Transactional
	public StudyResponse create(final WriteStudyRequest request, final User user) {
		final User owner = valifyExistUser(user.getId());
		final Category category = findCategoryById(request.categoryId());
		final Position ownerPosition = findPositionById(request.positionId());
		final Platform platform = valifyExistPlatform(request.platform());
		final Way way = valifyExistWay(request.way());
		final Study study = request.toStudy(owner, category, way, platform);

		// 생성된 스터디의 endDateTime 이 현재보다 이전일 경우 진행 완료 상태로 변경
		final LocalDateTime now = LocalDateTime.now();
		study.modifyStatusToCompleted(now);

		final Participant participant = Participant.from(study, owner, ownerPosition, Role.OWNER);
		study.addParticipant(participant);
		valifyEmptyParticipants(study);

		studyRepository.save(study);
		participantRepository.save(participant);
		return StudyResponse.from(study);
	}

	private Position findPositionById(final Long positionId) {
		return positionRepository.findById(positionId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 포지션 입니다."));
	}

	private Category findCategoryById(final Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 카테고리 입니다."));
	}

	private User valifyExistUser(final Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("존재하지 않는 사용자 입니다."));
	}

	private Platform valifyExistPlatform(final String platform) {
		return Stream.of(Platform.values())
				.filter(p -> p.name().equals(platform))
				.findFirst()
				.orElseThrow(() -> new NotFoundException("존재하지 않는 플랫폼 입니다."));
	}

	private Way valifyExistWay(final String way) {
		return Stream.of(Way.values())
				.filter(w -> w.name().equals(way))
				.findFirst()
				.orElseThrow(() -> new NotFoundException("존재하지 않는 진행 방식 입니다."));
	}

	private void valifyEmptyParticipants(final Study study) {
		final List<Participant> participants = study.getParticipants();
		if (participants.isEmpty()) {
			throw new BusinessException("스터디 생성시 참여자 리스트는 비어있을 수 없습니다.");
		}
	}

}
