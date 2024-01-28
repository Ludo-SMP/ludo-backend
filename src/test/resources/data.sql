-- user table
INSERT INTO user(created_date_time, deleted_date_time, updated_date_time, nickname, email, social)
VALUES ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '아카', 'archa@google.com', 'GOOGLE'),
       ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '휴', 'hugh@google.com', 'GOOGLE'),
       ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '빽', 'back@google.com', 'GOOGLE'),
       ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '현', 'hyun@google.com', 'GOOGLE'),
       ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '타로', 'taro@google.com', 'GOOGLE'),
       ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '포키', 'poki@google.com', 'GOOGLE'),
       ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '뚜뚜', 'dodo@google.com', 'GOOGLE'),
       ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '루루', 'lulu@google.com', 'GOOGLE');


-- position table
INSERT INTO `position`(created_date_time, deleted_date_time, updated_date_time, name)
VALUES ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '백엔드'),
       ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '프론트엔드'),
       ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '디자이너');


-- category table
INSERT INTO category(created_date_time, deleted_date_time, updated_date_time, name)
VALUES ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '프로젝트'),
       ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '코딩테스트'),
       ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '모의면접');


-- study table
INSERT INTO study(title, owner_id, `status`, way,
                  participant_limit, category_id,
                  start_date_time, end_date_time,
                  created_date_time, updated_date_time, deleted_date_time)
VALUES ('스터디1', 1, 'RECRUITING', 'ONLINE',
        5, 1,
        '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
        '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
       ('스터디2', 3, 'RECRUITING', 'OFFLINE',
        5, 2,
        '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
        '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
       ('스터디3', 2, 'RECRUITING', 'ONLINE',
        5, 3,
        '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
        '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
       ('스터디4', 4, 'RECRUITED', 'ONLINE',
        5, 1,
        '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
        '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
       ('스터디5', 5, 'PROGRESS', 'ONLINE',
        5, 1,
        '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
        '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
       ('스터디6', 6, 'COMPLETED', 'ONLINE',
        5, 1,
        '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
        '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
       ('스터디7', 7, 'PROGRESS', 'ONLINE',
        5, 1,
        '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
        '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
       ('스터디8', 8, 'RECRUITING', 'ONLINE',
        5, 1,
        '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
        '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL);


-- recruitment(모집공고) table
INSERT INTO recruitment (hits, recruitment_limit, created_date_time, deleted_date_time, recruitment_end_date_time,
                         study_id, updated_date_time, call_url, content, title)
VALUES (100, 5, '2025-01-20 12:30:00.000000', NULL, '2025-02-10 18:00:00.000000', 1, '2025-01-20 14:45:00.000000',
        'https://example.com/call1', 'Content 1', 'Recruitment Title 1'),
       (150, 8, '2025-01-21 10:15:00.000000', NULL, '2025-02-15 20:30:00.000000', 2, '2025-01-21 12:20:00.000000',
        'https://example.com/call2', 'Content 2', 'Recruitment Title 2'),
       (80, 3, '2025-01-22 08:00:00.000000', NULL, '2025-02-08 14:00:00.000000', 3, '2025-01-22 09:45:00.000000',
        'https://example.com/call3', 'Content 3', 'Recruitment Title 3'),
       (120, 6, '2025-01-23 15:20:00.000000', NULL, '2025-02-20 12:45:00.000000', 4, '2025-01-23 17:30:00.000000',
        'https://example.com/call4', 'Content 4', 'Recruitment Title 4'),
       (90, 4, '2025-01-24 11:40:00.000000', NULL, '2025-02-12 10:00:00.000000', 5, '2025-01-24 13:15:00.000000',
        'https://example.com/call5', 'Content 5', 'Recruitment Title 5'),
       (110, 7, '2025-01-25 09:10:00.000000', NULL, '2025-02-18 16:30:00.000000', 6, '2025-01-25 11:25:00.000000',
        'https://example.com/call6', 'Content 6', 'Recruitment Title 6'),
       (130, 5, '2025-01-26 14:00:00.000000', NULL, '2025-02-14 22:15:00.000000', 7, '2025-01-26 16:10:00.000000',
        'https://example.com/call7', 'Content 7', 'Recruitment Title 7'),
       (70, 3, '2025-01-27 16:45:00.000000', NULL, '2025-02-06 08:30:00.000000', 8, '2025-01-27 18:20:00.000000',
        'https://example.com/call8', 'Content 8', 'Recruitment Title 8');


-- recruitment_position_lnk(모집공고에 등록된 포지션) 테이블
INSERT INTO recruitment_position_lnk(recruitment_id, position_id,
                                     created_date_time, updated_date_time, deleted_date_time)
VALUES (1, 1,
        '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', NULL),
       (1, 2,
        '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', NULL),
       (1, 3,
        '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', NULL);


-- stack category table
INSERT INTO stack_category(name, created_date_time, updated_date_time, deleted_date_time)
VALUES ('백엔드', '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000'),
       ('프론트엔', '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000');


-- stack table
INSERT INTO stack(name, stack_category_id, created_date_time, updated_date_time, deleted_date_time)
VALUES ('spring', 1, '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000'),
       ('react', 2, '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000');


-- recruitment_stack_lnk (모집공고에 등록된 기술스택) table
INSERT INTO recruitment_stack_lnk(recruitment_id, stack_id,
                                  created_date_time, updated_date_time, deleted_date_time)
VALUES (1, 1, '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', NULL),
       (2, 1, '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', NULL),
       (1, 2, '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', NULL);


-- applicant table
INSERT INTO applicant(recruitment_id, user_id, `status`,
                      created_date_time, updated_date_time, deleted_date_time)
VALUES (1, 2, "UNCHECKED",
        '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000'),
       (1, 3, "ACCEPTED",
        '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000'),
       (1, 4, "CANCELLED",
        '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000', '2025-02-06 08:30:00.000000');