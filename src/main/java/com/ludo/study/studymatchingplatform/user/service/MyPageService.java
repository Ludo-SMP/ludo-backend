package com.ludo.study.studymatchingplatform.user.service;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.domain.id.ApplicantId;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.participant.Participant;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.participant.ParticipantRepositoryImpl;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.service.dto.response.MyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ParticipantRepositoryImpl participantRepository;
    private final ApplicantRepositoryImpl applicantRepository;
    private final StudyRepositoryImpl studyRepository;
    private final UtcDateTimePicker utcDateTimePicker;

    public MyPageResponse retrieveMyPage(final User user, final LocalDateTime now) {
        ensureStudyStatusCompleted(user, now);
        final List<Participant> participants = retrieveParticipantStudies(user);
        final List<Applicant> applicants = retrieveApplyRecruitment(user);
        final List<Participant> completedStudies = retrieveCompletedStudy(user);
        return MyPageResponse.from(user, participants, applicants, completedStudies);
    }

    // 테스트 케이스의 불변 유지하기 위해 now 를 컨트롤러에서 받아오도록 변경
    private void ensureStudyStatusCompleted(final User user, final LocalDateTime now) {
        final List<Participant> participants = participantRepository.findByUserId(user.getId());
        for (Participant participant : participants) {
            final Study study = participant.getStudy();
            study.modifyStatusToCompleted(now);
            studyRepository.save(study);
        }
    }

    private List<Participant> retrieveParticipantStudies(final User user) {
        return participantRepository.findByUserId(user.getId());
    }

    private List<Applicant> retrieveApplyRecruitment(final User user) {
        return applicantRepository.findMyPageApplyRecruitmentInfoByUserId(user.getId());
    }

    private List<Participant> retrieveCompletedStudy(final User user) {
        return participantRepository.findCompletedStudyByUserId(user.getId());
    }

    public void deleteApplyHistory(final User user, final Long recruitmentId) {
        final ApplicantId applicantId = new ApplicantId(recruitmentId, user.getId());
        final Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원 기록 입니다."));
        // 모집 공고 지원 기록 삭제 시 softDelete 처리
        applicant.softDelete(utcDateTimePicker.now());
        applicantRepository.save(applicant);
    }

}
