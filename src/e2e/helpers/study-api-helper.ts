import { ApiClient } from "../config/api-client";
import {
  Applicant,
  CreateStudyRequest,
  Participant,
  Study,
  UpdateStudyRequest,
} from "../types/study-types";

///
export type CreateStudyResponse = {
  study: Study;
};

export async function createStudy(
  apiClient: ApiClient,
  body: CreateStudyRequest
) {
  return apiClient.post<CreateStudyResponse>("/studies", body);
}

///
export type UpdateStudyResponse = CreateStudyResponse;

export async function updateStudy(
  apiClient: ApiClient,
  studyId: number,
  body: UpdateStudyRequest
) {
  return apiClient.put<UpdateStudyResponse>(`/studies/${studyId}`, body);
}

///

export type ParticipantResponse = {
  participant: Participant;
};

export async function acceptApplicant(
  apiClient: ApiClient,
  studyId: number,
  applicantUserId: number
) {
  return apiClient.post<ParticipantResponse>(
    `/studies/${studyId}/apply-accept/${applicantUserId}`
  );
}

export async function refuseApplicant(
  apiClient: ApiClient,
  studyId: number,
  applicantUserId: number
) {
  return apiClient.post<undefined>(
    `/studies/${studyId}/apply-refuse/${applicantUserId}`
  );
}

///

export type StudyResponse = { study: Study };

export async function findStudyDetailById(
  apiClient: ApiClient,
  studyId: number
) {
  return apiClient.get<StudyResponse>(`/studies/${studyId}`);
}

///

export type ApplicantStudyResponse = Pick<
  Study,
  "id" | "owner" | "status" | "participantLimit" | "participantCount"
> & { applicants: Applicant[] };
//  {
//   id: number;
//   owner: User;
//   status: StudyStatus;
//   title: string;
//   participantLimit: number;
//   participantCount: number;
//   applicants: (User & { position: Position })[];
// }
export async function findApplicantsByStudyId(
  apiClient: ApiClient,
  studyId: number
) {
  return apiClient.post<ApplicantStudyResponse>(
    `/studies/${studyId}/applicants`
  );
}

///

