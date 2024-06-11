package com.ludo.study.studymatchingplatform.study.service.study;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantWithReviewStatisticsResponse;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepositoryImpl studyRepository;
    private final ApplicantRepositoryImpl applicantRepository;
    private final UtcDateTimePicker utcDateTimePicker;
    private final ReviewStatisticsService reviewStatisticsService;

    public void leave(final User user, final Long studyId) {
        final Study study = studyRepository.findByIdWithRecruitment(studyId)
                .orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));

        final Participant participant = study.getParticipant(user);
        if (study.isOwner(participant)) {
            throw new IllegalStateException("스터디장은 탈퇴가 불가능합니다.");
        }
        participant.leave(study, utcDateTimePicker.now());
    }

    public ApplicantWithReviewStatisticsResponse findApplicantsWithReviewStatistics(final User user, final Long studyId) {
        final Study study = findByIdWithRecruitment(studyId);
        // 스터디 참여자 검증
        study.getParticipant(user);
        final Recruitment recruitment = study.getRecruitment();
        final List<Applicant> applicants =
                applicantRepository.findStudyApplicantInfoByRecruitmentId(recruitment.getId());
        final List<Long> applicantsUserIds = applicants.stream()
                .map(a -> a.getUser().getId()).toList();

        return ApplicantWithReviewStatisticsResponse.from(
                study,
                applicants,
                reviewStatisticsService.findByUserIdsIn(applicantsUserIds)
        );
    }


    public ApplicantResponse findApplicantsInfo(final User user, final Long studyId) {
        final Study study = findByIdWithRecruitment(studyId);
        // 스터디 참여자 검증
        study.getParticipant(user);
        final Recruitment recruitment = study.getRecruitment();
        final List<Applicant> applicants =
                applicantRepository.findStudyApplicantInfoByRecruitmentId(recruitment.getId());
        return ApplicantResponse.from(study, applicants);
    }

    private Study findByIdWithRecruitment(final Long studyId) {
        return studyRepository.findByIdWithRecruitment(studyId)
                .orElseThrow(() -> new SocialAccountNotFoundException("존재하지 않는 스터디입니다."));
    }

}
