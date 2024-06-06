package com.ludo.study.studymatchingplatform.study.controller;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.study.domain.study.StudyStatus;
import com.ludo.study.studymatchingplatform.study.service.dto.request.recruitment.applicant.StudyApplicantDecisionRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.StudyUpdateRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteStudyRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.recruitment.applicant.ApplicantResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.StudyResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.participant.ParticipantUserResponse;
import com.ludo.study.studymatchingplatform.study.service.recruitment.applicant.StudyApplicantDecisionService;
import com.ludo.study.studymatchingplatform.study.service.study.*;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyCreateService studyCreateService;
    private final StudyFetchService studyFetchService;
    private final StudyStatusService studyStatusService;
    private final StudyService studyService;
    private final StudyUpdateService studyUpdateService;
    private final StudyApplicantDecisionService applicantDecisionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @DataFieldName("study")
    @Operation(description = "스터디 생성")
    @ApiResponse(description = "스터디 생성 성공", responseCode = "201", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public StudyResponse create(@RequestBody final WriteStudyRequest request,
                                @Parameter(hidden = true) @AuthUser final User user) {
        return studyCreateService.create(request, user);
    }

    @Transactional
    @PatchMapping("/{studyId}")
    @DataFieldName("study")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "스터디 상태 변경")
    @ApiResponse(description = "스터디 상태 변경 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public StudyResponse changeStatus(@PathVariable final Long studyId,
                                      @RequestParam("status") final StudyStatus status,
                                      @Parameter(hidden = true) @AuthUser final User user) {
        return studyStatusService.changeStatus(studyId, status, user);
    }

    @PostMapping("/{studyId}")
    @ResponseStatus(HttpStatus.OK)
    @DataFieldName("study")
    @Operation(description = "스터디 종료")
    @ApiResponse(description = "스터디 종료 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public StudyResponse end(@Parameter(hidden = true) @AuthUser final User user, @PathVariable("studyId") final Long studyId) {
        return studyStatusService.end(user, studyId);
    }

    @DeleteMapping("/{studyId}/participants")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "스터디 탈퇴")
    @ApiResponse(description = "스터디 탈퇴 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public void leave(@Parameter(hidden = true) @AuthUser final User user,
                      @PathVariable("studyId") final Long studyId) {
        studyService.leave(user, studyId);
    }

    @GetMapping("/{studyId}")
    @ResponseStatus(HttpStatus.OK)
    @DataFieldName("study")
    @Operation(description = "스터디 상세 정보 조회")
    @ApiResponse(description = "스터디 조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public StudyResponse getStudyDetails(@Parameter(hidden = true) @AuthUser final User user,
                                         @PathVariable("studyId") final Long studyId) {
        return studyFetchService.getStudyDetails(user, studyId);
    }

    @PostMapping("/{studyId}/apply-accept/{applicantUserId}")
    @ResponseStatus(HttpStatus.OK)
    @DataFieldName("participant")
    @Operation(description = "스터디 지원 수락")
    @ApiResponse(description = "스터디 지원 수락 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public ParticipantUserResponse applicantAccept(@Parameter(hidden = true) @AuthUser final User user,
                                                   @PathVariable final Long studyId,
                                                   @PathVariable Long applicantUserId) {

        final StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(studyId,
                applicantUserId);
        return applicantDecisionService.applicantAccept(user,
                studyApplicantDecisionRequest);
    }

    @PostMapping("/{studyId}/apply-refuse/{applicantUserId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "스터디 지원 거절")
    @ApiResponse(description = "스터디 지원 거절 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public void applicantRefuse(@Parameter(hidden = true) @AuthUser final User user,
                                @PathVariable final Long studyId,
                                @PathVariable Long applicantUserId) {

        final StudyApplicantDecisionRequest studyApplicantDecisionRequest = new StudyApplicantDecisionRequest(studyId,
                applicantUserId);
        applicantDecisionService.applicantReject(user, studyApplicantDecisionRequest);
    }

    @GetMapping("/{studyId}/applicants")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "스터디 지원자 정보")
    @ApiResponse(description = "스터디 지원자 정보 조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public ApplicantResponse findApplicantsInfo(
            @Parameter(hidden = true) @AuthUser final User user,
            @PathVariable("studyId") final Long studyId) {
        return studyService.findApplicantsInfo(user, studyId);
    }

    @PutMapping("/{studyId}")
    @ResponseStatus(HttpStatus.OK)
    @DataFieldName("study")
    @Operation(description = "스터디 수정")
    @ApiResponse(description = "스터디 수정 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public StudyResponse update(@Parameter(hidden = true) @AuthUser final User user,
                                @PathVariable final Long studyId,
                                @RequestBody final StudyUpdateRequest request) {
        return studyUpdateService.update(user, studyId, request);
    }

}
