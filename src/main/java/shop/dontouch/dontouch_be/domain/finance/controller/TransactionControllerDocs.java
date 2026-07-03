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
import shop.dontouch.dontouch_be.domain.finance.dto.request.TransactionRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.TransactionResponse;
import shop.dontouch.dontouch_be.domain.finance.dto.request.TransactionUpdateRequest;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface TransactionControllerDocs {

  // ==================== 본인 전용 ====================

  @Operation(
      summary = "내 거래 내역 생성",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          Request Body(JSON)

          - `categoryId` (UUID, required): 카테고리 ID
          - `type` (TransactionType, required): 거래 유형
            - `INCOME`: 수입
            - `EXPENSE`: 지출
          - `amount` (Long, required): 거래 금액 (1 이상)
          - `memo` (String, optional): 거래 메모 (최대 30자)
          - `transactionDate` (LocalDateTime, required): 거래 발생 일시

          요청 예시
          ```json
          {
            "categoryId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
            "type": "INCOME",
            "amount": 50000,
            "memo": "월급",
            "transactionDate": "2026-05-19T10:00:00"
          }
          ```

          ### 응답 데이터
          - `transactionId` (UUID): 생성된 거래 ID
          - `userId` (UUID): 유저 ID
          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 카테고리 ID를 입력합니다.
          3. 거래 유형(INCOME 또는 EXPENSE)을 입력합니다.
          4. 거래 금액, 메모, 거래 일시를 입력합니다.
          5. 요청 성공 시 인증된 본인 명의로 생성된 거래 정보를 반환합니다.

          ### 유의 사항
          - 탈퇴(WITHDRAWN) 상태인 유저는 거래를 생성할 수 없습니다.
          - `categoryId`는 실제 존재하는 카테고리 ID여야 합니다.
          - `type`은 `INCOME`, `EXPENSE`만 허용됩니다.
          - `amount`는 1 이상이어야 합니다.
          - `memo`는 선택 입력값이며 최대 30자까지 입력 가능합니다.
          - `memo`를 비워두거나 공백만 입력하면 저장 시 `null`로 처리됩니다.
          - `transactionDate`는 필수 입력값입니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          - `USER_ALREADY_WITHDRAWN` (409 CONFLICT): 이미 탈퇴한 사용자입니다.
          - `CATEGORY_NOT_FOUND` (404 NOT_FOUND): 카테고리를 찾을 수 없습니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<TransactionResponse> createTransaction(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody TransactionRequest request
  );

  @Operation(
      summary = "내 거래 내역 목록 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          ### 응답 데이터
          거래 내역 목록(List<TransactionResponse>)

          각 거래 데이터는 아래 정보를 포함합니다.

          - `transactionId` (UUID): 거래 ID
          - `userId` (UUID): 유저 ID
          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 별도의 요청값 없이 호출합니다.
          3. 인증된 본인의 거래 내역 목록을 반환합니다.

          ### 유의 사항
          - 삭제된 거래 데이터는 조회되지 않습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          """
  )
  ResponseEntity<List<TransactionResponse>> getMyTransactions(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser
  );

  @Operation(
      summary = "내 거래 내역 카테고리별 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          Path Variable

          - `category-id` (UUID, required): 조회할 카테고리 ID

          요청 예시
          ```text
          /api/transactions/me/categories/bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
          ```

          ### 응답 데이터
          거래 내역 목록(List<TransactionResponse>)

          각 거래 데이터는 아래 정보를 포함합니다.

          - `transactionId` (UUID): 거래 ID
          - `userId` (UUID): 유저 ID
          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 조회할 카테고리 ID를 Path Variable로 전달합니다.
          3. 인증된 본인 거래 내역 중 해당 카테고리의 목록을 반환합니다.

          ### 유의 사항
          - 다른 유저의 거래 내역은 조회되지 않습니다(본인 소유 거래로 범위 제한).
          - 존재하지 않는 카테고리 ID로 요청 시 예외가 발생합니다.
          - 삭제된 거래 데이터는 조회되지 않습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `CATEGORY_NOT_FOUND` (404 NOT_FOUND): 카테고리를 찾을 수 없습니다.
          """
  )
  ResponseEntity<List<TransactionResponse>> getAllTransactionsByCategoryId(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "category-id") UUID categoryId
  );

  @Operation(
      summary = "내 거래 내역 수정",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          Path Variable

          - `transaction-id` (UUID, required): 수정할 거래 ID

          Request Body(JSON)

          아래 필드는 선택적으로 수정 가능합니다.

          - `categoryId` (UUID, optional): 카테고리 ID
          - `type` (TransactionType, optional): 거래 유형
            - `INCOME`
            - `EXPENSE`
          - `amount` (Long, optional): 거래 금액 (1 이상)
          - `memo` (String, optional): 거래 메모 (최대 30자)
          - `transactionDate` (LocalDateTime, optional): 거래 발생 일시

          요청 예시
          ```json
          {
            "categoryId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
            "amount": 100000,
            "memo": "수정된 메모"
          }
          ```

          ### 응답 데이터
          - `transactionId` (UUID): 거래 ID
          - `userId` (UUID): 유저 ID
          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 수정할 거래 ID를 Path Variable로 전달합니다.
          3. 수정할 필드만 Request Body에 포함하여 요청합니다.
          4. 수정 완료 후 변경된 거래 정보를 반환합니다.

          ### 유의 사항
          - 전달하지 않은 필드는 기존 값이 유지됩니다.
          - `categoryId`는 실제 존재하는 카테고리 ID여야 합니다.
          - `memo`가 blank 값이면 수정되지 않습니다.
          - `amount`는 1 이상이어야 합니다.
          - 존재하지 않는 거래 ID로 요청 시 예외가 발생합니다.
          - 본인 소유의 거래가 아닌 경우 수정할 수 없습니다.
          - 거래 소유자가 탈퇴(WITHDRAWN) 상태이면 수정할 수 없습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `TRANSACTION_NOT_FOUND` (404 NOT_FOUND): 거래를 찾을 수 없습니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): 본인 소유의 거래가 아닙니다.
          - `USER_ALREADY_WITHDRAWN` (409 CONFLICT): 이미 탈퇴한 사용자입니다.
          - `CATEGORY_NOT_FOUND` (404 NOT_FOUND): 카테고리를 찾을 수 없습니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<TransactionResponse> updateTransaction(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "transaction-id") UUID transactionId,
      @Valid @RequestBody TransactionUpdateRequest request
  );

  @Operation(
      summary = "내 거래 내역 삭제",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          Path Variable

          - `transaction-id` (UUID, required): 삭제할 거래 ID

          요청 예시
          ```text
          /api/transactions/me/bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
          ```

          ### 응답 데이터
          없음 (204 No Content)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 삭제할 거래 ID를 Path Variable로 전달합니다.
          3. 요청 성공 시 응답 본문 없이 204 상태 코드가 반환됩니다.

          ### 유의 사항
          - 실제 DB Row 삭제가 아닌 Soft Delete 방식입니다.
          - 삭제 시 `deletedAt` 값이 저장됩니다.
          - `@SQLRestriction("deleted_at IS NULL")` 조건으로 인해 삭제된 데이터는 이후 조회되지 않습니다.
          - 존재하지 않는 거래 ID로 요청 시 예외가 발생합니다.
          - 본인 소유의 거래가 아닌 경우 삭제할 수 없습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `TRANSACTION_NOT_FOUND` (404 NOT_FOUND): 거래를 찾을 수 없습니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): 본인 소유의 거래가 아닙니다.
          """
  )
  ResponseEntity<Void> deleteTransaction(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "transaction-id") UUID transactionId
  );

  // ==================== ADMIN 전용 ====================

  @Operation(
      summary = "[ADMIN] 전체 거래 내역 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          ### 응답 데이터
          거래 내역 목록(List<TransactionResponse>)

          각 거래 데이터는 아래 정보를 포함합니다.

          - `transactionId` (UUID): 거래 ID
          - `userId` (UUID): 유저 ID
          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 ADMIN 계정의 `Bearer {accessToken}`을 입력합니다.
          2. 별도의 요청값 없이 호출합니다.
          3. 저장된 전체 거래 내역 목록을 반환합니다.

          ### 유의 사항
          - ADMIN 권한을 가진 유저만 호출할 수 있습니다.
          - 삭제된 거래 데이터는 조회되지 않습니다.
          - `deleted_at IS NULL` 조건이 적용된 데이터만 조회됩니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          """
  )
  ResponseEntity<List<TransactionResponse>> getAllTransactions();

  @Operation(
      summary = "[ADMIN] 거래 단건 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Path Variable

          - `transaction-id` (UUID, required): 조회할 거래 ID

          요청 예시
          ```text
          /api/transactions/bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
          ```

          ### 응답 데이터
          - `transactionId` (UUID): 거래 ID
          - `userId` (UUID): 유저 ID
          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 ADMIN 계정의 `Bearer {accessToken}`을 입력합니다.
          2. 조회할 거래 ID를 Path Variable로 전달합니다.
          3. 해당 거래 정보를 반환합니다.

          ### 유의 사항
          - ADMIN 권한을 가진 유저만 호출할 수 있습니다.
          - 존재하지 않는 거래 ID로 요청 시 예외가 발생합니다.
          - 삭제된 거래 데이터는 조회되지 않습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `TRANSACTION_NOT_FOUND` (404 NOT_FOUND): 거래를 찾을 수 없습니다.
          """
  )
  ResponseEntity<TransactionResponse> getTransactionByTransactionId(
      @PathVariable(name = "transaction-id") UUID transactionId
  );

  @Operation(
      summary = "[ADMIN] 유저별 거래 내역 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Path Variable

          - `user-id` (UUID, required): 조회할 유저 ID

          요청 예시
          ```text
          /api/transactions/users/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
          ```

          ### 응답 데이터
          거래 내역 목록(List<TransactionResponse>)

          각 거래 데이터는 아래 정보를 포함합니다.

          - `transactionId` (UUID): 거래 ID
          - `userId` (UUID): 유저 ID
          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 ADMIN 계정의 `Bearer {accessToken}`을 입력합니다.
          2. 조회할 유저 ID를 Path Variable로 전달합니다.
          3. 해당 유저의 거래 내역 목록을 반환합니다.

          ### 유의 사항
          - ADMIN 권한을 가진 유저만 호출할 수 있습니다.
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.
          - Repository의 `findAllByUserIdWithCategory(UUID userId)` 메서드를 사용하여 카테고리를 함께 조회합니다(N+1 방지).
          - 삭제된 거래 데이터는 조회되지 않습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          """
  )
  ResponseEntity<List<TransactionResponse>> getAllTransactionsByUserId(
      @PathVariable(name = "user-id") UUID userId
  );
}
