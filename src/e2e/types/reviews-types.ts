import { User } from "./users-types";

export interface WriteReviewRequest {
  revieweeId: number;
  activenessScore: number;
  professionalismScore: number;
  communicationScore: number;
  togetherScore: number;
  recommendScore: number;
}

export interface Review {
  id: number;
  studyId: number;
  reviewer: User;
  reviewee: User;
  activenessScore: number;
  professionalismScore: number;
  communicationScore: number;
  togetherScore: number;
  recommendScore: number;
}

export type ReviewWithUserIds = Omit<Review, "reviewer" | "reviewee"> & { reviewerId: number; revieweeId: number };
