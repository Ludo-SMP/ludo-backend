import { relations } from "drizzle-orm/relations";
import { study, recruitment, position, recruitmentPositionLnk, recruitmentStackLnk, stack, recruitmentUserLnk, user, review, stackCategory, category, studyUserLnk, userReviewStatistics, userStudyStatistics } from "./schema";

export const recruitmentRelations = relations(recruitment, ({one, many}) => ({
	study: one(study, {
		fields: [recruitment.studyId],
		references: [study.studyId]
	}),
	recruitmentPositionLnks: many(recruitmentPositionLnk),
	recruitmentStackLnks: many(recruitmentStackLnk),
	recruitmentUserLnks: many(recruitmentUserLnk),
}));

export const studyRelations = relations(study, ({one, many}) => ({
	recruitments: many(recruitment),
	reviews: many(review),
	category: one(category, {
		fields: [study.categoryId],
		references: [category.categoryId]
	}),
	user: one(user, {
		fields: [study.ownerId],
		references: [user.userId]
	}),
	studyUserLnks: many(studyUserLnk),
}));

export const recruitmentPositionLnkRelations = relations(recruitmentPositionLnk, ({one}) => ({
	position: one(position, {
		fields: [recruitmentPositionLnk.positionId],
		references: [position.positionId]
	}),
	recruitment: one(recruitment, {
		fields: [recruitmentPositionLnk.recruitmentId],
		references: [recruitment.recruitmentId]
	}),
}));

export const positionRelations = relations(position, ({many}) => ({
	recruitmentPositionLnks: many(recruitmentPositionLnk),
	recruitmentUserLnks: many(recruitmentUserLnk),
	studyUserLnks: many(studyUserLnk),
}));

export const recruitmentStackLnkRelations = relations(recruitmentStackLnk, ({one}) => ({
	recruitment: one(recruitment, {
		fields: [recruitmentStackLnk.recruitmentId],
		references: [recruitment.recruitmentId]
	}),
	stack: one(stack, {
		fields: [recruitmentStackLnk.stackId],
		references: [stack.stackId]
	}),
}));

export const stackRelations = relations(stack, ({one, many}) => ({
	recruitmentStackLnks: many(recruitmentStackLnk),
	stackCategory: one(stackCategory, {
		fields: [stack.stackCategoryId],
		references: [stackCategory.stackCategoryId]
	}),
}));

export const recruitmentUserLnkRelations = relations(recruitmentUserLnk, ({one}) => ({
	recruitment: one(recruitment, {
		fields: [recruitmentUserLnk.recruitmentId],
		references: [recruitment.recruitmentId]
	}),
	user: one(user, {
		fields: [recruitmentUserLnk.userId],
		references: [user.userId]
	}),
	position: one(position, {
		fields: [recruitmentUserLnk.positionId],
		references: [position.positionId]
	}),
}));

export const userRelations = relations(user, ({many}) => ({
	recruitmentUserLnks: many(recruitmentUserLnk),
	reviews_revieweeId: many(review, {
		relationName: "review_revieweeId_user_userId"
	}),
	reviews_reviewerId: many(review, {
		relationName: "review_reviewerId_user_userId"
	}),
	studies: many(study),
	studyUserLnks: many(studyUserLnk),
	userReviewStatistics: many(userReviewStatistics),
	userStudyStatistics: many(userStudyStatistics),
}));

export const reviewRelations = relations(review, ({one}) => ({
	study: one(study, {
		fields: [review.studyId],
		references: [study.studyId]
	}),
	user_revieweeId: one(user, {
		fields: [review.revieweeId],
		references: [user.userId],
		relationName: "review_revieweeId_user_userId"
	}),
	user_reviewerId: one(user, {
		fields: [review.reviewerId],
		references: [user.userId],
		relationName: "review_reviewerId_user_userId"
	}),
}));

export const stackCategoryRelations = relations(stackCategory, ({many}) => ({
	stacks: many(stack),
}));

export const categoryRelations = relations(category, ({many}) => ({
	studies: many(study),
}));

export const studyUserLnkRelations = relations(studyUserLnk, ({one}) => ({
	study: one(study, {
		fields: [studyUserLnk.studyId],
		references: [study.studyId]
	}),
	position: one(position, {
		fields: [studyUserLnk.positionId],
		references: [position.positionId]
	}),
	user: one(user, {
		fields: [studyUserLnk.userId],
		references: [user.userId]
	}),
}));

export const userReviewStatisticsRelations = relations(userReviewStatistics, ({one}) => ({
	user: one(user, {
		fields: [userReviewStatistics.userId],
		references: [user.userId]
	}),
}));

export const userStudyStatisticsRelations = relations(userStudyStatistics, ({one}) => ({
	user: one(user, {
		fields: [userStudyStatistics.userId],
		references: [user.userId]
	}),
}));