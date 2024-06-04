import {
  bigint,
  char,
  datetime,
  int,
  mysqlTable,
  primaryKey,
  unique,
  varchar,
} from "drizzle-orm/mysql-core";

export const category = mysqlTable(
  "category",
  {
    categoryId: bigint("category_id", { mode: "number" })
      .autoincrement()
      .notNull(),
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    name: char("name", { length: 50 }).notNull(),
  },
  (table) => {
    return {
      categoryCategoryId: primaryKey({
        columns: [table.categoryId],
        name: "category_category_id",
      }),
    };
  }
);

export const position = mysqlTable(
  "position",
  {
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    positionId: bigint("position_id", { mode: "number" })
      .autoincrement()
      .notNull(),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    name: char("name", { length: 50 }).notNull(),
  },
  (table) => {
    return {
      positionPositionId: primaryKey({
        columns: [table.positionId],
        name: "position_position_id",
      }),
    };
  }
);

export const recruitment = mysqlTable(
  "recruitment",
  {
    applicantCount: int("applicant_count").notNull(),
    hits: int("hits").notNull(),
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    modifiedDateTime: datetime("modified_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    recruitmentEndDateTime: datetime("recruitment_end_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    recruitmentId: bigint("recruitment_id", { mode: "number" })
      .autoincrement()
      .notNull(),
    studyId: bigint("study_id", { mode: "number" })
      .notNull()
      .references(() => study.studyId),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    title: varchar("title", { length: 50 }).notNull(),
    content: varchar("content", { length: 2000 }).notNull(),
    callUrl: varchar("call_url", { length: 2048 }).notNull(),
    contact: char("contact", { length: 20 }).notNull(),
  },
  (table) => {
    return {
      recruitmentRecruitmentId: primaryKey({
        columns: [table.recruitmentId],
        name: "recruitment_recruitment_id",
      }),
      ukHslrcom0Qs791Euq0Tvgphd44: unique("UK_hslrcom0qs791euq0tvgphd44").on(
        table.studyId
      ),
    };
  }
);

export const recruitmentPositionLnk = mysqlTable(
  "recruitment_position_lnk",
  {
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    positionId: bigint("position_id", { mode: "number" })
      .notNull()
      .references(() => position.positionId),
    recruitmentId: bigint("recruitment_id", { mode: "number" })
      .notNull()
      .references(() => recruitment.recruitmentId),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
  },
  (table) => {
    return {
      recruitmentPositionLnkPositionIdRecruitmentId: primaryKey({
        columns: [table.positionId, table.recruitmentId],
        name: "recruitment_position_lnk_position_id_recruitment_id",
      }),
    };
  }
);

export const recruitmentStackLnk = mysqlTable(
  "recruitment_stack_lnk",
  {
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    recruitmentId: bigint("recruitment_id", { mode: "number" })
      .notNull()
      .references(() => recruitment.recruitmentId),
    stackId: bigint("stack_id", { mode: "number" })
      .notNull()
      .references(() => stack.stackId),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
  },
  (table) => {
    return {
      recruitmentStackLnkRecruitmentIdStackId: primaryKey({
        columns: [table.recruitmentId, table.stackId],
        name: "recruitment_stack_lnk_recruitment_id_stack_id",
      }),
    };
  }
);

export const recruitmentUserLnk = mysqlTable(
  "recruitment_user_lnk",
  {
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    positionId: bigint("position_id", { mode: "number" }).references(
      () => position.positionId
    ),
    recruitmentId: bigint("recruitment_id", { mode: "number" })
      .notNull()
      .references(() => recruitment.recruitmentId),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    userId: bigint("user_id", { mode: "number" })
      .notNull()
      .references(() => user.userId),
    status: char("status", { length: 10 }).notNull(),
  },
  (table) => {
    return {
      recruitmentUserLnkRecruitmentIdUserId: primaryKey({
        columns: [table.recruitmentId, table.userId],
        name: "recruitment_user_lnk_recruitment_id_user_id",
      }),
    };
  }
);

export const review = mysqlTable(
  "review",
  {
    activenessScore: bigint("activeness_score", { mode: "number" }),
    communicationScore: bigint("communication_score", { mode: "number" }),
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    id: bigint("id", { mode: "number" }).autoincrement().notNull(),
    professionalismScore: bigint("professionalism_score", { mode: "number" }),
    recommendScore: bigint("recommend_score", { mode: "number" }),
    revieweeId: bigint("reviewee_id", { mode: "number" }).references(
      () => user.userId
    ),
    reviewerId: bigint("reviewer_id", { mode: "number" }).references(
      () => user.userId
    ),
    studyId: bigint("study_id", { mode: "number" }).references(
      () => study.studyId
    ),
    togetherScore: bigint("together_score", { mode: "number" }),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
  },
  (table) => {
    return {
      reviewId: primaryKey({ columns: [table.id], name: "review_id" }),
    };
  }
);

export const stack = mysqlTable(
  "stack",
  {
    companyCount: int("company_count").notNull(),
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    stackCategoryId: bigint("stack_category_id", { mode: "number" })
      .notNull()
      .references(() => stackCategory.stackCategoryId),
    stackId: bigint("stack_id", { mode: "number" }).autoincrement().notNull(),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    name: varchar("name", { length: 50 }).notNull(),
    imageUrl: varchar("image_url", { length: 2048 }),
  },
  (table) => {
    return {
      stackStackId: primaryKey({
        columns: [table.stackId],
        name: "stack_stack_id",
      }),
    };
  }
);

export const stackCategory = mysqlTable(
  "stack_category",
  {
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    stackCategoryId: bigint("stack_category_id", { mode: "number" })
      .autoincrement()
      .notNull(),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    name: varchar("name", { length: 50 }).notNull(),
  },
  (table) => {
    return {
      stackCategoryStackCategoryId: primaryKey({
        columns: [table.stackCategoryId],
        name: "stack_category_stack_category_id",
      }),
    };
  }
);

export const study = mysqlTable(
  "study",
  {
    participantCount: int("participant_count").notNull(),
    participantLimit: int("participant_limit").notNull(),
    categoryId: bigint("category_id", { mode: "number" })
      .notNull()
      .references(() => category.categoryId),
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    endDateTime: datetime("end_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    ownerId: bigint("owner_id", { mode: "number" })
      .notNull()
      .references(() => user.userId),
    startDateTime: datetime("start_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    studyId: bigint("study_id", { mode: "number" }).autoincrement().notNull(),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    title: varchar("title", { length: 50 }).notNull(),
    platformUrl: varchar("platform_url", { length: 2048 }),
    platform: char("platform", { length: 20 }).notNull(),
    status: char("status", { length: 10 }).notNull(),
    way: char("way", { length: 10 }).notNull(),
  },
  (table) => {
    return {
      studyStudyId: primaryKey({
        columns: [table.studyId],
        name: "study_study_id",
      }),
    };
  }
);

export const studyUserLnk = mysqlTable(
  "study_user_lnk",
  {
    attendance: int("attendance").notNull(),
    validAttendance: int("valid_attendance").notNull(),
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    enrollmentDateTime: datetime("enrollment_date_time", {
      mode: "string",
      fsp: 6,
    }),
    positionId: bigint("position_id", { mode: "number" }).references(
      () => position.positionId
    ),
    studyId: bigint("study_id", { mode: "number" })
      .notNull()
      .references(() => study.studyId),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    userId: bigint("user_id", { mode: "number" })
      .notNull()
      .references(() => user.userId),
    role: char("role", { length: 10 }).notNull(),
  },
  (table) => {
    return {
      studyUserLnkStudyIdUserId: primaryKey({
        columns: [table.studyId, table.userId],
        name: "study_user_lnk_study_id_user_id",
      }),
    };
  }
);

export const user = mysqlTable(
  "user",
  {
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    userId: bigint("user_id", { mode: "number" }).autoincrement().notNull(),
    nickname: varchar("nickname", { length: 20 }),
    password: varchar("password", { length: 100 }),
    email: varchar("email", { length: 320 }).notNull(),
    social: char("social", { length: 10 }).notNull(),
  },
  (table) => {
    return {
      userUserId: primaryKey({ columns: [table.userId], name: "user_user_id" }),
    };
  }
);

export const userReviewStatistics = mysqlTable(
  "user_review_statistics",
  {
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    id: bigint("id", { mode: "number" }).autoincrement().notNull(),
    totalActivenessReviewCount: bigint("total_activeness_review_count", {
      mode: "number",
    }),
    totalActivenessScore: bigint("total_activeness_score", { mode: "number" }),
    totalCommunicationReviewCount: bigint("total_communication_review_count", {
      mode: "number",
    }),
    totalCommunicationScore: bigint("total_communication_score", {
      mode: "number",
    }),
    totalProfessionalismReviewCount: bigint(
      "total_professionalism_review_count",
      { mode: "number" }
    ),
    totalProfessionalismScore: bigint("total_professionalism_score", {
      mode: "number",
    }),
    totalRecommendReviewScore: bigint("total_recommend_review_score", {
      mode: "number",
    }),
    totalRecommendScore: bigint("total_recommend_score", { mode: "number" }),
    totalTogetherReviewScore: bigint("total_together_review_score", {
      mode: "number",
    }),
    totalTogetherScore: bigint("total_together_score", { mode: "number" }),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    userId: bigint("user_id", { mode: "number" }).references(() => user.userId),
  },
  (table) => {
    return {
      userReviewStatisticsId: primaryKey({
        columns: [table.id],
        name: "user_review_statistics_id",
      }),
      ukD538Aj7Oswyp4Kkgtnbju66G5: unique("UK_d538aj7oswyp4kkgtnbju66g5").on(
        table.userId
      ),
    };
  }
);

export const userStudyStatistics = mysqlTable(
  "user_study_statistics",
  {
    totalAttendance: int("total_attendance").notNull(),
    totalCompletedStudyCount: int("total_completed_study_count").notNull(),
    totalFinishedStudyCount: int("total_finished_study_count").notNull(),
    totalLeftStudyCount: int("total_left_study_count").notNull(),
    totalStudyDays: int("total_study_days").notNull(),
    totalTeammateCount: int("total_teammate_count").notNull(),
    totalValidAttendance: int("total_valid_attendance").notNull(),
    createdDateTime: datetime("created_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    deletedDateTime: datetime("deleted_date_time", { mode: "string", fsp: 6 }),
    id: bigint("id", { mode: "number" }).autoincrement().notNull(),
    updatedDateTime: datetime("updated_date_time", {
      mode: "string",
      fsp: 6,
    }).notNull(),
    userId: bigint("user_id", { mode: "number" }).references(() => user.userId),
  },
  (table) => {
    return {
      userStudyStatisticsId: primaryKey({
        columns: [table.id],
        name: "user_study_statistics_id",
      }),
      uk3Bndtg2A96Bq2Axf1Iameidip: unique("UK_3bndtg2a96bq2axf1iameidip").on(
        table.userId
      ),
    };
  }
);
