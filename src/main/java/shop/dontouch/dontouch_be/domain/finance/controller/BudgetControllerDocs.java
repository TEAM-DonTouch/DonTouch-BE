package shop.dontouch.dontouch_be.domain.finance.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.finance.dto.request.BudgetRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.BudgetResponse;

public interface BudgetControllerDocs {

  @Operation(
      summary = "예산 저장 (생성 / 수정)",
      description = """
          ### 요청 파라미터
          Request Body(JSON)

          - `userId` (UUID, required): 예산을 설정할 유저 ID
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
            "userId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
            "period": "MONTHLY",
            "amount": 500000
          }
          ```

          요청 예시 (CUSTOM)
          ```json
          {
            "userId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
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
          1. 유저 ID, 기간 유형, 금액을 입력합니다.
          2. CUSTOM 기간일 경우 startDate, endDate를 함께 입력합니다.
          3. 해당 유저의 예산이 없으면 새로 생성, 이미 있으면 기존 예산을 수정합니다.

          ### 유의 사항
          - `userId`는 실제 존재하는 유저 ID여야 합니다.
          - `period`가 `CUSTOM`일 경우 `startDate`와 `endDate`는 필수입니다.
          - `period`가 `CUSTOM`이 아닌 경우 `startDate`, `endDate`는 무시되며 null로 저장됩니다.
          - 유저당 예산은 하나만 존재합니다 (Upsert 방식).

          ### 예외 처리
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<BudgetResponse> saveBudget(
      @Valid @RequestBody BudgetRequest request
  );

  @Operation(
      summary = "전체 예산 조회",
      description = """
          ### 요청 파라미터
          없음

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
          1. 별도의 요청값 없이 호출합니다.
          2. 저장된 전체 예산 목록을 반환합니다.

          ### 예외 처리
          - `INTERNAL_SERVER_ERROR` (500 INTERNAL_SERVER_ERROR): 서버에 문제가 발생했습니다.
          """
  )
  ResponseEntity<List<BudgetResponse>> getAllBudgets();

  @Operation(
      summary = "유저별 예산 조회",
      description = """
          ### 요청 파라미터
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
          1. 조회할 유저 ID를 Path Variable로 전달합니다.
          2. 해당 유저의 예산 정보를 반환합니다.

          ### 유의 사항
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.
          - 해당 유저의 예산이 없을 경우 예외가 발생합니다.

          ### 예외 처리
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          - `BUDGET_NOT_FOUND` (404 NOT_FOUND): 예산을 찾을 수 없습니다.
          """
  )
  ResponseEntity<BudgetResponse> getBudgetByUserId(
      @PathVariable(name = "user-id") UUID userId
  );

  @Operation(
      summary = "예산 삭제",
      description = """
          ### 요청 파라미터
          Path Variable

          - `budget-id` (UUID, required): 삭제할 예산 ID

          요청 예시
          ```text
          /api/budgets/cccccccc-cccc-cccc-cccc-cccccccccccc
          ```

          ### 응답 데이터
          없음 (204 No Content)

          ### 사용 방법
          1. 삭제할 예산 ID를 Path Variable로 전달합니다.
          2. 요청 성공 시 응답 본문 없이 204 상태 코드가 반환됩니다.

          ### 유의 사항
          - 예산 데이터가 DB에서 완전히 삭제됩니다 (Hard Delete).
          - 존재하지 않는 예산 ID로 요청 시 예외가 발생합니다.

          ### 예외 처리
          - `BUDGET_NOT_FOUND` (404 NOT_FOUND): 예산을 찾을 수 없습니다.
          """
  )
  ResponseEntity<Void> deleteBudget(
      @PathVariable(name = "budget-id") UUID budgetId
  );
}
