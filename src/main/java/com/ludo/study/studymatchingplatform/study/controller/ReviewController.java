package com.ludo.study.studymatchingplatform.study.controller;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.study.service.dto.request.study.WriteReviewRequest;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.PeerReviewsResponse;
import com.ludo.study.studymatchingplatform.study.service.dto.response.study.WriteReviewResponse;
import com.ludo.study.studymatchingplatform.study.service.study.ReviewFacade;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final ReviewFacade reviewFacade;

    @ResponseStatus(HttpStatus.OK)
    @DataFieldName("reviews")
    @Operation(description = "특정 스터디에 대한 리뷰 목록 조회")
    @GetMapping("/studies/{studyId}/reviews")
    public List<PeerReviewsResponse> getPeerReviews(@Parameter(hidden = true) @AuthUser final Long userId, @PathVariable("studyId") Long studyId) {
        return reviewFacade.getPeerReviews(studyId, userId);
    }

    @PostMapping("/studies/{studyId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    @DataFieldName("review")
    @Operation(description = "리뷰 작성")
    @ApiResponse(description = "리뷰 작성 성공", responseCode = "201", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public WriteReviewResponse write(@Parameter(hidden = true) @AuthUser final User user, @PathVariable("studyId") Long studyId, @Valid @RequestBody WriteReviewRequest request) {
        return reviewFacade.write(request, studyId, user);
    }

}
