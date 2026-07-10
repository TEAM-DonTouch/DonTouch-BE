# TODO

Tracks ongoing and planned work. Updated at the end of each task cycle.

---

## 문제상황

커뮤니티(게시글/댓글/좋아요), 챌린지/뱃지, 마이페이지 설정 도메인이 엔티티만 있거나 아예 없는 상태였고 컨트롤러/서비스/레포지토리가 빈 스텁이었음.

---

## 목표

커뮤니티 CRUD+좋아요+댓글, 진행형 챌린지(신규 도메인), 완료형 뱃지 조회, 마이페이지 알림/보안 설정, 팔로우 기반 팔로잉 탭까지 백엔드 API와 FE(Flutter) 연동을 완료한다.

## 참고사항

- soft delete는 `BaseEntity.delete()` + `@SQLRestriction("deleted_at IS NULL")` 패턴(Category 참고)을 그대로 따름
- 좋아요/팔로우/챌린지 참여는 모두 토글 또는 멱등 구조: 재좋아요/재팔로우가 막히지 않도록 hard delete 사용, 챌린지 재참여는 200 no-op
- 페이지네이션은 이 코드베이스 최초 도입이라 `global/common/dto/PageResponse.java`로 감싸서 반환 (Spring `Page` 직접 노출 안 함)
- Lombok `boolean isXxx` 필드는 Jackson이 `"xxx"`로 직렬화하는 버그가 있어 `Boolean`(wrapper) 타입으로 전부 교체함 (`isLiked`, `isJoined` 등)
- 로컬 실행: `docker run postgres:16`, `docker run redis:7` + `application-local.yml`(gitignore됨, 직접 생성 필요) + `SPRING_PROFILES_ACTIVE=local`

---

## 진행된 사항

- PostLike, Challenge/UserChallenge, Follow, UserSettings 엔티티 신규 추가. Post에 `commentCount` 컬럼 추가(댓글 작성/삭제 시 자동 증감)
- 게시글 CRUD(정렬 latest/popular, 페이징, 팔로잉 필터), 좋아요 토글, 댓글 CRUD API 구현 및 Swagger 문서화
- 챌린지: 전체/내 챌린지 목록(진행률/참여여부 배치조회), 참여(멱등), ADMIN 챌린지 생성 API
- 뱃지: 내 뱃지 목록 조회 API (`GET /api/achievements/me`)
- 마이페이지 설정: `GET/PATCH /api/users/me/settings` (lazy 생성)
- 팔로우: 토글 API + 게시글 목록 `following=true` 필터
- 로컬 Postgres/Redis로 실제 부팅 후 전체 플로우(작성→좋아요→댓글→삭제→403/404, 챌린지 참여, 팔로우 필터, 설정 lazy 생성) curl로 검증 완료
- Flutter FE: community/challenge/achievement 모델+서비스 신규, UserService에 settings/follow 메서드 추가, community_screen/post_detail_screen(신규)/home_screen/my_page_screen을 실제 API 연동으로 전환. `flutter analyze` 통과

## 앞으로 해야할 작업

- **뱃지 자동 부여 / 챌린지 진행률 자동 증가 메커니즘 없음** — 의도적으로 이번 라운드에서 보류. 무엇이 진행률을 올리는지(예: Transaction 연동) 결정 필요. 결정되면 완료 시 자동 뱃지 부여 로직도 함께 구현
- FE 마이페이지의 "이번 달 소비 현황" 섹션은 여전히 하드코딩 (이번 작업 범위 밖, 기존 Transaction/Budget 연동 이슈)
- FE `user_model.dart`의 enum들이 백엔드 실제 enum과 이름이 다른 부분 존재 (예: UserJobType) — 이번 작업 이전부터 있던 불일치, 별도 확인 필요

---

## Notes

- Claude does not commit, push, or create PRs. All git operations are done by the developer.
