package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteReviewRequest;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final UtcDateTimePicker utcDateTimePicker;

    public Review write(WriteReviewRequest request, Study study, User reviewer) {
        final Participant participantingReviewer = study.findParticipant(reviewer.getId())
                .orElseThrow(() -> new IllegalArgumentException("참여 중인 스터디가 아닙니다. 리뷰를 작성할 수 없습니다."));
        final Participant participantingReviewee = study.findParticipant(request.revieweeId())
                .orElseThrow(() -> new IllegalArgumentException("스터디에 참여 중인 사용자에게만 리뷰를 작성할 수 있습니다."));
        if (participantingReviewer == participantingReviewee) {
            throw new IllegalArgumentException("자기 자신에게는 리뷰를 작성할 수 없습니다.");
        }

        // 명세 - [스터디가 진행 완료 상태로 바뀐 후 3일 후 부터, 14일 간 리뷰 작성 가능]
        study.ensureReviewPeriodAvailable(utcDateTimePicker);
        return request.toReviewWithStudy(study, participantingReviewer.getUser(), participantingReviewee.getUser());

    }

}
