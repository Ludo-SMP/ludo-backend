package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Review;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteReviewRequest;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public final class ReviewServiceTest {

    private static final UtcDateTimePicker utcDateTimePicker = new FixedUtcDateTimePicker();
    private final ReviewService reviewService = new ReviewService(utcDateTimePicker);

    @DisplayName("[Success] 스터디 종료 이후 14일 내에 리뷰를 작성할 수 있다.")
    @ParameterizedTest
    @MethodSource("validEndDateTimes")
    void writeReview(final LocalDateTime endDateTime) {
        // given
        final User reviewer = User.builder().id(1L).nickname("reviewer").email("reviewer@aa.aa").build();
        final User reviewee = User.builder().id(2L).nickname("reviewee").email("reviewee@aa.aa").build();
        final Study study = Study.builder().owner(reviewer).endDateTime(endDateTime).build();
        final Position position = Position.builder().name("position").build();
        study.addParticipant(reviewer, position, Role.OWNER);
        study.addParticipant(reviewee, position, Role.MEMBER);

        final WriteReviewRequest request = WriteReviewRequest.builder().revieweeId(reviewee.getId()).activenessScore(5L).professionalismScore(5L).communicationScore(5L).togetherScore(5L).recommendScore(5L).build();


        // when
        final Review review = reviewService.write(request, study, reviewer);

        // then
        assertThat(review.getReviewer()).isEqualTo(reviewer);
        assertThat(review.getReviewee()).isEqualTo(reviewee);

    }

    private static Stream<LocalDateTime> validEndDateTimes() {
        final LocalDateTime now = utcDateTimePicker.now();
        return Stream.of(now.minusDays(3), now.minusDays(13), now.minusDays(14));

    }


    @DisplayName("[Exception] 스터디가 종료 이후 ~ 14일 범위가 지나면 리뷰를 작성할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidEndDateTimes")
    void writeReviewFailure(final LocalDateTime endDateTime) {
        // given
        final User reviewer = User.builder()
                .id(1L)
                .nickname("reviewer")
                .email("reviewer@aa.aa")
                .build();
        final User reviewee = User.builder()
                .id(2L)
                .nickname("reviewee")
                .email("reviewee@aa.aa")
                .build();
        final Study study = Study.builder()
                .owner(reviewer)
                .endDateTime(endDateTime)
                .build();
        final Position position = Position.builder()
                .name("position")
                .build();
        study.addParticipant(reviewer, position, Role.OWNER);
        study.addParticipant(reviewee, position, Role.MEMBER);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(reviewee.getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> reviewService.write(request, study, reviewer))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("[Exception] 스터디가 종료되기 전에는 리뷰를 작성할 수 없으며, 사용자에게 이를 인지하기 위한 적절한 안내 메세지를 보여준다.")
    @Test
    void writeReviewFailureBeforeThreeDaysAfterStudyDone() {
        // given
        final LocalDateTime now = utcDateTimePicker.now();
        final LocalDateTime endDateTime = now.plusSeconds(1);

        final User reviewer = User.builder()
                .id(1L).nickname("reviewer")
                .email("reviewer@aa.aa")
                .build();
        final User reviewee = User.builder()
                .id(2L).nickname("reviewee")
                .email("reviewee@aa.aa")
                .build();
        final Study study = Study.builder()
                .owner(reviewer).endDateTime(endDateTime)
                .build();
        final Position position = Position.builder().
                name("position")
                .build();
        study.addParticipant(reviewer, position, Role.OWNER);
        study.addParticipant(reviewee, position, Role.MEMBER);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(reviewee.getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> reviewService.write(request, study, reviewer))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("아직 리뷰 작성 기간이 되지 않았습니다. 스터디 완료 후부터 리뷰 작성이 가능합니다.");

    }

    @DisplayName("[Exception] 스터디가 종료된 후 14일이 지난 후에는 리뷰를 작성할 수 없으며, 사용자에게 이를 인지하기 위한 적절한 안내 메세지를 보여준다.")
    @Test
    void writeReviewFailureAfterFourteenDaysAfterStudyDone() {
        // given
        final LocalDateTime now = utcDateTimePicker.now();
        final LocalDateTime endDateTime = now.minusDays(14).minusSeconds(1);

        final User reviewer = User.builder()
                .id(1L)
                .nickname("reviewer")
                .email("reviewer@aa.aa")
                .build();
        final User reviewee = User.builder()
                .id(2L)
                .nickname("reviewee")
                .email("reviewee@aa.aa")
                .build();
        final Study study = Study.builder().
                owner(reviewer)
                .endDateTime(endDateTime)
                .build();
        final Position position = Position.builder()
                .name("position")
                .build();
        study.addParticipant(reviewer, position, Role.OWNER);
        study.addParticipant(reviewee, position, Role.MEMBER);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(reviewee.getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> reviewService.write(request, study, reviewer))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("리뷰 작성 기간이 지났습니다. 리뷰 작성은 스터디 완료 후 최대 14일까지 가능합니다.");

    }

    private static Stream<LocalDateTime> invalidEndDateTimes() {
        final LocalDateTime now = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME;
        return Stream.of(now.plusSeconds(1), now.minusDays(14).minusSeconds(1));

    }


    @DisplayName("[Exception] Reviewee가 스터디원이 아닌 경우, 리뷰 작성이 불가능하다.")
    @Test
    void writeReviewToNonParticipants() {
        final LocalDateTime now = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME;
        final LocalDateTime endDateTime = now.minusDays(10);

        final User reviewer = User.builder()
                .id(1L)
                .nickname("reviewer")
                .email("reviewer@aa.aa")
                .build();
        final User notParticipantingReviewee = User.builder()
                .id(2L)
                .nickname("reviewee")
                .email("reviewee@aa.aa")
                .build();
        final Study study = Study.builder()
                .owner(reviewer)
                .endDateTime(endDateTime)
                .build();
        final Position position = Position.builder()
                .name("position")
                .build();
        study.addParticipant(reviewer, position, Role.OWNER);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(notParticipantingReviewee.getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();


        // when
        // then
        assertThatThrownBy(() -> reviewService.write(request, study, reviewer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("스터디에 참여 중인 사용자에게만 리뷰를 작성할 수 있습니다.");
    }

    @DisplayName("[Exception] Reviewer가 스터디원이 아닌 경우, 리뷰 작성이 불가능하다.")
    @Test
    void writeReviewIfNotParticipating() {

        final LocalDateTime now = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME;
        final LocalDateTime endDateTime = now.minusDays(10);

        final User notParticipatingReviewer = User.builder()
                .id(1L)
                .nickname("reviewer")
                .email("reviewer@aa.aa")
                .build();
        final User reviewee = User.builder()
                .id(2L)
                .nickname("reviewee")
                .email("reviewee@aa.aa")
                .build();
        final Study study = Study.builder()
                .owner(notParticipatingReviewer)
                .endDateTime(endDateTime)
                .build();
        final Position position = Position.builder()
                .name("position")
                .build();
        study.addParticipant(reviewee, position, Role.MEMBER);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(reviewee.getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> reviewService.write(request, study, notParticipatingReviewer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("참여 중인 스터디가 아닙니다. 리뷰를 작성할 수 없습니다.");
    }


    @DisplayName("[Exception] 자기 자신에게는 리뷰 작성이 불가능하다.")
    @Test
    void writeReviewForSelf() {

        final LocalDateTime now = FixedUtcDateTimePicker.DEFAULT_FIXED_UTC_DATE_TIME;
        final LocalDateTime endDateTime = now.minusDays(10);

        final User reviewer = User.builder()
                .id(1L)
                .nickname("reviewer")
                .email("reviewer@aa.aa")
                .build();
        final Study study = Study.builder()
                .owner(reviewer)
                .endDateTime(endDateTime)
                .build();
        final Position position = Position.builder()
                .name("position")
                .build();
        study.addParticipant(reviewer, position, Role.OWNER);

        final WriteReviewRequest request = WriteReviewRequest.builder()
                .revieweeId(reviewer.getId())
                .activenessScore(5L)
                .professionalismScore(5L)
                .communicationScore(5L)
                .togetherScore(5L)
                .recommendScore(5L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> reviewService.write(request, study, reviewer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자기 자신에게는 리뷰를 작성할 수 없습니다.");
    }

}