package shop.dontouch.dontouch_be.domain.achievement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import shop.dontouch.dontouch_be.domain.achievement.dto.response.UserAchievementResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface AchievementControllerDocs {

  @Operation(
      summary = "내 뱃지 목록 조회",
      description = """
          현재 로그인한 사용자가 획득한 뱃지 목록을 획득일 최신순으로 조회합니다.

          ### 요청 파라미터
          Header

          - `Authorization: Bearer {accessToken}` (required)

          요청 파라미터는 없습니다. 조회 대상은 토큰의 사용자로 자동 결정됩니다.

          ### 응답 데이터
          `200 OK` + `List<UserAchievementResponse>` (페이징 없이 전체 목록 반환)

          - `achievementId` (UUID): 뱃지 ID
          - `name` (String): 뱃지 이름
          - `description` (String): 뱃지 설명
          - `earnedAt` (LocalDateTime): 획득 일시

          응답 예시
          ```json
          [
            {
              "achievementId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
              "name": "첫 거래 등록",
              "description": "첫 번째 거래를 등록했습니다.",
              "earnedAt": "2026-07-28T10:00:00"
            }
          ]
          ```

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 별도의 요청값 없이 호출합니다.

          ### 유의 사항
          - 획득한 뱃지가 없으면 빈 배열 `[]`을 반환합니다. 404가 아닙니다.
          - 획득일(`earnedAt`) 내림차순으로 정렬되며, 정렬 기준은 변경할 수 없습니다.
          - 아직 획득하지 않은 뱃지는 목록에 포함되지 않습니다. 전체 뱃지 목록을 조회하는 API는 없습니다.
          - 뱃지 정의 생성과 사용자에게 뱃지를 부여하는 기능은 아직 구현되지 않았습니다.
            따라서 현재는 DB에 직접 데이터를 넣지 않는 한 항상 빈 배열이 반환됩니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 사용자입니다.
          """
  )
  ResponseEntity<List<UserAchievementResponse>> getMyAchievements(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser
  );
}
