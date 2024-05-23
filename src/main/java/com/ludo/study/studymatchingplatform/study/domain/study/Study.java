package com.ludo.study.studymatchingplatform.study.domain.study;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.common.utils.LocalDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.study.category.Category;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Role;
import com.ludo.study.studymatchingplatform.study.service.exception.InvalidReviewPeriodException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Slf4j
public class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "char(10)")
    private StudyStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "char(20)")
    private Platform platform;

    @Column(nullable = true, length = 2048)
    @Size(max = 2048)
    private String platformUrl;

    @Column(nullable = false, length = 50)
    private String title;

    @OneToOne(mappedBy = "study", fetch = LAZY)
    private Recruitment recruitment;

    @OneToMany(mappedBy = "study", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Participant> participants = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "char(10)")
    private Way way;

    @Column(nullable = false)
    private Integer participantLimit;

    // null 제거 필요
    @Column(nullable = false)
    private Integer participantCount;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    public void addParticipant(final Participant participant) {
        this.participants.add(participant);
        this.participantCount = this.participants.size();
    }

    public void registerRecruitment(final Recruitment recruitment) {
        this.recruitment = recruitment;
        this.recruitment.connectToStudy(this);
    }

    public void changeStatus(final StudyStatus status) {
        this.status = status;
    }

    public void addRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
    }

    public Integer getParticipantCount() {
        return participants.size();
    }

    public String getCategoryByName() {
        return category.getName();
    }

    public Long getCategoryId() {
        return category.getId();
    }

    public Long getOwnerId() {
        return owner.getId();
    }

    public String getOwnerNickname() {
        return owner.getNickname();
    }

    public String getOwnerEmail() {
        return owner.getEmail();
    }

    public String getCategoryName() {
        return category.getName();
    }

    public void ensureRecruitmentWritableBy(final User user) {
        if (recruitment != null && !recruitment.isDeleted()) {
            throw new IllegalArgumentException("이미 작성된 모집 공고가 존재합니다.");
        }

        if (!isOwner(user)) {
            throw new IllegalArgumentException("모집 공고를 작성할 권한이 없습니다.");
        }
    }

    public Recruitment ensureRecruitmentEditable(final User user) {
        if (!owner.getId().equals(user.getId())) {
            throw new IllegalArgumentException("모집 공고를 수정할 권한이 없습니다.");
        }
        if (recruitment == null || recruitment.isDeleted()) {
            throw new IllegalArgumentException("존재하지 않는 모집 공고입니다.");
        }
        return recruitment;
    }

    public void ensureStudyEditable(final User user) {
        if (!owner.getId().equals(user.getId())) {
            throw new IllegalArgumentException("스터디를 수정할 권한이 없습니다.");
        }
    }

    public Boolean isOwner(final User user) {
        return Objects.equals(owner.getId(), user.getId());
    }

    public Boolean isOwner(final Participant participant) {
        return participant.matchesUser(owner);
    }

    public void ensureRecruiting() {
        if (status != StudyStatus.RECRUITING) {
            throw new IllegalStateException("현재 모집 중인 스터디가 아닙니다.");
        }
    }

    public void acceptApplicant(final User owner, final User applicantUser) {
        ensureAcceptApplicant(owner, applicantUser);
        accept(applicantUser);
        if (isMaxParticipantCount()) {
            changeStatus(StudyStatus.RECRUITED);
        }
    }

    public void rejectApplicant(final User owner, final User applicantUser) {
        ensureRejectApplicant(owner, applicantUser);
        recruitment.rejectApplicant(applicantUser);
    }

    private void ensureRejectApplicant(final User owner, final User applicantUser) {
        ensureCorrectOwner(owner);
        ensureApplicantUserIsNotOwner(owner, applicantUser);
        recruitment.ensureCorrectApplicantUser(applicantUser);
    }

    private void ensureApplicantUserIsNotOwner(final User owner, final User applicantUser) {
        if (owner.equals(applicantUser)) {
            throw new IllegalArgumentException("스터디 장과 지원자가 같습니다.");
        }
    }

    private void ensureAcceptApplicant(final User owner, final User applicantUser) {
        ensureCorrectOwner(owner);
        ensureApplicantUserIsNotOwner(owner, applicantUser);
        ensureRecruiting();
        ensureRemainParticipantLimit();
        recruitment.ensureCorrectApplicantUser(applicantUser);
    }

    private void ensureCorrectOwner(final User owner) {
        log.info("스터디장 = {}", this.owner.getId());
        log.info("파라미터 = {}", owner.getId());
        if (!isOwner(owner)) {
            throw new IllegalStateException(
                    String.format("스터디 장이 아닙니다. 스터디 장 id = %s, 잘못된 id = %s", this.owner, owner));
        }
    }

    private void ensureRemainParticipantLimit() {
        if (Objects.equals(getParticipantCount(), participantLimit)) {
            throw new IllegalStateException("남아있는 자리가 없습니다.");
        }
    }

    private void accept(final User applicantUser) {
        recruitment.acceptApplicant(applicantUser);
        final Applicant applicant = recruitment.getApplicant(applicantUser);
        addParticipant(Participant.from(this, applicantUser, applicant.getPosition(), Role.MEMBER));
    }

    private Boolean isMaxParticipantCount() {
        return Objects.equals(participantCount, participantLimit);
    }

    public Boolean allParticipating(final Long... userIds) {
        return Arrays.stream(userIds).allMatch(this::isParticipating);
    }

    public Boolean isParticipating(final User user) {
        return isParticipating(user.getId());
    }

    public Boolean isParticipating(final Long userId) {
        return participants.stream()
                .anyMatch(p -> p.matchesUser(userId));
    }

    public Integer getDday() {
        return Period.between(startDateTime.toLocalDate(), endDateTime.toLocalDate()).getDays();
    }

    public Participant getParticipant(final User user) {
        return getParticipant(user.getId());
    }

    public Optional<Participant> findParticipant(final Long userId) {
        return participants.stream()
                .filter(p -> p.matchesUser(userId))
                .findFirst();
    }

    public Participant getParticipant(final Long userId) {
        return findParticipant(userId)
                .orElseThrow(() -> new IllegalStateException("현재 참여 중인 스터디원이 아닙니다."));
    }

    public void removeParticipant(final Participant participant) {
        participants.removeIf(p -> Objects.equals(p, participant));
    }

    public void modifyStatus(final StudyStatus status, final LocalDateTime now) {
        // 모집 마감 상태는 시간에 따라 다른 결과 반영
        if (status == StudyStatus.RECRUITED) {
            modifyStatusToRecruited(now);
            modifyStatusToProgress(now);
        }
    }

    public void modifyStatusToRecruiting() {
        this.status = StudyStatus.RECRUITING;
    }

    private void modifyStatusToRecruited(final LocalDateTime now) {
        if (this.startDateTime.isAfter(now)) {
            this.status = StudyStatus.RECRUITED;
        }
    }

    private void modifyStatusToProgress(final LocalDateTime now) {
        if (this.startDateTime.isBefore(now) && this.endDateTime.isAfter(now)) {
            this.status = StudyStatus.PROGRESS;
        }
    }

    public void modifyStatusToCompleted(final LocalDateTime now) {
        if (this.endDateTime.isBefore(now)) {
            this.status = StudyStatus.COMPLETED;
        }
    }

    public void deactivateForRecruitment() {
        this.recruitment.softDelete();
    }

    public void activateForRecruitment() {
        this.recruitment.activate();
    }

    public Boolean ensureHasRecruitment() {
        if (this.recruitment != null && !this.recruitment.isDeleted()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void update(final String title, final Category category, final Integer participantLimit,
                       final Way way, final Platform platform, final String platformUrl,
                       final LocalDateTime startDateTime, final LocalDateTime endDateTime) {
        if (title != null) {
            this.title = title;
        }
        if (category != null) {
            this.category = category;
        }
        if (participantLimit != null) {
            this.participantLimit = participantLimit;
        }
        if (way != null) {
            this.way = way;
        }
        if (platform != null) {
            this.platform = platform;
        }
        if (platformUrl != null) {
            this.platformUrl = platformUrl;
        }
        if (startDateTime != null) {
            this.startDateTime = startDateTime;
        }
        if (endDateTime != null) {
            this.endDateTime = endDateTime;
        }
    }

    public void ensureReviewPeriodAvailable(final LocalDateTimePicker localDateTimePicker) {
        final LocalDateTime now = localDateTimePicker.now();
       final LocalDateTime reviewAvailStartTime = getReviewAvailStartTime();
       final LocalDateTime reviewAvailEndTime = getReviewAvailEndTime();
        final LocalDateTime reviewAvailEndTime = endDateTime.plusDays(14).plusSeconds(1);

        // Order Specific
        if (isAvailableReviewPeriod()) {
            return;
        }
        throw new InvalidReviewPeriodException("리뷰 작성 기간이 아닙니다.", reviewAvailStartTime, reviewAvailEndTime);
            throw new InvalidReviewPeriodException("리뷰 작성 기간이 지났습니다.", reviewAvailStartTime, reviewAvailEndTime);
        }

        if (isNotReviewPeriodYet(now)) {
            throw new InvalidReviewPeriodException("아직 리뷰 작성 기간이 되지 않았습니다.", reviewAvailStartTime, reviewAvailEndTime);
        }

        System.out.println("min avail: " + reviewAvailStartTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("max avail: " + reviewAvailEndTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("now : " + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    }

    public boolean isNotReviewPeriodYet(final LocalDateTime now) {
        final LocalDateTime reviewAvailStartTime = endDateTime.plusDays(3);
        return now.isBefore(reviewAvailStartTime);
    }

    public boolean isReviewPeriodElapsed(final LocalDateTime now) {
        final LocalDateTime reviewAvailEndTime = endDateTime.plusDays(14);
        return now.isAfter(reviewAvailEndTime);
    }
    
    private boolean isAvailableReviewPeriod() {
        final LocalDateTime reviewAvailStartTime = getReviewAvailStartTime();
        final LocalDateTime reviewAvailEndTime = getReviewAvailEndTime();
        
        return now.isAfter(getReviewAvailStartTime) && now.isBefore(reviewAvailEndTime);
    }
    
    private LocalDateTime getReviewAvailStartTime() {
        return endDateTime;
    }
    
    private LocalDateTime getReviewAvailEndTime() {
        return endDateTime.plusDays(14);
    }
}
