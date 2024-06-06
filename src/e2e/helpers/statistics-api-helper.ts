import { ApiClient } from "../config/api-client";
import { Review } from "../types/reviews-types";
import { ReviewStatistics } from "../types/statistics-types";

export type WriteReviewResponse = { review: Review };

///
export type StudyStatisticsResponse = {
  studyStatistics: StudyStatisticsResponse;
};

export async function getMyStudyStatistics(apiClient: ApiClient) {
  return apiClient.get<StudyStatisticsResponse>(`/statistics/studies`);
}

/// 

export type ReviewStatisticsResponse = { reviewStatistics: ReviewStatistics };

export async function getMyReviewStatistics(apiClient: ApiClient) {
  return apiClient.get<ReviewStatisticsResponse>(`/statistics/reviews`);
}
