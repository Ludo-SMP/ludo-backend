package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyFetchService {

    private final StudyRepositoryImpl studyRepository;

    public StudyResponse getStudyDetails(final User user, final Long studyId) {
        final Study study = studyRepository.findByIdWithParticipants(studyId)
                .orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));

        if (!study.isParticipating(user)) {
            throw new IllegalArgumentException("스터디원만 조회 가능합니다.");
        }
        // endDateTime 에 따른 진행 완료 상태 변경
        final LocalDateTime now = LocalDateTime.now();
        study.modifyStatusToCompleted(now);
        return StudyResponse.from(study);
    }

}
