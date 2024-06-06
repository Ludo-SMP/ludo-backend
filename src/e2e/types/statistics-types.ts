import { User } from "./users-types";

export type StudyStatistics = {
  id: number;
  user: User;
  totalTeammateCount: number;
  totalPerfectAttendanceStudies: number;
  totalRequiredAttendanceStudies: number;
  totalLeftStudyCount: number;
  totalAttendance: number;
  totalValidAttendance: number;
  totalStudyDays: number;
};

export type ReviewStatistics = {
  // 적극성
  activenessScore: number;
  // 전문성
  professionalismScore: number;
  // 의사소통
  communicationScore: number;
  // 다시함께
  togetherScore: number;
  // 추천
  recommendationScore: number;
};
