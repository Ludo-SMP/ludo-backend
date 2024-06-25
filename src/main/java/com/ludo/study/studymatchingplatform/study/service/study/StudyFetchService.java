package com.ludo.study.studymatchingplatform.study.service.study;

import java.time.LocalDateTime;
import java.util.List;

import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyWithReviewedParticipantResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyFetchService {

    private final StudyRepositoryImpl studyRepository;
    private final UtcDateTimePicker utcDateTimePicker;
    private final StudyStatusService studyStatusService;
    private final ReviewRepositoryImpl reviewRepository;

    @Transactional
    public StudyWithReviewedParticipantResponse getStudyDetails(final User user, final Long studyId) {
        final Study study = studyRepository.findByIdWithParticipants(studyId)
                .orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));

        if (!study.isParticipating(user)) {
            throw new IllegalArgumentException("스터디원만 조회 가능합니다.");
        }

        // endDateTime 에 따른 진행 완료 상태 변경
        final LocalDateTime now = utcDateTimePicker.now();
        if (study.getStatus() != StudyStatus.COMPLETED
                && study.getEndDateTime().isBefore(now)) { // 스터디 진행완료
            studyStatusService.end(user, studyId);
        }

        final List<Review> reviews = reviewRepository.findAllByStudyId(studyId).stream()
                .toList();
        return StudyWithReviewedParticipantResponse.from(study, reviews);
    }

}
