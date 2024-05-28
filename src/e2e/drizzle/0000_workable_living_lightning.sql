-- Current sql file was generated after introspecting the database
-- If you want to run this migration please uncomment this code before executing migrations
/*
CREATE TABLE `category` (
	`category_id` bigint AUTO_INCREMENT NOT NULL,
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`updated_date_time` datetime(6) NOT NULL,
	`name` char(50) NOT NULL,
	CONSTRAINT `category_category_id` PRIMARY KEY(`category_id`)
);
--> statement-breakpoint
CREATE TABLE `position` (
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`position_id` bigint AUTO_INCREMENT NOT NULL,
	`updated_date_time` datetime(6) NOT NULL,
	`name` char(50) NOT NULL,
	CONSTRAINT `position_position_id` PRIMARY KEY(`position_id`)
);
--> statement-breakpoint
CREATE TABLE `recruitment` (
	`applicant_count` int NOT NULL,
	`hits` int NOT NULL,
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`modified_date_time` datetime(6) NOT NULL,
	`recruitment_end_date_time` datetime(6) NOT NULL,
	`recruitment_id` bigint AUTO_INCREMENT NOT NULL,
	`study_id` bigint NOT NULL,
	`updated_date_time` datetime(6) NOT NULL,
	`title` varchar(50) NOT NULL,
	`content` varchar(2000) NOT NULL,
	`call_url` varchar(2048) NOT NULL,
	`contact` char(20) NOT NULL,
	CONSTRAINT `recruitment_recruitment_id` PRIMARY KEY(`recruitment_id`),
	CONSTRAINT `UK_hslrcom0qs791euq0tvgphd44` UNIQUE(`study_id`)
);
--> statement-breakpoint
CREATE TABLE `recruitment_position_lnk` (
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`position_id` bigint NOT NULL,
	`recruitment_id` bigint NOT NULL,
	`updated_date_time` datetime(6) NOT NULL,
	CONSTRAINT `recruitment_position_lnk_position_id_recruitment_id` PRIMARY KEY(`position_id`,`recruitment_id`)
);
--> statement-breakpoint
CREATE TABLE `recruitment_stack_lnk` (
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`recruitment_id` bigint NOT NULL,
	`stack_id` bigint NOT NULL,
	`updated_date_time` datetime(6) NOT NULL,
	CONSTRAINT `recruitment_stack_lnk_recruitment_id_stack_id` PRIMARY KEY(`recruitment_id`,`stack_id`)
);
--> statement-breakpoint
CREATE TABLE `recruitment_user_lnk` (
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`position_id` bigint,
	`recruitment_id` bigint NOT NULL,
	`updated_date_time` datetime(6) NOT NULL,
	`user_id` bigint NOT NULL,
	`status` char(10) NOT NULL,
	CONSTRAINT `recruitment_user_lnk_recruitment_id_user_id` PRIMARY KEY(`recruitment_id`,`user_id`)
);
--> statement-breakpoint
CREATE TABLE `review` (
	`activeness_score` bigint,
	`communication_score` bigint,
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`id` bigint AUTO_INCREMENT NOT NULL,
	`professionalism_score` bigint,
	`recommend_score` bigint,
	`reviewee_id` bigint,
	`reviewer_id` bigint,
	`study_id` bigint,
	`together_score` bigint,
	`updated_date_time` datetime(6) NOT NULL,
	CONSTRAINT `review_id` PRIMARY KEY(`id`)
);
--> statement-breakpoint
CREATE TABLE `stack` (
	`company_count` int NOT NULL,
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`stack_category_id` bigint NOT NULL,
	`stack_id` bigint AUTO_INCREMENT NOT NULL,
	`updated_date_time` datetime(6) NOT NULL,
	`name` varchar(50) NOT NULL,
	`image_url` varchar(2048),
	CONSTRAINT `stack_stack_id` PRIMARY KEY(`stack_id`)
);
--> statement-breakpoint
CREATE TABLE `stack_category` (
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`stack_category_id` bigint AUTO_INCREMENT NOT NULL,
	`updated_date_time` datetime(6) NOT NULL,
	`name` varchar(50) NOT NULL,
	CONSTRAINT `stack_category_stack_category_id` PRIMARY KEY(`stack_category_id`)
);
--> statement-breakpoint
CREATE TABLE `study` (
	`participant_count` int NOT NULL,
	`participant_limit` int NOT NULL,
	`category_id` bigint NOT NULL,
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`end_date_time` datetime(6) NOT NULL,
	`owner_id` bigint NOT NULL,
	`start_date_time` datetime(6) NOT NULL,
	`study_id` bigint AUTO_INCREMENT NOT NULL,
	`updated_date_time` datetime(6) NOT NULL,
	`title` varchar(50) NOT NULL,
	`platform_url` varchar(2048),
	`platform` char(20) NOT NULL,
	`status` char(10) NOT NULL,
	`way` char(10) NOT NULL,
	CONSTRAINT `study_study_id` PRIMARY KEY(`study_id`)
);
--> statement-breakpoint
CREATE TABLE `study_user_lnk` (
	`attendance` int NOT NULL,
	`valid_attendance` int NOT NULL,
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`enrollment_date_time` datetime(6),
	`position_id` bigint,
	`study_id` bigint NOT NULL,
	`updated_date_time` datetime(6) NOT NULL,
	`user_id` bigint NOT NULL,
	`role` char(10) NOT NULL,
	CONSTRAINT `study_user_lnk_study_id_user_id` PRIMARY KEY(`study_id`,`user_id`)
);
--> statement-breakpoint
CREATE TABLE `user` (
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`updated_date_time` datetime(6) NOT NULL,
	`user_id` bigint AUTO_INCREMENT NOT NULL,
	`nickname` varchar(20),
	`password` varchar(100),
	`email` varchar(320) NOT NULL,
	`social` char(10) NOT NULL,
	CONSTRAINT `user_user_id` PRIMARY KEY(`user_id`)
);
--> statement-breakpoint
CREATE TABLE `user_review_statistics` (
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`id` bigint AUTO_INCREMENT NOT NULL,
	`total_activeness_review_count` bigint,
	`total_activeness_score` bigint,
	`total_communication_review_count` bigint,
	`total_communication_score` bigint,
	`total_professionalism_review_count` bigint,
	`total_professionalism_score` bigint,
	`total_recommend_review_score` bigint,
	`total_recommend_score` bigint,
	`total_together_review_score` bigint,
	`total_together_score` bigint,
	`updated_date_time` datetime(6) NOT NULL,
	`user_id` bigint,
	CONSTRAINT `user_review_statistics_id` PRIMARY KEY(`id`),
	CONSTRAINT `UK_d538aj7oswyp4kkgtnbju66g5` UNIQUE(`user_id`)
);
--> statement-breakpoint
CREATE TABLE `user_study_statistics` (
	`total_attendance` int NOT NULL,
	`total_completed_study_count` int NOT NULL,
	`total_finished_study_count` int NOT NULL,
	`total_left_study_count` int NOT NULL,
	`total_study_days` int NOT NULL,
	`total_teammate_count` int NOT NULL,
	`total_valid_attendance` int NOT NULL,
	`created_date_time` datetime(6) NOT NULL,
	`deleted_date_time` datetime(6),
	`id` bigint AUTO_INCREMENT NOT NULL,
	`updated_date_time` datetime(6) NOT NULL,
	`user_id` bigint,
	CONSTRAINT `user_study_statistics_id` PRIMARY KEY(`id`),
	CONSTRAINT `UK_3bndtg2a96bq2axf1iameidip` UNIQUE(`user_id`)
);
--> statement-breakpoint
ALTER TABLE `recruitment` ADD CONSTRAINT `FKcxmff8tt9nlmc2uhv8hxoc0md` FOREIGN KEY (`study_id`) REFERENCES `study`(`study_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `recruitment_position_lnk` ADD CONSTRAINT `FK32x6ojftvcfgonw36508ly4dd` FOREIGN KEY (`position_id`) REFERENCES `position`(`position_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `recruitment_position_lnk` ADD CONSTRAINT `FKmglyui6ms0jcn1cdacnhyguq4` FOREIGN KEY (`recruitment_id`) REFERENCES `recruitment`(`recruitment_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `recruitment_stack_lnk` ADD CONSTRAINT `FK9o9clc5grpqlj5jya4fegn52k` FOREIGN KEY (`recruitment_id`) REFERENCES `recruitment`(`recruitment_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `recruitment_stack_lnk` ADD CONSTRAINT `FKma8sj20332hutcndt3fk62qgi` FOREIGN KEY (`stack_id`) REFERENCES `stack`(`stack_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `recruitment_user_lnk` ADD CONSTRAINT `FK3flukj91gql7o32ufwhpqxew6` FOREIGN KEY (`recruitment_id`) REFERENCES `recruitment`(`recruitment_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `recruitment_user_lnk` ADD CONSTRAINT `FKax0jnfjn7etxjq2vw0d7fryat` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `recruitment_user_lnk` ADD CONSTRAINT `FKieljwlb47aeyyglg370k7rbcn` FOREIGN KEY (`position_id`) REFERENCES `position`(`position_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `review` ADD CONSTRAINT `FK1ekqsiyb8aq8h2fw93qb75n8r` FOREIGN KEY (`study_id`) REFERENCES `study`(`study_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `review` ADD CONSTRAINT `FK8kasut96v7v88hihmgsk14usl` FOREIGN KEY (`reviewee_id`) REFERENCES `user`(`user_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `review` ADD CONSTRAINT `FK8l40hgqc1woa2m6xjap0r30jp` FOREIGN KEY (`reviewer_id`) REFERENCES `user`(`user_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `stack` ADD CONSTRAINT `FKihgnjltw5c0ig5m4nof751u01` FOREIGN KEY (`stack_category_id`) REFERENCES `stack_category`(`stack_category_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `study` ADD CONSTRAINT `FK61rof6gu4refc35aae1g62eus` FOREIGN KEY (`category_id`) REFERENCES `category`(`category_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `study` ADD CONSTRAINT `FK87ukam6d27hunus4f8wvwrhq` FOREIGN KEY (`owner_id`) REFERENCES `user`(`user_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `study_user_lnk` ADD CONSTRAINT `FKab43vjkkv34ykhlxmvhte49gt` FOREIGN KEY (`study_id`) REFERENCES `study`(`study_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `study_user_lnk` ADD CONSTRAINT `FKdw4ojlrx2a9fjqc1cycmmn15d` FOREIGN KEY (`position_id`) REFERENCES `position`(`position_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `study_user_lnk` ADD CONSTRAINT `FKpk0qnup8m9yonk1hu81gkfkfi` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `user_review_statistics` ADD CONSTRAINT `FK1han0i2cxvs8x4bn609emjkdy` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE `user_study_statistics` ADD CONSTRAINT `FKeh8lqx1mf84molihxxew6mavs` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE no action ON UPDATE no action;
*/