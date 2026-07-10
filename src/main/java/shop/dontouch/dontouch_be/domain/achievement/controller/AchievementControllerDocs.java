package shop.dontouch.dontouch_be.domain.achievement.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import shop.dontouch.dontouch_be.domain.achievement.dto.response.UserAchievementResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface AchievementControllerDocs {

  @Operation(
      summary = "내 뱃지 목록 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          ### 응답 데이터
          `List<UserAchievementResponse>` — 획득일(`earnedAt`) 최신순

          - `achievementId` (UUID)
          - `name` (String): 뱃지 이름
          - `description` (String): 뱃지 설명
          - `earnedAt` (LocalDateTime): 획득 일시

          ### 유의 사항
          - 뱃지 정의 생성 및 사용자에게 뱃지를 부여하는 기능은 이번 범위에 포함되지 않습니다 (추후 별도 이벤트 기반 작업 필요).

          ### 예외 처리
          - `INTERNAL_SERVER_ERROR` (500 INTERNAL_SERVER_ERROR): 서버에 문제가 발생했습니다.
          """
  )
  ResponseEntity<List<UserAchievementResponse>> getMyAchievements(
      @AuthenticationPrincipal CustomUserDetails currentUser
  );
}
