package com.ludo.study.studymatchingplatform.study.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ludo.study.studymatchingplatform.study.domain.Position;
import com.ludo.study.studymatchingplatform.study.domain.Study;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.Recruitment;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentPosition;
import com.ludo.study.studymatchingplatform.study.domain.recruitment.RecruitmentStack;
import com.ludo.study.studymatchingplatform.study.domain.stack.Stack;
import com.ludo.study.studymatchingplatform.study.repository.PositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentPositionRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.RecruitmentStackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.StackRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.repository.StudyRepositoryImpl;
import com.ludo.study.studymatchingplatform.study.service.dto.EditRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.WriteRecruitmentRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.ApplyRecruitmentRequest;
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

	public Recruitment write(final WriteRecruitmentRequest request) {
		final Study study = studyRepository.findByIdWithRecruitment(request.getStudyId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));

		study.ensureRecruitmentWritable();

		final Recruitment recruitment = request.toRecruitment(study);

		final List<RecruitmentStack> recruitmentStacks = recruitmentStackService.createMany(recruitment,
			request.getStackIds());
		final List<RecruitmentPosition> recruitmentPositions = recruitmentPositionService.createMany(recruitment,
			request.getPositionIds());

		recruitment.addRecruitmentStacks(recruitmentStacks);
		recruitment.addRecruitmentPositions(recruitmentPositions);

		recruitmentRepository.save(recruitment);
		return recruitment;
	}

	public Recruitment edit(
		final User user,
		final EditRecruitmentRequest request
	) {

		final Study study = studyRepository.findByIdWithRecruitment(request.getStudyId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다."));

		study.ensureRecruitmentEditable(user);

		final Recruitment recruitment = study.getRecruitment();

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
			request.getHits(),
			request.getRecruitmentLimit(),
			request.getRecruitmentEndDateTime()
		);

		return recruitment;
	}

	public void apply(final ApplyRecruitmentRequest request) {
	}
}
