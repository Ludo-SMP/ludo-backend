# -- user table
# INSERT INTO user(created_date_time, deleted_date_time, updated_date_time, nickname, email, social)
# VALUES ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '아카', 'archa@google.com', 'GOOGLE'),
#        ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '휴', 'hugh@google.com', 'GOOGLE'),
#        ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '빽', 'back@google.com', 'GOOGLE'),
#        ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '현', 'hyun@google.com', 'GOOGLE'),
#        ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '타로', 'taro@google.com', 'GOOGLE'),
#        ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '포키', 'poki@google.com', 'GOOGLE'),
#        ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '뚜뚜', 'dodo@google.com', 'GOOGLE'),
#        ('2025-02-10 18:00:00.000000', null, '2025-02-10 18:00:00.000000', '루루', 'lulu@google.com', 'GOOGLE');
#
# -- position table
# INSERT INTO `position`(created_date_time, deleted_date_time, updated_date_time, name)
# VALUES ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '백엔드'),
#        ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '프론트엔드'),
#        ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '디자이너');
#
#
-- category table
# INSERT INTO category(created_date_time, deleted_date_time, updated_date_time, name)
# VALUES ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '프로젝트'),
#        ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '코딩테스트'),
#        ('2025-02-10 18:00:00.000000', NULL, '2025-02-10 18:00:00.000000', '모의면접');

