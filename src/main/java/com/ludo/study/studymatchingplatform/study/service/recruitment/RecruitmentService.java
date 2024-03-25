package com.ludo.study.studymatchingplatform.study.service.recruitment;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.applicant.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.position.Position;
import com.ludo.study.studymatchingplatform.study.domain.study.Study;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.applicant.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.position.RecruitmentPositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.RecruitmentStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.study.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.EditRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.applicant.ApplyRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.WriteRecruitmentStudyInfoResponse;
import com.ludo.study.studymatchingplatform.study.service.recruitment.position.RecruitmentPositionService;
import com.ludo.study.studymatchingplatform.study.service.recruitment.stack.RecruitmentStackService;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitmentService {

	private final RecruitmentRepositoryImpl recruitmentRepository;
	private final StudyRepositoryImpl studyRepository;
	private final RecruitmentStackRepositoryImpl recruitmentStackRepository;
	private final RecruitmentStackService recruitmentStackService;
	private final RecruitmentPositionRepositoryImpl recruitmentPositionRepository;
	private final RecruitmentPositionService recruitmentPositionService;
	private final StackRepositoryImpl stackRepository;
	private final PositionRepositoryImpl positionRepository;
	private final ApplicantRepositoryImpl applicantRepository;

	@Transactional
	public Recruitment write(final User user, final WriteRecruitmentRequest request,
							 final Long studyId) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));
		// 모집 마감을을 과거의 시간으로 설정할 경우 예외 발생
		validateRecruitmentEndDateTime(request.getRecruitmentEndDateTime());
		study.ensureRecruitmentWritableBy(user);

		final Recruitment recruitment = request.toRecruitment(study);
		recruitmentRepository.save(recruitment);

		recruitmentStackService.addMany(recruitment, request.getStackIds());
		recruitmentPositionService.addMany(recruitment, request.getPositionIds());
		study.registerRecruitment(recruitment);

		// 모집 공고 생성시 스터디 상태를 모집중으로 변경
		study.modifyStatusToRecruiting();

		return recruitment;
	}

	public void validateRecruitmentEndDateTime(final LocalDateTime recruitmentEndDateTime) {
		if (recruitmentEndDateTime.isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("모집 마감일은 과거의 시간일 수 없습니다.");
		}
	}

	public WriteRecruitmentStudyInfoResponse getStudyInfo(final User user, final Long studyId) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));
		study.ensureRecruitmentWritableBy(user);
		return WriteRecruitmentStudyInfoResponse.from(study);
	}

	public Recruitment edit(final User user, final Long recruitmentId, final EditRecruitmentRequest request) {
		final Recruitment recruitment = recruitmentRepository.findByIdWithStudy(recruitmentId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 공고입니다."));

		recruitment.ensureEditable(user);

		recruitmentStackService.update(recruitment, request.getStackIds());
		recruitmentPositionService.update(recruitment, request.getPositionIds());

		recruitment.edit(
				request.getTitle(),
				request.getContact(),
				request.getCallUrl(),
				request.getApplicantCount(),
				request.getRecruitmentEndDateTime(),
				request.getContent()
		);

		return recruitment;
	}

	public Applicant apply(final User user, final Long recruitmentId, final ApplyRecruitmentRequest request) {
		final Recruitment recruitment = recruitmentRepository.findByIdWithStudy(recruitmentId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 공고입니다."));
		recruitment.ensureRecruiting();
		recruitment.ensureApplicable(user);
		final Optional<Applicant> applicant = recruitment.findApplicant(user);
		final Position position = positionRepository.findById(request.positionId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포지션입니다."));

		if (applicant.isEmpty()) {
			final Applicant newApplicant = Applicant.of(recruitment, user, position);
			applicantRepository.save(newApplicant);
			final Applicant savedApplicant = applicantRepository.save(newApplicant);
			recruitment.addApplicant(savedApplicant);

			return savedApplicant;
		} else {
			final Applicant reapplicant = applicant.get();
			reapplicant.ensureApplicable();
			reapplicant.reapply();

			return reapplicant;
		}

	}

	public Applicant cancel(final User user, final Long recruitmentId) {
		final Recruitment recruitment = recruitmentRepository.findByIdWithStudy(recruitmentId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 공고입니다."));
		recruitment.ensureRecruiting();

		final Applicant applicant = recruitment.findApplicant(user)
				.orElseThrow(() -> new IllegalArgumentException("지원한 적 없는 모집 공고입니다."));

		final StudyStatus studyStatus = recruitment.getStudy().getStatus();
		applicant.ensureCancellable(studyStatus);
		applicant.changeStatus(ApplicantStatus.CANCELLED);
		applicant.softDelete();

		return applicant;
	}

	public void delete(final User user, final Long studyId) {
		final Study study = studyRepository.findByIdWithRecruitment(studyId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디 입니다."));
		study.ensureRecruitmentEditable(user);
		study.deactivateForRecruitment();
		recruitmentRepository.save(study.getRecruitment());
	}

}
