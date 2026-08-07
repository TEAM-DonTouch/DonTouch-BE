-- 전역 카테고리를 시드한다.
-- user_id IS NULL 이면 전역 카테고리이며, 모든 유저에게 노출된다.
-- 기존에는 전역 카테고리가 한 건도 없어 신규 유저가 커스텀 카테고리를 먼저 만들지 않으면
-- 거래를 생성할 수 없었다.
--
-- uk_category_global_name_active (name) WHERE user_id IS NULL AND deleted_at IS NULL
-- 부분 유니크 인덱스가 걸려 있으므로 이름은 서로 겹치지 않게 유지한다.
-- 스키마 변경이 없으므로 엔티티 수정 없이 단독으로 적용해도 안전하다.

INSERT INTO category (id, user_id, name, created_at, updated_at) VALUES
  (gen_random_uuid(), NULL, '식비',        now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '카페/간식',   now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '편의점/마트', now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '교통',        now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '주거/통신',   now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '의료/건강',   now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '쇼핑',        now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '문화/여가',   now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '교육',        now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '급여',        now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '용돈',        now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '부수입',      now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '금융',        now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul'),
  (gen_random_uuid(), NULL, '기타',        now() AT TIME ZONE 'Asia/Seoul', now() AT TIME ZONE 'Asia/Seoul');