# -- study table
# INSERT INTO study(title, owner_id, `status`, way,
#                   participant_limit, participant_count, category_id,
#                   start_date_time, end_date_time,
#                   created_date_time, updated_date_time, deleted_date_time)
# VALUES ('스터디1', 1, 'RECRUITING', 'ONLINE',
#         5, 1, 1,
#         '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
#         '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
#        ('스터디2', 3, 'RECRUITING', 'OFFLINE',
#         5, 1, 2,
#         '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
#         '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
#        ('스터디3', 2, 'RECRUITING', 'ONLINE',
#         5, 1, 3,
#         '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
#         '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
#        ('스터디4', 4, 'RECRUITED', 'ONLINE',
#         5, 1, 1,
#         '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
#         '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
#        ('스터디5', 5, 'PROGRESS', 'ONLINE',
#         5, 1, 1,
#         '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
#         '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
#        ('스터디6', 6, 'COMPLETED', 'ONLINE',
#         5, 1, 1,
#         '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
#         '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
#        ('스터디7', 7, 'PROGRESS', 'ONLINE',
#         5, 1, 1,
#         '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
#         '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL),
#        ('스터디8', 8, 'RECRUITING', 'ONLINE',
#         5, 1, 1,
#         '2025-03-01 18:00:00.000000', '2025-03-20 18:00:00.000000',
#         '2025-02-10 18:00:00.000000', '2025-02-10 18:00:00.000000', NULL);
#
# -- recruitment(모집공고) table
# INSERT INTO recruitment (hits, recruitment_limit, created_date_time, deleted_date_time, recruitment_end_date_time,
#                          study_id, updated_date_time, call_url, content, title)
# VALUES (100, 5, '2025-01-20 12:30:00.000000', NULL, '2025-02-10 18:00:00.000000', 1, '2025-01-20 14:45:00.000000',
#         'https://example.com/call1', 'Content 1', 'Recruitment Title 1')
#        (150, 8, '2025-01-21 10:15:00.000000', NULL, '2025-02-15 20:30:00.000000', 2, '2025-01-21 12:20:00.000000',
#         'https://example.com/call2', 'Content 2', 'Recruitment Title 2'),
#        (80, 3, '2025-01-22 08:00:00.000000', NULL, '2025-02-08 14:00:00.000000', 3, '2025-01-22 09:45:00.000000',
#         'https://example.com/call3', 'Content 3', 'Recruitment Title 3'),
#        (120, 6, '2025-01-23 15:20:00.000000', NULL, '2025-02-20 12:45:00.000000', 4, '2025-01-23 17:30:00.000000',
#         'https://example.com/call4', 'Content 4', 'Recruitment Title 4'),
#        (90, 4, '2025-01-24 11:40:00.000000', NULL, '2025-02-12 10:00:00.000000', 5, '2025-01-24 13:15:00.000000',
#         'https://example.com/call5', 'Content 5', 'Recruitment Title 5'),
#        (110, 7, '2025-01-25 09:10:00.000000', NULL, '2025-02-18 16:30:00.000000', 6, '2025-01-25 11:25:00.000000',
#         'https://example.com/call6', 'Content 6', 'Recruitment Title 6'),
#        (130, 5, '2025-01-26 14:00:00.000000', NULL, '2025-02-14 22:15:00.000000', 7, '2025-01-26 16:10:00.000000',
#         'https://example.com/call7', 'Content 7', 'Recruitment Title 7'),
#        (70, 3, '2025-01-27 16:45:00.000000', NULL, '2025-02-06 08:30:00.000000', 8, '2025-01-27 18:20:00.000000',
#         'https://example.com/call8', 'Content 8', 'Recruitment Title 8');
#
# INSERT INTO stack_category (name, created_date_time, updated_date_time)
# VALUES ('협업툴', now(), now());
# INSERT INTO stack_category (name, created_date_time, updated_date_time)
# VALUES ('데이터', now(), now());
# INSERT INTO stack_category (name, created_date_time, updated_date_time)
# VALUES ('모바일', now(), now());
# INSERT INTO stack_category (name, created_date_time, updated_date_time)
# VALUES ('프론트엔드', now(), now());
# INSERT INTO stack_category (name, created_date_time, updated_date_time)
# VALUES ('데브옵스', now(), now());
# INSERT INTO stack_category (name, created_date_time, updated_date_time)
# VALUES ('백엔드', now(), now());
# INSERT INTO stack_category (name, created_date_time, updated_date_time)
# VALUES ('테스팅툴', now(), now());
# INSERT INTO stack_category (name, created_date_time, updated_date_time)
# VALUES ('데이터베이스', now(), now());
# INSERT INTO stack_category (name, created_date_time, updated_date_time)
# VALUES ('언어', now(), now());
# # 디자인 추가 항목
# INSERT INTO stack_category (name, created_date_time, updated_date_time)
# VALUES ('디자인', now(), now());
#
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Agit', 1, 9, 'agit.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Airflow', 2, 77, 'airflow.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Alamofire', 3, 31, 'alamofire.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Appium', 7, 37, 'appium.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Asana', 1, 15, 'asana.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('AWS Athena', 2, 45, 'aws_athena.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('AWS Redshift', 2, 26, 'aws_redshift.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Bazel', 3, 2, 'bazel.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Bitrise', 3, 7, 'bitrise.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Clickhouse', 2, 2, 'clickhouse.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Confluence', 1, 128, 'confluence.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('C++', 9, 35, 'c++.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('C#', 9, 19, 'c#.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Cucumber', 7, 3, 'cucumber.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Cypress', 7, 27, 'cypress.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Dagger', 3, 22, 'dagger.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Discord', 1, 2, 'discord.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Docker', 5, 176, 'docker.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Dooray', 1, 3, 'dooray.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Druid', 2, 14, 'druid.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('ElasticSearch', 8, 134, 'elasticsearch.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Enzyme', 7, 5, 'enzyme.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('ExoPlayer', 3, 8, 'exoplayer.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Fastlane', 3, 38, 'fastlane.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Flask', 6, 36, 'flask.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Flink', 2, 10, 'flink.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Flow', 1, 0, 'flow.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Fluentd', 2, 30, 'fluentd.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Github', 5, 174, 'github.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Glide', 3, 19, 'glide.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Google BigQuery', 2, 46, 'google_bigquery.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Google Cloud Build', 5, 2, 'google_cloud_build.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Google Data Studio', 2, 22, 'google_data_studio.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Google Firebase', 3, 62, 'google_firebase.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Grafana', 2, 87, 'grafana.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Hadoop', 2, 56, 'hadoop.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Hazelcast', 2, 2, 'hazelcast.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('HBase', 2, 16, 'hbase.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Hive', 2, 32, 'hive.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Immer', 4, 5, 'immer.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Impala', 2, 5, 'impala.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Jandi', 1, 2, 'jandi.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Jasmine', 7, 4, 'jasmine.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Java', 9, 130, 'java.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Javascript', 9, 193, 'javascript.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Jenkins', 5, 106, 'jenkins.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Jira', 1, 149, 'jira.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Jotai', 4, 7, 'jotai.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('JUnit', 7, 52, 'junit.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Kafka', 2, 97, 'kafka.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Kakaotalk', 1, 6, 'kakaotalk.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('KakaoWork', 1, 3, 'kakaowork.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Karma', 7, 5, 'karma.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Keras', 2, 11, 'keras.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Kibana', 2, 113, 'kibana.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Kotest', 7, 3, 'kotest.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Kubeflow', 2, 10, 'kubeflow.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Kubernetes', 5, 128, 'kubernetes.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Kudu', 2, 4, 'kudu.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Locust', 7, 5, 'locust.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Looker', 2, 3, 'looker.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Lottie', 3, 18, 'lottie.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Metabase', 2, 4, 'metabase.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Meteor', 4, 0, 'meteor.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Microsoft-Teams', 1, 7, 'microsoft_teams.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('MobX', 4, 23, 'mobx.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Mocha', 7, 21, 'mocha.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Mockito', 7, 18, 'mockito.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Monday', 1, 5, 'monday.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('MongoDB', 8, 98, 'mongodb.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Moya', 3, 9, 'moya.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('MSSQL', 8, 12, 'mssql.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('MySQL', 8, 162, 'mysql.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Naver Works', 1, 7, 'naver_works.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('NestJS', 6, 67, 'nestjs.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('NextJS', 4, 112, 'nextjs.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('nGrinder', 7, 11, 'ngrinder.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('NIFI', 2, 3, 'nifi.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Notion', 1, 118, 'notion.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('NuxtJS', 4, 23, 'nuxtjs.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('OpenGL', 4, 9, 'opengl.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('OracleDB', 8, 22, 'oracledb.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Playwright', 7, 13, 'playwright.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('PostgreSQL', 8, 76, 'postgresql.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Presto', 2, 24, 'presto.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Prometheus', 2, 67, 'prometheus.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Puppeteer', 7, 10, 'puppeteer.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Pytorch', 2, 66, 'pytorch.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Ranger', 2, 1, 'ranger.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Ray', 2, 7, 'ray.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('React Context', 4, 8, 'react_context.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('ReactiveX', 4, 70, 'reactivex.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('ReactJS', 4, 185, 'reactjs.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('React Native', 3, 48, 'react_native.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('ReactorKit', 3, 13, 'reactorkit.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('React Query', 4, 42, 'react_query.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Recoil', 4, 33, 'recoil.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Redash', 2, 23, 'redash.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Redis', 8, 168, 'redis.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Redux', 4, 105, 'redux.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Relay', 4, 3, 'relay.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('ReScript', 9, 1, 'rescript.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Rest-Assured', 7, 5, 'rest_assured.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Retrofit', 3, 32, 'retrofit.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('RIBs', 3, 5, 'ribs.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Ruby', 9, 16, 'ruby.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Rust', 9, 9, 'rust.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Selenium', 7, 48, 'selenium.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Sinon', 7, 3, 'sinon.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Slack', 1, 182, 'slack.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('SnapKit', 3, 26, 'snapkit.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Snowflake', 2, 3, 'snowflake.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Sonarqube', 7, 17, 'sonarqube.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Spark', 2, 67, 'spark.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Spring', 6, 87, 'spring.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('SpringBoot', 6, 97, 'springboot.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Storybook', 4, 72, 'storybook.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Styled-Components', 4, 162, 'styled_components.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Superset', 2, 7, 'superset.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Svelte', 4, 5, 'svelte.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Swagger', 6, 73, 'swagger.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Swift', 9, 129, 'swift.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Tableau', 2, 58, 'tableau.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Tailwind', 4, 10, 'tailwind.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Telegram', 1, 2, 'telegram.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Tensorflow', 2, 69, 'tensorflow.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Testing Library', 7, 24, 'testing_library.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Trello', 1, 11, 'trello.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Trino', 2, 5, 'trino.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Tuist', 3, 10, 'tuist.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Unity', 4, 10, 'unity.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('VueJS', 4, 77, 'vuejs.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Vuex', 4, 37, 'vuex.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Zeppelin', 2, 21, 'zeppelin.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Zipkin', 2, 6, 'zipkin.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Zustand', 4, 11, 'zustand.png', now(), now());
#
# # 디자인 추가 항목 // company_count 임의 작성
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Figma', 10, 11, 'figma.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Photoshop', 10, 11, 'photoshop.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('illustrator', 10, 11, 'illustrator.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('After Effects', 10, 11, 'after_effects.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Premiere Pro', 10, 11, 'premiere_pro.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('XD', 10, 11, 'xd.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('Indesign', 10, 11, 'indesign.png', now(), now());
# INSERT INTO stack (name, stack_category_id, company_count, image_url, created_date_time, updated_date_time)
# VALUES ('ProtoPie', 10, 11, 'proto_pie.png', now(), now());
