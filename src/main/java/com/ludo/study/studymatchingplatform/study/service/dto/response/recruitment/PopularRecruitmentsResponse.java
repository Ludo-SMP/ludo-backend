package com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment;

import java.util.List;

import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.RecruitmentPreviewResponse;

public record PopularRecruitmentsResponse(List<RecruitmentPreviewResponse> popularCodingRecruitments,
										  List<RecruitmentPreviewResponse> popularInterviewRecruitments,
										  List<RecruitmentPreviewResponse> popularProjectRecruitments) {
}
