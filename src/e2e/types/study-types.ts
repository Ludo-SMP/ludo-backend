import { BaseDateTime } from "./base-types";
import { ReviewStatistics } from "./statistics-types";
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
  attendanceDay: Day[];
}

export enum Day {
  MONDAY = 1,
  TUESDAY = 2,
  WEDNESDAY = 3,
  THURSDAY = 4,
  FRIDAY = 5,
  SATURDAY = 6,
  SUNDAY = 7,
}
export const ALL_DAYS = [
  Day.MONDAY,
  Day.TUESDAY,
  Day.WEDNESDAY,
  Day.THURSDAY,
  Day.FRIDAY,
  Day.SATURDAY,
  Day.SUNDAY,
];

export type Participant = User & { position: Position };
export type Applicant = User & { position: Position };
export type ApplicantWithReviewStatistics = Applicant & { reviewStatistics: ReviewStatistics };

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
