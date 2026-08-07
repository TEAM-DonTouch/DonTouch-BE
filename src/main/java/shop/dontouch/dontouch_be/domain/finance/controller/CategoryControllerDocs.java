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
import shop.dontouch.dontouch_be.domain.finance.dto.request.CategoryRequest;
import shop.dontouch.dontouch_be.domain.finance.dto.response.CategoryResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface CategoryControllerDocs {

  @Operation(
      summary = "내 커스텀 카테고리 생성",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          Request Body(JSON)

          - `categoryName` (String, required): 카테고리 이름 (최대 10자, 공백 불가)

          요청 예시
          ```json
          {
            "categoryName": "용돈"
          }
          ```

          ### 응답 데이터
          - `categoryId` (UUID): 생성된 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `custom` (boolean): 커스텀 카테고리 여부 (항상 true)
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 생성할 카테고리 이름을 입력합니다.
          2. 요청 성공 시 본인 소유의 커스텀 카테고리로 생성됩니다.

          ### 유의 사항
          - `categoryName`은 공백일 수 없고 최대 10자까지 입력 가능합니다.
          - 전역 카테고리 이름 또는 본인의 기존 커스텀 카테고리 이름과 겹치면 예외가 발생합니다.
          - 다른 유저의 커스텀 카테고리 이름과는 겹쳐도 무방합니다.

          ### 예외 처리
          - `CATEGORY_NAME_DUPLICATE` (409 CONFLICT): 이미 존재하는 카테고리입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<CategoryResponse> createMyCategory(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody CategoryRequest request
  );

  @Operation(
      summary = "내가 사용 가능한 카테고리 목록 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          ### 응답 데이터
          카테고리 목록(List<CategoryResponse>) — 전역 카테고리 + 본인의 커스텀 카테고리가 함께 반환됩니다.

          각 카테고리 데이터는 아래 정보를 포함합니다.

          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `custom` (boolean): 커스텀 카테고리 여부 (false면 전역 카테고리)
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 별도의 요청값 없이 호출합니다.
          2. 전역 카테고리 전체와 본인이 생성한 커스텀 카테고리가 함께 반환됩니다.

          ### 유의 사항
          - 다른 유저의 커스텀 카테고리는 조회되지 않습니다.
          - 삭제된 카테고리는 조회되지 않습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          """
  )
  ResponseEntity<List<CategoryResponse>> getMyCategories(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser
  );

  @Operation(
      summary = "내 커스텀 카테고리 삭제",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          Path Variable

          - `category-id` (UUID, required): 삭제할 카테고리 ID

          ### 응답 데이터
          없음 (204 No Content)

          ### 사용 방법
          1. 삭제할 카테고리 ID를 Path Variable로 전달합니다.
          2. 요청 성공 시 응답 본문 없이 204 상태 코드가 반환됩니다.

          ### 유의 사항
          - 본인 소유의 커스텀 카테고리만 삭제할 수 있습니다. 전역 카테고리는 이 API로 삭제할 수 없습니다.
          - 실제 DB Row 삭제가 아닌 Soft Delete 방식입니다.
          - 이미 거래(Transaction)에서 사용 중인 카테고리는 삭제할 수 없습니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): 본인 소유의 카테고리가 아닙니다.
          - `CATEGORY_NOT_FOUND` (404 NOT_FOUND): 카테고리를 찾을 수 없습니다.
          - `CATEGORY_IN_USE` (409 CONFLICT): 이미 거래에서 사용 중인 카테고리는 삭제할 수 없습니다.
          """
  )
  ResponseEntity<Void> deleteMyCategory(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "category-id") UUID categoryId
  );

  ///  ADMIN

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
          - `custom` (boolean): 커스텀 카테고리 여부 (항상 false)
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
      summary = "전역 카테고리 전체 조회",
      description = """
          ### 요청 파라미터
          없음

          ### 응답 데이터
          카테고리 목록(List<CategoryResponse>) — 전역 카테고리만 반환됩니다. (유저 커스텀 카테고리는 `GET /api/categories/me`를 사용하세요)

          각 카테고리 데이터는 아래 정보를 포함합니다.

          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `custom` (boolean): 커스텀 카테고리 여부 (항상 false)
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 별도의 요청값 없이 호출합니다.
          2. 전역 카테고리 목록을 반환합니다.

          ### 유의 사항
          - 삭제된 카테고리는 조회되지 않습니다.
          - `deleted_at IS NULL` 조건이 적용된 데이터만 조회됩니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          """
  )
  ResponseEntity<List<CategoryResponse>> getAllGlobalCategories();

  @Operation(
      summary = "[ADMIN] 특정 유저의 카테고리 조회 (전역+커스텀 병합)",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Path Variable

          - `user-id` (UUID, required): 조회할 유저 ID

          요청 예시
          ```text
          /api/categories/users/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
          ```

          ### 응답 데이터
          카테고리 목록(List<CategoryResponse>) — 전역 카테고리와 해당 유저의 커스텀 카테고리를 병합하여 반환합니다. (해당 유저 입장에서 실제로 보이는 목록과 동일)

          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `custom` (boolean): 커스텀 카테고리 여부 (false면 전역 카테고리)
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 조회할 유저 ID를 Path Variable로 전달합니다.
          2. 전역 카테고리 전체와 해당 유저가 생성한 커스텀 카테고리가 함께 반환됩니다.

          ### 유의 사항
          - CS 문의 대응 등 감사(audit) 목적의 조회입니다.
          - 다른 유저의 커스텀 카테고리는 조회되지 않습니다.
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유효하지 않은 유저입니다.
          """
  )
  ResponseEntity<List<CategoryResponse>> getCategoriesByUserId(
      @PathVariable(name = "user-id") UUID userId
  );

  @Operation(
      summary = "[ADMIN] 전체 카테고리 조회 (전역+커스텀)",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          ### 응답 데이터
          카테고리 목록(List<CategoryResponse>) — 전역 카테고리와 모든 유저의 커스텀 카테고리를 필터 없이 전부 반환합니다.

          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `custom` (boolean): 커스텀 카테고리 여부
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 별도의 요청값 없이 호출합니다.
          2. 전역 카테고리와 모든 유저의 커스텀 카테고리를 함께 반환합니다.

          ### 유의 사항
          - 전체 모니터링/감사 목적의 조회입니다.
          - 삭제된 카테고리는 조회되지 않습니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          """
  )
  ResponseEntity<List<CategoryResponse>> getAllCategories();

  @Operation(
      summary = "카테고리 단건 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`
          - 전역 카테고리 조회 시 선택
          - 커스텀 카테고리 조회 시 필수

          Path Variable
          - `category-id` (UUID, required): 조회할 카테고리 ID

          요청 예시
          ```text
          /api/categories/bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb
          ```

          ### 응답 데이터
          - `categoryId` (UUID): 카테고리 ID
          - `categoryName` (String): 카테고리 이름
          - `custom` (boolean): 커스텀 카테고리 여부
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 조회할 카테고리 ID를 Path Variable로 전달합니다.
          2. 전역 카테고리는 로그인하지 않아도 조회할 수 있습니다.
          3. 커스텀 카테고리는 소유자 또는 ADMIN만 조회할 수 있습니다.

          ### 유의 사항
          - 다른 사용자의 커스텀 카테고리는 조회할 수 없습니다.
          - 존재하지 않거나 삭제된 카테고리는 조회되지 않습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `ACCESS_DENIED` (403 FORBIDDEN): 커스텀 카테고리 조회 권한이 없습니다.
          - `CATEGORY_NOT_FOUND` (404 NOT_FOUND): 카테고리를 찾을 수 없습니다.
          """
  )
  ResponseEntity<CategoryResponse> getCategoryByCategoryId(
      @Parameter(hidden = true)
      @AuthenticationPrincipal
      CustomUserDetails currentUser,

      @PathVariable(name = "category-id")
      UUID categoryId
  );

  @Operation(
      summary = "[ADMIN] 전역 카테고리 수정",
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
          - `custom` (boolean): 커스텀 카테고리 여부 (항상 false)
          - `createdAt` (LocalDateTime): 생성 일시
          - `updatedAt` (LocalDateTime): 수정 일시

          ### 사용 방법
          1. 수정할 카테고리 ID를 Path Variable로 전달합니다.
          2. 변경할 카테고리 이름을 Request Body에 입력합니다.
          3. 수정 완료 후 변경된 카테고리 정보를 반환합니다.

          ### 유의 사항
          - `categoryName`은 공백일 수 없습니다.
          - 전역 카테고리만 수정할 수 있습니다. 유저의 커스텀 카테고리 ID를 전달하면 예외가 발생합니다.
          - 이미 존재하는 카테고리 이름으로 수정 시 예외가 발생합니다.
          - 존재하지 않는 카테고리 ID로 요청 시 예외가 발생합니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요하거나, 대상이 전역 카테고리가 아닙니다.
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
      summary = "[ADMIN] 카테고리 삭제 (전역+커스텀 무관)",
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
          2. 대상이 전역 카테고리든 특정 유저의 커스텀 카테고리든 스코프와 무관하게 삭제됩니다.
          3. 요청 성공 시 응답 본문 없이 204 상태 코드가 반환됩니다.

          ### 유의 사항
          - ADMIN은 전역 카테고리뿐 아니라 유저가 생성한 커스텀 카테고리도 삭제할 수 있습니다.
          - 실제 DB Row 삭제가 아닌 Soft Delete 방식입니다.
          - 삭제 시 `deletedAt` 값이 저장됩니다.
          - `@SQLRestriction("deleted_at IS NULL")` 조건으로 인해 삭제된 데이터는 이후 조회되지 않습니다.
          - 존재하지 않는 카테고리 ID로 요청 시 예외가 발생합니다.
          - 이미 거래(Transaction)에서 사용 중인 카테고리는 삭제할 수 없습니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `CATEGORY_NOT_FOUND` (404 NOT_FOUND): 카테고리를 찾을 수 없습니다.
          - `CATEGORY_IN_USE` (409 CONFLICT): 이미 거래에서 사용 중인 카테고리는 삭제할 수 없습니다.
          """
  )
  ResponseEntity<Void> deleteCategory(
      @PathVariable(name = "category-id") UUID categoryId
  );
}
