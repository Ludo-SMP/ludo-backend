import { WriteReviewRequest } from "../types/reviews-types";
import { randInt } from "./rand-fixture";

export type WriteReviewRequestArgs = Pick<WriteReviewRequest, "revieweeId"> &
  Partial<WriteReviewRequest>;

export function fakeWriteReviewRequest({
  activenessScore,
  communicationScore,
  professionalismScore,
  recommendScore,
  revieweeId,
  togetherScore,
}: WriteReviewRequestArgs) {
  return {
    revieweeId,
    activenessScore: activenessScore ?? randInt(1, 5),
    communicationScore: communicationScore ?? randInt(1, 5),
    professionalismScore: professionalismScore ?? randInt(1, 5),
    recommendScore: recommendScore ?? randInt(1, 5),
    togetherScore: togetherScore ?? randInt(1, 5),
  };
}
