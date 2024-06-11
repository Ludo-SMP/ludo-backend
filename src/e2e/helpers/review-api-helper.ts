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
  }: WriteReviewRequest,
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
  activeness: number;
  professionalism: number;
  communication: number;
  together: number;
  recommend: number;
};

export type PeerReview = {
  selfReview: ReviewResponse;
  peerReview: ReviewResponse;
};
export type StudyPeerReviews = {
  id: number;
  title: string;
  reviews: PeerReview[];
};
type StudyPeerReviewsResponse = { studies: StudyPeerReviews[] };

export async function getPeerReviews(apiClient: ApiClient) {
  return apiClient.get<StudyPeerReviewsResponse>(`/reviews`);
}
