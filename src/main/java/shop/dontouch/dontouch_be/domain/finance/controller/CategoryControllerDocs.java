package shop.dontouch.dontouch_be.domain.finance.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.finance.dto.request.CategoryRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.CategoryResponse;

public interface CategoryControllerDocs {

  @Operation(
      summary = "[ADMIN] 카테고리 생성",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Request Body(JSON)

          - `categoryName` (String, required): 카테고리 이름 (최대 10자, 공백 불가)

          요청 예시
          ```json
          {
            "categoryName": "식비"
          }
          ```

          ### 응답 데이터
          - `categoryId` (UUID): 생성된 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 생성할 카테고리 이름을 입력합니다.
          2. 요청 성공 시 생성된 카테고리 정보를 반환합니다.

          ### 유의 사항
          - `categoryName`은 공백일 수 없습니다.
          - `categoryName`은 최대 10자까지 입력 가능합니다.
          - 이미 존재하는 카테고리 이름으로 생성 시 예외가 발생합니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `CATEGORY_NAME_DUPLICATE` (409 CONFLICT): 이미 존재하는 카테고리입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<CategoryResponse> createCategory(
      @Valid @RequestBody CategoryRequest request
  );

  @Operation(
      summary = "전체 카테고리 조회",
      description = """
          ### 요청 파라미터
          없음

          ### 응답 데이터
          카테고리 목록(List<CategoryResponse>)

          각 카테고리 데이터는 아래 정보를 포함합니다.

          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 별도의 요청값 없이 호출합니다.
          2. 저장된 전체 카테고리 목록을 반환합니다.

          ### 유의 사항
          - 삭제된 카테고리는 조회되지 않습니다.
          - `deleted_at IS NULL` 조건이 적용된 데이터만 조회됩니다.

          ### 예외 처리
          - `INTERNAL_SERVER_ERROR` (500 INTERNAL_SERVER_ERROR): 서버에 문제가 발생했습니다.
          """
  )
  ResponseEntity<List<CategoryResponse>> getAllCategories();

  @Operation(
      summary = "카테고리 단건 조회",
      description = """
          ### 요청 파라미터
          Path Variable

          - `category-id` (UUID, required): 조회할 카테고리 ID

          요청 예시
          ```text
          /api/categories/bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
          ```

          ### 응답 데이터
          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 조회할 카테고리 ID를 Path Variable로 전달합니다.
          2. 해당 카테고리 정보를 반환합니다.

          ### 유의 사항
          - 존재하지 않는 카테고리 ID로 요청 시 예외가 발생합니다.
          - 삭제된 카테고리는 조회되지 않습니다.

          ### 예외 처리
          - `CATEGORY_NOT_FOUND` (404 NOT_FOUND): 카테고리를 찾을 수 없습니다.
          """
  )
  ResponseEntity<CategoryResponse> getCategoryByCategoryId(
      @PathVariable(name = "category-id") UUID categoryId
  );

  @Operation(
      summary = "[ADMIN] 카테고리 수정",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Path Variable

          - `category-id` (UUID, required): 수정할 카테고리 ID

          Request Body(JSON)

          - `categoryName` (String, required): 변경할 카테고리 이름 (최대 10자, 공백 불가)

          요청 예시
          ```json
          {
            "categoryName": "교통비"
          }
          ```

          ### 응답 데이터
          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 변경된 카테고리 이름
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 수정할 카테고리 ID를 Path Variable로 전달합니다.
          2. 변경할 카테고리 이름을 Request Body에 입력합니다.
          3. 수정 완료 후 변경된 카테고리 정보를 반환합니다.

          ### 유의 사항
          - `categoryName`은 공백일 수 없습니다.
          - 이미 존재하는 카테고리 이름으로 수정 시 예외가 발생합니다.
          - 존재하지 않는 카테고리 ID로 요청 시 예외가 발생합니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `CATEGORY_NOT_FOUND` (404 NOT_FOUND): 카테고리를 찾을 수 없습니다.
          - `CATEGORY_NAME_DUPLICATE` (409 CONFLICT): 이미 존재하는 카테고리입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<CategoryResponse> updateCategory(
      @PathVariable(name = "category-id") UUID categoryId,
      @Valid @RequestBody CategoryRequest request
  );

  @Operation(
      summary = "[ADMIN] 카테고리 삭제",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Path Variable

          - `category-id` (UUID, required): 삭제할 카테고리 ID

          요청 예시
          ```text
          /api/categories/bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
          ```

          ### 응답 데이터
          없음 (204 No Content)

          ### 사용 방법
          1. 삭제할 카테고리 ID를 Path Variable로 전달합니다.
          2. 요청 성공 시 응답 본문 없이 204 상태 코드가 반환됩니다.

          ### 유의 사항
          - 실제 DB Row 삭제가 아닌 Soft Delete 방식입니다.
          - 삭제 시 `deletedAt` 값이 저장됩니다.
          - `@SQLRestriction("deleted_at IS NULL")` 조건으로 인해 삭제된 데이터는 이후 조회되지 않습니다.
          - 존재하지 않는 카테고리 ID로 요청 시 예외가 발생합니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `CATEGORY_NOT_FOUND` (404 NOT_FOUND): 카테고리를 찾을 수 없습니다.
          """
  )
  ResponseEntity<Void> deleteCategory(
      @PathVariable(name = "category-id") UUID categoryId
  );
}
