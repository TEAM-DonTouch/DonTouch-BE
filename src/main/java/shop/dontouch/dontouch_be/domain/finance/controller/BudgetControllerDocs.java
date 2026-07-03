package shop.dontouch.dontouch_be.domain.finance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.finance.dto.request.BudgetRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.BudgetResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface BudgetControllerDocs {

  // ==================== 본인 전용 ====================

  @Operation(
      summary = "내 예산 저장 (생성 / 수정)",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          Request Body(JSON)

          - `period` (BudgetPeriod, required): 예산 기간 유형
            - `WEEKLY`: 주간
            - `MONTHLY`: 월간
            - `YEARLY`: 연간
            - `CUSTOM`: 직접 설정 (startDate, endDate 필수)
          - `amount` (Long, required): 예산 금액 (1 이상)
          - `startDate` (LocalDate, optional): 시작일 (CUSTOM 시 필수)
          - `endDate` (LocalDate, optional): 종료일 (CUSTOM 시 필수)

          요청 예시 (MONTHLY)
          ```json
          {
            "period": "MONTHLY",
            "amount": 500000
          }
          ```

          요청 예시 (CUSTOM)
          ```json
          {
            "period": "CUSTOM",
            "amount": 300000,
            "startDate": "2026-05-01",
            "endDate": "2026-05-31"
          }
          ```

          ### 응답 데이터
          - `budgetId` (UUID): 예산 ID
          - `userId` (UUID): 유저 ID
          - `period` (BudgetPeriod): 예산 기간 유형
          - `amount` (Long): 예산 금액
          - `startDate` (LocalDate): 시작일 (CUSTOM인 경우)
          - `endDate` (LocalDate): 종료일 (CUSTOM인 경우)
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 인증된 본인 기준으로 기간 유형, 금액을 입력합니다.
          3. CUSTOM 기간일 경우 startDate, endDate를 함께 입력합니다.
          4. 본인의 예산이 없으면 새로 생성, 이미 있으면 기존 예산을 수정합니다.

          ### 유의 사항
          - 탈퇴(WITHDRAWN) 상태인 유저는 예산을 생성/수정할 수 없습니다.
          - `period`가 `CUSTOM`일 경우 `startDate`와 `endDate`는 필수입니다.
          - `period`가 `CUSTOM`이 아닌 경우 `startDate`, `endDate`는 무시되며 null로 저장됩니다.
          - 유저당 예산은 하나만 존재합니다 (Upsert 방식).

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          - `USER_ALREADY_WITHDRAWN` (409 CONFLICT): 이미 탈퇴한 사용자입니다.
          - `BUDGET_PERIOD_DATE_REQUIRED` (400 BAD_REQUEST): CUSTOM 기간은 시작일과 종료일을 모두 입력해야 합니다.
          - `BUDGET_PERIOD_DATE_INVALID` (400 BAD_REQUEST): 시작일은 종료일보다 늦을 수 없습니다.
          """
  )
  ResponseEntity<BudgetResponse> saveBudget(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody BudgetRequest request
  );

  @Operation(
      summary = "내 예산 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          ### 응답 데이터
          - `budgetId` (UUID): 예산 ID
          - `userId` (UUID): 유저 ID
          - `period` (BudgetPeriod): 예산 기간 유형
          - `amount` (Long): 예산 금액
          - `startDate` (LocalDate): 시작일 (CUSTOM인 경우)
          - `endDate` (LocalDate): 종료일 (CUSTOM인 경우)
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 별도의 요청값 없이 호출합니다.
          3. 인증된 본인의 예산 정보를 반환합니다.

          ### 유의 사항
          - 본인의 예산이 없을 경우 예외가 발생합니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          - `BUDGET_NOT_FOUND` (404 NOT_FOUND): 예산을 찾을 수 없습니다.
          """
  )
  ResponseEntity<BudgetResponse> getMyBudget(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser
  );

  @Operation(
      summary = "내 예산 삭제",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          Path Variable

          - `budget-id` (UUID, required): 삭제할 예산 ID

          요청 예시
          ```text
          /api/budgets/me/cccccccc-cccc-cccc-cccc-cccccccccccc
          ```

          ### 응답 데이터
          없음 (204 No Content)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 삭제할 예산 ID를 Path Variable로 전달합니다.
          3. 요청 성공 시 응답 본문 없이 204 상태 코드가 반환됩니다.

          ### 유의 사항
          - 예산 데이터가 DB에서 완전히 삭제됩니다 (Hard Delete).
          - 존재하지 않는 예산 ID로 요청 시 예외가 발생합니다.
          - 본인 소유의 예산이 아닌 경우 삭제할 수 없습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `BUDGET_NOT_FOUND` (404 NOT_FOUND): 예산을 찾을 수 없습니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): 본인 소유의 예산이 아닙니다.
          """
  )
  ResponseEntity<Void> deleteBudget(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "budget-id") UUID budgetId
  );

  // ==================== ADMIN 전용 ====================

  @Operation(
      summary = "[ADMIN] 전체 예산 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          ### 응답 데이터
          예산 목록(List<BudgetResponse>)

          각 예산 데이터는 아래 정보를 포함합니다.

          - `budgetId` (UUID): 예산 ID
          - `userId` (UUID): 유저 ID
          - `period` (BudgetPeriod): 예산 기간 유형
          - `amount` (Long): 예산 금액
          - `startDate` (LocalDate): 시작일 (CUSTOM인 경우)
          - `endDate` (LocalDate): 종료일 (CUSTOM인 경우)
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 ADMIN 계정의 `Bearer {accessToken}`을 입력합니다.
          2. 별도의 요청값 없이 호출합니다.
          3. 저장된 전체 예산 목록을 반환합니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          """
  )
  ResponseEntity<List<BudgetResponse>> getAllBudgets();

  @Operation(
      summary = "[ADMIN] 유저별 예산 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Path Variable

          - `user-id` (UUID, required): 조회할 유저 ID

          요청 예시
          ```text
          /api/budgets/users/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
          ```

          ### 응답 데이터
          - `budgetId` (UUID): 예산 ID
          - `userId` (UUID): 유저 ID
          - `period` (BudgetPeriod): 예산 기간 유형
          - `amount` (Long): 예산 금액
          - `startDate` (LocalDate): 시작일 (CUSTOM인 경우)
          - `endDate` (LocalDate): 종료일 (CUSTOM인 경우)
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 ADMIN 계정의 `Bearer {accessToken}`을 입력합니다.
          2. 조회할 유저 ID를 Path Variable로 전달합니다.
          3. 해당 유저의 예산 정보를 반환합니다.

          ### 유의 사항
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.
          - 해당 유저의 예산이 없을 경우 예외가 발생합니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          - `BUDGET_NOT_FOUND` (404 NOT_FOUND): 예산을 찾을 수 없습니다.
          """
  )
  ResponseEntity<BudgetResponse> getBudgetByUserId(
      @PathVariable(name = "user-id") UUID userId
  );
}
