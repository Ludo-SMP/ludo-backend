import { ApiClient } from "../config/api-client";
import {
  ApplyRecruitmentRequest,
  ApplyRecruitmentResponse,
  RecruitmentDetailsResponse,
  WriteRecruitmentRequest,
} from "../types/recruitment-types";

export async function findRecruitmentById(
  apiClient: ApiClient,
  recruitmentId: string
) {
  return apiClient.get<RecruitmentDetailsResponse>(
    `/recruitments/${recruitmentId}`
  );
}

export async function writeRecruitment(
  apiClient: ApiClient,
  studyId: number,
  {
    applicantCount,
    callUrl,
    content,
    positionIds,
    recruitmentEndDateTime,
    stackIds,
    title,
    contact,
  }: WriteRecruitmentRequest
) {
  return apiClient.post<RecruitmentDetailsResponse>(
    `/studies/${studyId}/recruitments`,
    {
      applicantCount,
      callUrl,
      content,
      positionIds,
      recruitmentEndDateTime,
      stackIds,
      title,
      contact,
    }
  );
}

export async function applyRecruitment(
  apiClient: ApiClient,
  studyId: number,
  recruitmentId: string,
  { positionId }: ApplyRecruitmentRequest
) {
  return apiClient.post<ApplyRecruitmentResponse>(
    `/studies/${studyId}/recruitments/${recruitmentId}/apply`,
    { positionId }
  );
}
