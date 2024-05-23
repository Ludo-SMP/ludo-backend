package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.common.utils.LocalDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.study.ReviewRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteReviewRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.ReviewResponse;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepositoryImpl reviewRepository;
    private final StudyRepositoryImpl studyRepository;
    private final LocalDateTimePicker localDateTimePicker;

    public ReviewResponse write(WriteReviewRequest request, Long studyId, User reviewer) {
        final Study study = studyRepository.findByIdWithParticipants(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. 리뷰를 작성할 수 없습니다."));

        final Participant participantingReviewer = study.findParticipant(reviewer.getId())
                .orElseThrow(() -> new IllegalArgumentException("참여 중인 스터디가 아닙니다. 리뷰를 작성할 수 없습니다."));
        final Participant participantingReviewee = study.findParticipant(request.revieweeId())
                .orElseThrow(() -> new IllegalArgumentException("스터디에 참여 중인 사용자에게만 리뷰를 작성할 수 있습니다."));
        if (participantingReviewer == participantingReviewee) {
            throw new IllegalArgumentException("자기 자신에게는 리뷰를 작성할 수 없습니다.");
        }

        // 명세 - [스터디가 진행 완료 상태로 바뀐 후 3일 후 부터, 14일 간 리뷰 작성 가능]
        // 진행 완료 상태로 바뀐 뒤 날짜를 정확히 추적하기 위해서는 추가적인 state 및 논의 필요할듯
        // -> 일단 EndDateTime 기반으로 작성
        study.ensureReviewPeriodAvailable(localDateTimePicker);

        final boolean isDuplicateReview = reviewRepository.findAllByStudyId(studyId)
                .stream().anyMatch(review -> review.isDuplicateReview(studyId, reviewer.getId(), request.revieweeId()));
        if (isDuplicateReview) {
            throw new IllegalArgumentException("이미 리뷰를 작성 하셨습니다.");
        }

        final Review review = request.toReviewWithStudy(study, participantingReviewer.getUser(), participantingReviewee.getUser());
        return ReviewResponse.from(reviewRepository.save(review));

    }

}
