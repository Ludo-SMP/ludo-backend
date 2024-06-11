import { User } from "./users-types";

export type StudyStatistics = {
  id: number;
  user: User;
  totalTeammateCount: number;
  totalFinishAttendanceStudies: number;
  totalPerfectAttendanceStudies: number;
  totalLeftStudyCount: number;
  totalAttendance: number;
  totalValidAttendance: number;
  totalStudyDays: number;
};

export type ReviewStatistics = {
  // 적극성
  activeness: number;
  // 전문성
  professionalism: number;
  // 의사소통
  communication: number;
  // 다시함께
  together: number;
  // 추천
  recommend: number;
};
