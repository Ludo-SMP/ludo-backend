import { addDays } from "date-fns";
import { localNow } from "../helpers/datetime-helper";
import { WriteRecruitmentRequest } from "../types/recruitment-types";
import { randOneOf, randPositionIds, randUrl } from "./position-fixture";
import { randInt, randParagraph, randText } from "./rand-fixture";
import { randStackIds } from "./stack-fixture";

export type FakeWriteRecruitmentRequestArgs = Partial<WriteRecruitmentRequest>;

export function fakeWriteRecruitmentRequest({
  title,
  stackIds,
  positionIds,
  applicantCount,
  recruitmentEndDateTime,
  content,
  contact,
  callUrl,
}: FakeWriteRecruitmentRequestArgs = {}): WriteRecruitmentRequest {
  return {
    applicantCount: applicantCount ?? randInt(4, 10),
    callUrl: callUrl ?? randUrl(),
    content: content ?? randParagraph(),
    positionIds: positionIds ?? randPositionIds(),
    recruitmentEndDateTime:
      recruitmentEndDateTime ?? addDays(localNow(), 10).toISOString(),
    stackIds: stackIds ?? randStackIds(),
    title: title ?? randText(),
    contact: contact ?? randOneOf("EMAIL", "KAKAO"),
  };
}
