import { ApiClient } from "../config/api-client";
import { Review, WriteReviewRequest } from "../types/reviews-types";

export type WriteReviewResponse = { review: Review };

export async function writeReview(
  apiClient: ApiClient,
  studyId: number,
  {
    activenessScore,
    communicationScore,
    professionalismScore,
    recommendScore,
    revieweeId,
    togetherScore,
  }: WriteReviewRequest
) {
  return apiClient.post<WriteReviewResponse>(`/studies/${studyId}/reviews`, {
    activenessScore,
    communicationScore,
    professionalismScore,
    recommendScore,
    revieweeId,
    togetherScore,
  });
}
