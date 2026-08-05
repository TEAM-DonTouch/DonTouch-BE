-- =====================================================================
-- 카테고리 이름 중복 방지
-- =====================================================================

-- 삭제되지 않은 전역 카테고리의 이름 중복 방지
create unique index uk_category_global_name_active
  on category (name)
  where user_id is null
      and deleted_at is null;

-- 동일 사용자의 삭제되지 않은 커스텀 카테고리 이름 중복 방지
create unique index uk_category_user_name_active
  on category (user_id, name)
  where user_id is not null
      and deleted_at is null;

-- =====================================================================
-- 거래 조회 성능 개선
-- =====================================================================

create index idx_transactions_user_type_date
  on transactions (user_id, type, transaction_date);