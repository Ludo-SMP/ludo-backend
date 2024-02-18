package com.ludo.study.studymatchingplatform.study.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.StudyStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Applicant;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.ApplicantStatus;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.repository.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.ApplicantRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentPositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.recruitment.RecruitmentStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.stack.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.EditRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.user.domain.User;

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
	public Recruitment write(final User user, final WriteRecruitmentRequest request) {
		final Study study = studyRepository.findByIdWithRecruitment(request.getStudyId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));

		study.ensureRecruitmentWritableBy(user);

		final Recruitment recruitment = request.toRecruitment(study);
		recruitmentRepository.save(recruitment);

		recruitmentStackService.addMany(recruitment, request.getStackIds());
		recruitmentPositionService.addMany(recruitment, request.getPositionIds());

		return recruitment;
	}

	public Recruitment edit(final User user, final Long recruitmentId, final EditRecruitmentRequest request) {
		final Recruitment recruitment = recruitmentRepository.findByIdWithStudy(recruitmentId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 공고입니다."));

		recruitment.ensureEditable(user);

		recruitmentStackService.update(recruitment, request.getStackIds());
		recruitmentPositionService.update(recruitment, request.getPositionIds());

		final Set<Stack> nextStacks = stackRepository.findByIdIn(request.getStackIds());
		final Set<Position> nextPositions = positionRepository.findByIdIn(request.getPositionIds());

		for (final Stack stack : nextStacks) {
			recruitment.getRecruitmentStack(stack)
					.ifPresent(recruitmentStack -> {
						recruitment.removeRecruitmentStack(recruitmentStack);
						recruitmentStackRepository.deleteById(recruitmentStack.getId());
					});
		}

		for (final Position position : nextPositions) {
			recruitment.getRecruitmentPosition(position)
					.ifPresent(recruitmentPosition -> {
						recruitment.removeRecruitmentPosition(recruitmentPosition);
						recruitmentPositionRepository.deleteById(recruitmentPosition.getId());
					});
		}

		recruitment.edit(
				request.getTitle(),
				request.getContent(),
				request.getCallUrl(),
				request.getRecruitmentLimit(),
				request.getRecruitmentEndDateTime()
		);

		return recruitment;
	}

	public Applicant apply(final User user, final long recruitmentId) {
		final Recruitment recruitment = recruitmentRepository.findByIdWithStudy(recruitmentId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 공고입니다."));
		recruitment.ensureRecruiting();

		final Optional<Applicant> applicant = recruitment.findApplicant(user);

		if (applicant.isEmpty()) {
			final Applicant newApplicant = Applicant.of(recruitment, user);
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

	public Applicant cancel(final User user, final long recruitmentId) {
		final Recruitment recruitment = recruitmentRepository.findByIdWithStudy(recruitmentId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집 공고입니다."));
		recruitment.ensureRecruiting();

		final Applicant applicant = recruitment.findApplicant(user)
				.orElseThrow(() -> new IllegalArgumentException("지원한 적 없는 모집 공고입니다."));

		final StudyStatus studyStatus = recruitment.getStudy().getStatus();
		applicant.ensureCancellable(studyStatus);
		applicant.changeStatus(ApplicantStatus.CANCELLED);

		return applicant;
	}

}
