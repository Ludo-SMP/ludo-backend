import { BaseDateTime } from "../types/base-types";
import { Category, Position, Stack } from "../types/study-types";
import { User } from "../types/users-types";

type Contact = "EMAIL" | "KAKAO";

export interface ApplyRecruitmentResponse {
  applicantId: string;
}

export interface ApplyRecruitmentRequest {
  positionId: number;
}

export interface WriteRecruitmentRequest {
  title: string;
  stackIds: number[];
  positionIds: number[];
  applicantLimit: number;
  content: string;
  contact?: Contact;
  recruitmentEndDateTime: string;
  callUrl: string;
}

export interface RecruitmentDetailsResponse {
  recruitment: RecruitmentDetail;
  study: StudyDetail;
}

export interface RecruitmentDetail extends BaseDateTime {
  id: string;
  applicantLimit: number;
  positions: Position[];
  stacks: Stack[];
  contact: Contact;
  callUrl: string;
  title: string;
  content: string;
  endDateTime: string;
}

export interface StudyDetail {
  id: number;
  title: string;
  owner: User;
  platform: string;
  way: string;
  participantLimit: number;
  category: Category;
}
