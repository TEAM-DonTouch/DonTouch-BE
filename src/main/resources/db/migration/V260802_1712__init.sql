-- =====================================================================
-- 초기 스키마
-- =====================================================================
-- 이 스크립트는 엔티티 정의로부터 Hibernate가 생성한 DDL을 기준으로 작성했다.
-- 이후 스키마 변경은 반드시 새 마이그레이션 파일로 추가하며,
-- 이미 적용된 파일은 절대 수정하지 않는다(체크섬 불일치로 기동 실패).
-- =====================================================================


-- =====================================================================
-- 1. 유저
-- =====================================================================
create table users (
    id                 uuid         not null,
    login_id           varchar(30)  not null,
    password           varchar(255) not null,
    email              varchar(255) not null,
    login_platform     varchar(20)  not null,
    provider_id        varchar(255),
    nickname           varchar(30)  not null,
    profile_image_url  varchar(500),
    role               varchar(255) not null,
    age                integer,
    gender             varchar(15),
    job_type           varchar(255) not null,
    region             varchar(255) not null,
    status             varchar(255) not null,
    created_at         timestamp(6) not null,
    updated_at         timestamp(6) not null,
    deleted_at         timestamp(6),
    primary key (id),
    constraint uk_users_login_id unique (login_id),
    constraint uk_users_email unique (email),
    constraint uk_users_nickname unique (nickname),
    constraint uk_users_login_platform_provider_id unique (login_platform, provider_id),
    constraint ck_users_login_platform check (login_platform in ('LOCAL', 'GOOGLE', 'KAKAO', 'APPLE')),
    constraint ck_users_role check (role in ('GENERAL_USER', 'ADMIN')),
    constraint ck_users_gender check (gender in ('MALE', 'FEMALE', 'NOT_SELECTED')),
    constraint ck_users_job_type check (job_type in
        ('EMPLOYEE', 'FREELANCER', 'SOLE_PROPRIETOR', 'CORPORATE_OFFICER', 'UNEMPLOYED', 'STUDENT', 'OTHER')),
    constraint ck_users_region check (region in
        ('SEOUL', 'BUSAN', 'DAEGU', 'INCHEON', 'GWANGJU', 'DAEJEON', 'ULSAN', 'SEJONG', 'GYEONGGI',
         'GANGWON', 'CHUNGBUK', 'CHUNGNAM', 'JEONBUK', 'JEONNAM', 'GYEONGBUK', 'GYEONGNAM', 'JEJU')),
    constraint ck_users_status check (status in ('ACTIVE', 'DORMANT', 'SUSPENDED', 'WITHDRAWN'))
);

create table user_settings (
    id                         uuid         not null,
    user_id                    uuid         not null,
    push_notification_enabled  boolean      not null,
    biometric_login_enabled    boolean      not null,
    created_at                 timestamp(6) not null,
    updated_at                 timestamp(6) not null,
    deleted_at                 timestamp(6),
    primary key (id),
    constraint uk_user_settings_user unique (user_id)
);

create table follows (
    id            uuid         not null,
    follower_id   uuid         not null,
    following_id  uuid         not null,
    created_at    timestamp(6) not null,
    updated_at    timestamp(6) not null,
    deleted_at    timestamp(6),
    primary key (id),
    constraint uk_follow_follower_following unique (follower_id, following_id)
);


-- =====================================================================
-- 2. 가계부 (카테고리 / 예산 / 거래)
-- =====================================================================
create table category (
    id          uuid         not null,
    user_id     uuid,
    name        varchar(10)  not null,
    created_at  timestamp(6) not null,
    updated_at  timestamp(6) not null,
    deleted_at  timestamp(6),
    primary key (id)
);

create table budget (
    id          uuid         not null,
    user_id     uuid         not null,
    period      varchar(255) not null,
    amount      bigint       not null,
    start_date  date,
    end_date    date,
    created_at  timestamp(6) not null,
    updated_at  timestamp(6) not null,
    deleted_at  timestamp(6),
    primary key (id),
    constraint uk_budget_user unique (user_id),
    constraint ck_budget_period check (period in ('WEEKLY', 'MONTHLY', 'YEARLY', 'CUSTOM'))
);

create table transactions (
    id                uuid         not null,
    user_id           uuid         not null,
    category_id       uuid         not null,
    type              varchar(255) not null,
    amount            bigint       not null,
    memo              varchar(30),
    transaction_date  timestamp(6) not null,
    created_at        timestamp(6) not null,
    updated_at        timestamp(6) not null,
    deleted_at        timestamp(6),
    primary key (id),
    constraint ck_transactions_type check (type in ('INCOME', 'EXPENSE'))
);


