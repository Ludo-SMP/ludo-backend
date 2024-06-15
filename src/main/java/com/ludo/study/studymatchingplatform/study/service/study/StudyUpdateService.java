package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Platform;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.Way;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.category.CategoryRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.StudyUpdateRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StudyUpdateService {

    private final StudyRepositoryImpl studyRepository;
    private final CategoryRepositoryImpl categoryRepository;
    private final PositionRepositoryImpl positionRepository;
    private final ParticipantRepositoryImpl participantRepository;
    private final UserRepositoryImpl userRepository;

	public StudyResponse update(final User user, final Long studyId, final StudyUpdateRequest request) {
		final User owner = valifyExistUser(user.getId());
		final Category category = findCategoryById(request.categoryId());
		final Platform platform = valifyExistPlatform(request.platform());
		final Way way = valifyExistWay(request.way());
		final Study study = findStudyById(studyId);
		study.ensureStudyEditable(owner);
		study.update(request.title(), category, request.participantLimit(),
				request.attendanceDay(), way, platform, request.platformUrl(),
				request.startDateTime(), request.endDateTime());

        // 수정된 endDateTime이 현재 시간 이전일 경우 진행 완료 상태로 변경
        final LocalDateTime now = LocalDateTime.now();
        study.modifyStatusToCompleted(now);

        final Position ownerPosition = findPositionById(request.positionId());
        final Participant participant = findParticipantByIds(study.getId(), user.getId());
        participant.updatePosition(ownerPosition);

        studyRepository.save(study);
        participantRepository.save(participant);
        return StudyResponse.from(study);
    }

    private Study findStudyById(final Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));
    }

    private Position findPositionById(final Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 포지션입니다."));
    }

    private Category findCategoryById(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new EntityNotFoundException("존재하지 않는 카테고리입니다"));
    }

    private Platform valifyExistPlatform(final String platform) {
        return Stream.of(Platform.values())
                .filter(p -> p.name().equals(platform))
                .findFirst()
                .orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 플랫폼 입니다."));
    }

    private Way valifyExistWay(final String way) {
        return Stream.of(Way.values())
                .filter(w -> w.name().equals(way))
                .findFirst()
                .orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 진행 방식 입니다."));
    }

    private User valifyExistUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 사용자 입니다."));
    }

    private Participant findParticipantByIds(final Long studyId, final Long userId) {
        return participantRepository.find(studyId, userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 참가자 입니다."));
    }

}
