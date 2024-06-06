import { BaseDateTime } from "./base-types";
import { User } from "./users-types";

export type UpdateStudyRequest = CreateStudyRequest;
export interface CreateStudyRequest {
  title: string;
  categoryId: number;
  positionId: number;
  way: Way;
  platform: Platform;
  platformUrl: string;
  participantLimit: number;
  startDateTime: string;
  endDateTime: string;
}

export type Participant = User & { position: Position };
export type Applicant = User & { position: Position };

export type StudyStatus = "RECRUITING" | "RECRUITED" | "PROGRESS" | "COMPLETED";
export type Platform = "GATHER" | "GOOGLE_MEET";
export type Way = "ONLINE" | "OFFLINE";
export type Role = "OWNER" | "MEMBER";

export interface Study extends BaseDateTime {
  id: number;
  status: StudyStatus;
  hasRecruitment: boolean;
  title: string;
  platform: Platform;
  platformUrl: string;
  way: Way;
  participantLimit: number;
  participantCount: number;
  startDateTime: string;
  endDateTime: string;
  category: Category;
  owner: User;
  participants: ParticipantUser[];
}

export interface Category {
  id: number;
  name: string;
}

export interface ParticipantUser {
  id: number;
  nickname: string;
  email: string;
  role: Role;
  position: Position;
}

export interface Position {
  id: number;
  name: string;
}

export interface Stack {
  id: number;
  name: string;
  imageUrl: string;
}