-- =====================================================================
-- 3. 커뮤니티 (게시글 / 댓글 / 좋아요)
-- =====================================================================
create table post (
    id             uuid         not null,
    user_id        uuid         not null,
    title          varchar(30)  not null,
    content        varchar(255) not null,
    view_count     integer      not null,
    like_count     integer      not null,
    comment_count  integer      not null,
    created_at     timestamp(6) not null,
    updated_at     timestamp(6) not null,
    deleted_at     timestamp(6),
    primary key (id)
);

create table comment (
    id          uuid         not null,
    user_id     uuid         not null,
    post_id     uuid         not null,
    content     varchar(100) not null,
    created_at  timestamp(6) not null,
    updated_at  timestamp(6) not null,
    deleted_at  timestamp(6),
    primary key (id)
);

create table post_likes (
    id          uuid         not null,
    user_id     uuid         not null,
    post_id     uuid         not null,
    created_at  timestamp(6) not null,
    updated_at  timestamp(6) not null,
    deleted_at  timestamp(6),
    primary key (id),
    constraint uk_post_like_user_post unique (user_id, post_id)
);


-- =====================================================================
-- 4. 업적
-- =====================================================================
create table achievement (
    id           uuid         not null,
    name         varchar(30)  not null,
    description  varchar(100) not null,
    created_at   timestamp(6) not null,
    updated_at   timestamp(6) not null,
    deleted_at   timestamp(6),
    primary key (id),
    constraint uk_achievement_name unique (name)
);

create table user_achievement (
    id              uuid         not null,
    user_id         uuid         not null,
    achievement_id  uuid         not null,
    created_at      timestamp(6) not null,
    updated_at      timestamp(6) not null,
    deleted_at      timestamp(6),
    primary key (id)
);


-- =====================================================================
-- 5. 구현 예정 도메인 (AI / 취업정보 / 공지)
-- =====================================================================
create table ai_alert (
    id          uuid         not null,
    user_id     uuid         not null,
    message     varchar(255) not null,
    created_at  timestamp(6) not null,
    updated_at  timestamp(6) not null,
    deleted_at  timestamp(6),
    primary key (id)
);

create table ai_image (
    id                      uuid         not null,
    transactions_image_url  varchar(500),
    created_at              timestamp(6) not null,
    updated_at              timestamp(6) not null,
    deleted_at              timestamp(6),
    primary key (id)
);

create table employment_info (
    id          uuid         not null,
    user_id     uuid         not null,
    hire_date   date         not null,
    salary      integer      not null,
    bonus       integer      not null,
    created_at  timestamp(6) not null,
    updated_at  timestamp(6) not null,
    deleted_at  timestamp(6),
    primary key (id),
    constraint uk_employment_info_user unique (user_id)
);

create table notice (
    id             uuid         not null,
    title          varchar(30)  not null,
    content        varchar(255) not null,
    view_count     integer      not null,
    notice_status  boolean      not null,
    created_at     timestamp(6) not null,
    updated_at     timestamp(6) not null,
    deleted_at     timestamp(6),
    primary key (id)
);


-- =====================================================================
-- 6. 외래키
-- =====================================================================
alter table user_settings
    add constraint fk_user_settings_user foreign key (user_id) references users;

alter table follows
    add constraint fk_follows_follower foreign key (follower_id) references users;
alter table follows
    add constraint fk_follows_following foreign key (following_id) references users;

alter table category
    add constraint fk_category_user foreign key (user_id) references users;

alter table budget
    add constraint fk_budget_user foreign key (user_id) references users;

alter table transactions
    add constraint fk_transactions_user foreign key (user_id) references users;
alter table transactions
    add constraint fk_transactions_category foreign key (category_id) references category;

alter table post
    add constraint fk_post_user foreign key (user_id) references users;

alter table comment
    add constraint fk_comment_user foreign key (user_id) references users;
alter table comment
    add constraint fk_comment_post foreign key (post_id) references post;

alter table post_likes
    add constraint fk_post_likes_user foreign key (user_id) references users;
alter table post_likes
    add constraint fk_post_likes_post foreign key (post_id) references post;

alter table user_achievement
    add constraint fk_user_achievement_user foreign key (user_id) references users;
alter table user_achievement
    add constraint fk_user_achievement_achievement foreign key (achievement_id) references achievement;

alter table ai_alert
    add constraint fk_ai_alert_user foreign key (user_id) references users;

alter table employment_info
    add constraint fk_employment_info_user foreign key (user_id) references users;
