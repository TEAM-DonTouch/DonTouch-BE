package shop.dontouch.dontouch_be.domain.finance.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.finance.dto.TransactionRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.TransactionResponse;
import shop.dontouch.dontouch_be.domain.finance.dto.TransactionUpdateRequest;

public interface TransactionControllerDocs {

  @Operation(
      summary = "거래 내역 생성",
      description = """
          ### 요청 파라미터
          Request Body(JSON)
          
          - `userId` (UUID, required): 거래를 생성할 유저 ID
          - `type` (TransactionType, required): 거래 유형
            - `INCOME`: 수입
            - `EXPENSE`: 지출
          - `amount` (Long, required): 거래 금액 (1 이상)
          - `memo` (String, required): 거래 메모 (최대 30자)
          - `transactionDate` (LocalDateTime, required): 거래 발생 일시
          
          요청 예시
          ```json
          {
            "userId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
            "type": "INCOME",
            "amount": 50000,
            "memo": "월급",
            "transactionDate": "2026-05-19T10:00:00"
          }
          ```
          
          ### 응답 데이터
          - `transactionId` (UUID): 생성된 거래 ID
          - `userId` (UUID): 유저 ID
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시
          
          ### 사용 방법
          1. 거래를 생성할 유저 ID를 입력합니다.
          2. 거래 유형(INCOME 또는 EXPENSE)을 입력합니다.
          3. 거래 금액, 메모, 거래 일시를 입력합니다.
          4. 요청 성공 시 생성된 거래 정보를 반환합니다.
          
          ### 유의 사항
          - `userId`는 실제 존재하는 유저 ID여야 합니다.
          - `type`은 `INCOME`, `EXPENSE`만 허용됩니다.
          - `amount`는 1 이상이어야 합니다.
          - `memo`는 공백일 수 없으며 최대 30자까지 입력 가능합니다.
          - `transactionDate`는 필수 입력값입니다.
          
          ### 예외 처리
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<TransactionResponse> createTransaction(
      @Valid @RequestBody TransactionRequest request
  );

  @Operation(
      summary = "전체 거래 내역 조회",
      description = """
          ### 요청 파라미터
          없음
          
          ### 응답 데이터
          거래 내역 목록(List<TransactionResponse>)
          
          각 거래 데이터는 아래 정보를 포함합니다.
          
          - `transactionId` (UUID): 거래 ID
          - `userId` (UUID): 유저 ID
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시
          
          ### 사용 방법
          1. 별도의 요청값 없이 호출합니다.
          2. 저장된 전체 거래 내역 목록을 반환합니다.
          
          ### 유의 사항
          - 삭제된 거래 데이터는 조회되지 않습니다.
          - `deleted_at IS NULL` 조건이 적용된 데이터만 조회됩니다.
          - 현재 코드 기준 TODO 주석에 따라 추후 ADMIN 권한 전용으로 변경 예정입니다.
          
          ### 예외 처리
          - `INTERNAL_SERVER_ERROR` (500 INTERNAL_SERVER_ERROR): 서버에 문제가 발생했습니다.
          """
  )
  ResponseEntity<List<TransactionResponse>> getAllTransactions();

  @Operation(
      summary = "유저별 거래 내역 조회",
      description = """
          ### 요청 파라미터
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
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시
          
          ### 사용 방법
          1. 조회할 유저 ID를 Path Variable로 전달합니다.
          2. 해당 유저의 거래 내역 목록을 반환합니다.
          
          ### 유의 사항
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.
          - Repository의 `findAllByUserId(UUID userId)` 메서드를 사용하여 조회합니다.
          - 삭제된 거래 데이터는 조회되지 않습니다.
          
          ### 예외 처리
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저를 찾을 수 없습니다.
          """
  )
  ResponseEntity<List<TransactionResponse>> getAllTransactionByUserId(
      @PathVariable(name = "user-id") UUID userId
  );

  @Operation(
      summary = "거래 단건 조회",
      description = """
          ### 요청 파라미터
          Path Variable
          
          - `transaction-id` (UUID, required): 조회할 거래 ID
          
          요청 예시
          ```text
          /api/transactions/bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
          ```
          
          ### 응답 데이터
          - `transactionId` (UUID): 거래 ID
          - `userId` (UUID): 유저 ID
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시
          
          ### 사용 방법
          1. 조회할 거래 ID를 Path Variable로 전달합니다.
          2. 해당 거래 정보를 반환합니다.
          
          ### 유의 사항
          - 존재하지 않는 거래 ID로 요청 시 예외가 발생합니다.
          - 삭제된 거래 데이터는 조회되지 않습니다.
          
          ### 예외 처리
          - `TRANSACTION_NOT_FOUND` (404 NOT_FOUND): 거래를 찾을 수 없습니다.
          """
  )
  ResponseEntity<TransactionResponse> getTransactionByTransactionId(
      @PathVariable(name = "transaction-id") UUID transactionId
  );

  @Operation(
      summary = "거래 내역 수정",
      description = """
          ### 요청 파라미터
          Path Variable
          
          - `transaction-id` (UUID, required): 수정할 거래 ID
          
          Request Body(JSON)
          
          아래 필드는 선택적으로 수정 가능합니다.
          
          - `type` (TransactionType, optional): 거래 유형
            - `INCOME`
            - `EXPENSE`
          - `amount` (Long, optional): 거래 금액 (1 이상)
          - `memo` (String, optional): 거래 메모 (최대 30자)
          - `transactionDate` (LocalDateTime, optional): 거래 발생 일시
          
          요청 예시
          ```json
          {
            "amount": 100000,
            "memo": "수정된 메모"
          }
          ```
          
          ### 응답 데이터
          - `transactionId` (UUID): 거래 ID
          - `userId` (UUID): 유저 ID
          - `type` (TransactionType): 거래 유형
          - `amount` (Long): 거래 금액
          - `memo` (String): 거래 메모
          - `transactionDate` (LocalDateTime): 거래 발생 일시
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시
          
          ### 사용 방법
          1. 수정할 거래 ID를 Path Variable로 전달합니다.
          2. 수정할 필드만 Request Body에 포함하여 요청합니다.
          3. 수정 완료 후 변경된 거래 정보를 반환합니다.
          
          ### 유의 사항
          - 전달하지 않은 필드는 기존 값이 유지됩니다.
          - `memo`가 blank 값이면 수정되지 않습니다.
          - `amount`는 1 이상이어야 합니다.
          - 존재하지 않는 거래 ID로 요청 시 예외가 발생합니다.
          
          ### 예외 처리
          - `TRANSACTION_NOT_FOUND` (404 NOT_FOUND): 거래를 찾을 수 없습니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<TransactionResponse> updateTransaction(
      @PathVariable(name = "transaction-id") UUID transactionId,
      @Valid @RequestBody TransactionUpdateRequest request
  );

  @Operation(
      summary = "거래 내역 삭제",
      description = """
          ### 요청 파라미터
          Path Variable
          
          - `transaction-id` (UUID, required): 삭제할 거래 ID
          
          요청 예시
          ```text
          /api/transactions/bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
          ```
          
          ### 응답 데이터
          없음 (204 No Content)
          
          ### 사용 방법
          1. 삭제할 거래 ID를 Path Variable로 전달합니다.
          2. 요청 성공 시 응답 본문 없이 204 상태 코드가 반환됩니다.
          
          ### 유의 사항
          - 실제 DB Row 삭제가 아닌 Soft Delete 방식입니다.
          - 삭제 시 `deletedAt` 값이 저장됩니다.
          - `@SQLRestriction("deleted_at IS NULL")` 조건으로 인해 삭제된 데이터는 이후 조회되지 않습니다.
          - 존재하지 않는 거래 ID로 요청 시 예외가 발생합니다.
          
          ### 예외 처리
          - `TRANSACTION_NOT_FOUND` (404 NOT_FOUND): 거래를 찾을 수 없습니다.
          """
  )
  ResponseEntity<Void> deleteTransaction(
      @PathVariable(name = "transaction-id") UUID transactionId
  );
}