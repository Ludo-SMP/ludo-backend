import { addDays } from "date-fns/addDays";
import { localNow } from "../helpers/datetime-helper";
import { CreateStudyRequest, UpdateStudyRequest } from "../types/study-types";
import { randCategoryId } from "./category-fixture";
import { randOneOf, randPositionId, randUrl } from "./position-fixture";
import { randInt, randStr } from "./rand-fixture";

export type FakeCreateStudyRequestArgs = Partial<CreateStudyRequest>;
export type FakeUpdateStudyRequestArgs = FakeCreateStudyRequestArgs;

export function fakeCreateStudyRequest({
  categoryId,
  endDateTime,
  participantLimit,
  platform,
  platformUrl,
  positionId,
  startDateTime,
  title,
  way,
}: FakeCreateStudyRequestArgs = {}): CreateStudyRequest {
  const now = localNow();
  return {
    title: title ?? randStr(10, 20),
    categoryId: categoryId ?? randCategoryId(),
    positionId: positionId ?? randPositionId(),
    way: way ?? randOneOf("ONLINE", "OFFLINE"),
    platform: platform ?? randOneOf("GATHER", "GOOGLE_MEET"),
    platformUrl: platformUrl ?? randUrl(),
    participantLimit: participantLimit ?? randInt(4, 8),
    startDateTime: startDateTime ?? now,
    endDateTime: endDateTime ?? addDays(now, 10).toISOString(),
  };
}

export function fakeUpdateStudyRequest({
  categoryId,
  endDateTime,
  participantLimit,
  platform,
  platformUrl,
  positionId,
  startDateTime,
  title,
  way,
}: FakeUpdateStudyRequestArgs = {}): UpdateStudyRequest {
  const now = localNow();
  return {
    title: title ?? randStr(10, 20),
    categoryId: categoryId ?? randCategoryId(),
    positionId: positionId ?? randPositionId(),
    way: way ?? randOneOf("ONLINE", "OFFLINE"),
    platform: platform ?? randOneOf("GATHER", "GOOGLE_MEET"),
    platformUrl: platformUrl ?? randUrl(),
    participantLimit: participantLimit ?? randInt(4, 8),
    startDateTime: startDateTime ?? now,
    endDateTime: endDateTime ?? addDays(now, 10).toISOString(),
  };
}
