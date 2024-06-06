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

export type ReviewResponse = {
  reviewerId: number;
  revieweeId: number;
  activenessScore: number;
  professionalismScore: number;
  communicationScore: number;
  togetherScore: number;
  recommendScore: number;
};

export type PeerReview = {
  selfReview: ReviewResponse;
  peerReview: ReviewResponse;
};
type PeerReviewsResponse = { reviews: PeerReview[] };

export async function getPeerReviews(apiClient: ApiClient, studyId: number) {
  return apiClient.get<PeerReviewsResponse>(`/studies/${studyId}/reviews`);
}
