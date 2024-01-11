# ERD 설계

1. 테이블 명 단수 사용 News, Newss
2. int, integer 자료형 차이
3. updatedDateTime을 모든 테이블에 적용하는 이유
   여러 대의 Slave DB가 있을 때, DB를 합쳐야 하는 경우가 있을텐데,
   `updatedDateTime` 이 없으면 어떤 Slave DB의 ROW가 최신 ROW인지 인지할 수 없어서
   각 DB의 로그를 다 뒤져서 업데이트 된 날짜를 찾아야 되는 수가 생길 수도 있음.
4. 네이밍 컨벤션

    - study_user_participants
    - study_user_applicants
    - study_participants
    - study_applicants

5. M:N을 풀기 위해 중간테이블이 들어갔을 때, 네이밍 전략 조사 및 컨벤션 합의 필요

```mermaid

%%{init: {'theme': 'neutral' } }%%
erDiagram
    owner_id
    study_user_id

    user ||--o{ study_applicant: places
    study_applicant }|--|| study: places
    user ||--o{ study_participant: places
    study_participant }|--|| study: places
    study ||--|{ study_stack: contains
    study_stack }|--|| stack: contains
    study ||--|{ study_category: contains
    study_category }|--|| category: contains
    study ||--|{ study_position: contains
    study_position }|--|| position: contains

    user {
        bigint user_id PK
        varchar platform "GOOGLE | NAVER | KAKAO"
        varchar name
        datetime created_date_time
        datetime updated_date_time
        datetime deleted_date_time "NULL"
    }

    study {
        bigint study_id PK
        bigint owner_id FK
        varchar status "PENDING | RECRUITING | PROGRESS | COMPLETED"
        varchar title
        varchar way "ONLINE | OFFLINE"
        datetime start_date_time
        datetime end_date_time
        datetime enrollment_end_date_time
        int enrollment_count "unsigned"
        varchar content
        datetime created_date_time
        datetime updated_date_time
        datetime deleted_date_time "NULL"
    }

    study_category {
        bigint study_id FK
        bigint category_id FK
        datetime created_date_time
        datetime updated_date_time
        datetime deleted_date_time "NULL"
    }

    study_participant {
        bigint study_id FK
        bigint user_id FK
        datetime created_date_time
        datetime updated_date_time
        datetime deleted_date_time "NULL"
    }

    study_applicant {
        bigint study_id FK
        bigint user_id FK
        varchar status "UNCHECKED | ACCEPT | REJECT | REMOVED"
        datetime created_date_time
        datetime updated_date_time
        datetime deleted_date_time "NULL"
    }

    category {
        bigint category_id PK
        varchar name
        datetime created_date_time
        datetime updated_date_time
        datetime deleted_date_time "NULL"
    }

    study_position {
        bitint study_id FK
        bitint position_id FK
        datetime created_date_time
        datetime updated_date_time
        datetime deleted_date_time "NULL"
    }

    position {
        bigint position_id PK
        varchar name
        datetime created_date_time
        datetime updated_date_time
        datetime deleted_date_time "NULL"
    }

    study_stack {
        bitint study_id FK
        bitint stack_id FK
        datetime created_date_time
        datetime updated_date_time
        datetime deleted_date_time "NULL"
    }

    stack {
        bigint stack_id PK
        bigint stack_category FK
        varchar title
        varchar image_url "NULL"
        datetime created_date_time
        datetime updated_date_time
        datetime deleted_date_time "NULL"
    }

```
