package com.ludo.study.studymatchingplatform.study.controller;

import com.ludo.study.studymatchingplatform.auth.common.AuthUser;
import com.ludo.study.studymatchingplatform.common.annotation.DataFieldName;
import com.ludo.study.studymatchingplatform.study.service.dto.response.ReviewStatisticsResponse;
import com.ludo.study.studymatchingplatform.study.service.study.ReviewStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewStatisticsController {

    private final ReviewStatisticsService reviewStatisticsService;

    @GetMapping("/statistics/reviews")
    @ResponseStatus(HttpStatus.OK)
    @DataFieldName("reviewStatistics")
    @Operation(description = "리뷰 통계 조회")
    @ApiResponse(description = "리뷰 통계 조회 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public ReviewStatisticsResponse getReviewStatistics(@Parameter(hidden = true) @AuthUser final Long userId) {
        return reviewStatisticsService.findOrCreateByUserId(userId);
    }

}
