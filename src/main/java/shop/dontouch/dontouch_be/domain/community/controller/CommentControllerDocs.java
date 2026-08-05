package shop.dontouch.dontouch_be.domain.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.dontouch.dontouch_be.domain.community.dto.request.CommentRequest;
import shop.dontouch.dontouch_be.domain.community.dto.response.CommentResponse;
import shop.dontouch.dontouch_be.global.common.dto.PageResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface CommentControllerDocs {

  @Operation(
      summary = "댓글 작성",
      description = """
          특정 게시글에 댓글을 작성합니다.

          ### 요청 파라미터
          Header

          - `Authorization: Bearer {accessToken}` (required)

          Path Variable

          - `post-id` (UUID, required): 댓글을 작성할 게시글 ID

          Request Body(JSON)

          - `content` (String, required): 댓글 내용 (공백 불가, 최대 100자)

          요청 예시
          ```json
          {
            "content": "저도 이번 달에 도전해볼게요!"
          }
          ```

          ### 응답 데이터
          `201 CREATED` + `CommentResponse`

          - `commentId` (UUID): 생성된 댓글 ID
          - `postId` (UUID): 댓글이 달린 게시글 ID
          - `authorId` (UUID): 작성자 ID
          - `authorNickname` (String): 작성자 닉네임
          - `authorProfileImageUrl` (String): 작성자 프로필 이미지 URL (없으면 null)
          - `content` (String): 댓글 내용
          - `createdAt` (LocalDateTime): 작성 일시

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 댓글을 달 게시글의 `postId`와 `content`를 담아 요청합니다.
          3. 성공 시 생성된 댓글 정보가 `201 CREATED`로 반환됩니다.

          ### 유의 사항
          - 작성 성공 시 해당 게시글의 `commentCount`가 1 증가합니다.
          - 작성자는 토큰의 사용자로 자동 지정되며, 요청 본문으로 지정할 수 없습니다.
          - 대댓글(답글)은 지원하지 않습니다. 모든 댓글은 게시글에 직접 달립니다.
          - 삭제된 게시글에는 댓글을 작성할 수 없습니다.

          ### 예외 처리
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 댓글 내용이 비어 있거나 100자를 초과했습니다.
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 사용자입니다.
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          """
  )
  ResponseEntity<CommentResponse> createComment(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId,
      @Valid @RequestBody CommentRequest request
  );

  @Operation(
      summary = "댓글 목록 조회",
      description = """
          특정 게시글의 댓글 목록을 페이지 단위로 조회합니다.

          ### 요청 파라미터
          Header

          - `Authorization: Bearer {accessToken}` (required)

          Path Variable

          - `post-id` (UUID, required): 댓글을 조회할 게시글 ID

          Query Parameter

          - `sort` (String, optional, 기본값 `oldest`): 정렬 기준
            - `oldest`: 오래된 순 (작성일 오름차순)
            - `latest`: 최신순 (작성일 내림차순)
            - 그 외 값을 보내면 `oldest`로 처리됩니다.
          - `page` (int, optional, 기본값 0): 페이지 번호 (0부터 시작)
          - `size` (int, optional, 기본값 20): 페이지당 댓글 수 (1 이상 50 이하)

          ### 응답 데이터
          `200 OK` + `PageResponse<CommentResponse>`

          - `content` (List): 댓글 목록 (각 항목은 댓글 작성 API의 `CommentResponse`와 동일한 구조)
          - `page` (int): 현재 페이지 번호
          - `size` (int): 페이지 크기
          - `totalElements` (long): 전체 댓글 수
          - `totalPages` (int): 전체 페이지 수
          - `hasNext` (boolean): 다음 페이지 존재 여부

          응답 예시
          ```json
          {
            "content": [
              {
                "commentId": "cccccccc-cccc-cccc-cccc-cccccccccccc",
                "postId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
                "authorId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
                "authorNickname": "절약왕",
                "authorProfileImageUrl": null,
                "content": "저도 이번 달에 도전해볼게요!",
                "createdAt": "2026-07-28T10:05:00"
              }
            ],
            "page": 0,
            "size": 20,
            "totalElements": 1,
            "totalPages": 1,
            "hasNext": false
          }
          ```

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 게시글 상세 화면 진입 시 `page=0`으로 호출합니다.
          3. `hasNext`가 `true`면 `page`를 1씩 증가시켜 추가로 요청합니다.
          4. 정렬을 바꾸려면 `sort` 값을 변경하고 `page=0`부터 다시 요청합니다.

          ### 유의 사항
          - 정렬은 `sort` 파라미터의 `oldest` / `latest` 두 가지만 지원합니다.
            엔티티 필드명을 직접 지정하는 방식(`?sort=content,desc` 등)은 무시됩니다.
          - 삭제된 댓글은 목록에 포함되지 않으며, `totalElements`에도 집계되지 않습니다.
          - `size`가 50을 초과하거나 `page`가 음수이면 `INVALID_INPUT_VALUE` (400)를 반환합니다.
          - 게시글의 `commentCount`와 이 API의 `totalElements`는 동일한 값을 가리킵니다.

          ### 예외 처리
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): `page`/`size` 값이 허용 범위를 벗어났습니다.
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 사용자입니다.
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          """
  )
  ResponseEntity<PageResponse<CommentResponse>> getComments(
      @PathVariable(name = "post-id") UUID postId,
      @RequestParam(name = "sort", defaultValue = "oldest") String sort,
      @RequestParam(name = "page", defaultValue = "0")
      @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page,
      @RequestParam(name = "size", defaultValue = "20")
      @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
      @Max(value = 50, message = "페이지 크기는 50 이하여야 합니다.") int size
  );

  @Operation(
      summary = "댓글 삭제",
      description = """
          본인이 작성한 댓글을 삭제합니다.

          ### 요청 파라미터
          Header

          - `Authorization: Bearer {accessToken}` (required, 작성자 본인만 가능)

          Path Variable

          - `post-id` (UUID, required): 댓글이 달린 게시글 ID
          - `comment-id` (UUID, required): 삭제할 댓글 ID

          ### 응답 데이터
          `204 NO_CONTENT` (응답 본문 없음)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 댓글 목록에서 받은 `postId`와 `commentId`를 경로에 넣어 호출합니다.

          ### 유의 사항
          - 실제 DB Row를 지우지 않는 Soft Delete 방식입니다.
          - 삭제 성공 시 게시글의 `commentCount`가 1 감소합니다.
          - 작성자 본인만 삭제할 수 있습니다. 게시글 작성자라도 타인의 댓글은 삭제할 수 없습니다.
          - `comment-id`가 `post-id`에 속한 댓글이 아니면 `COMMENT_NOT_FOUND`를 반환합니다.
          - 이미 삭제된 댓글을 다시 삭제하면 `COMMENT_NOT_FOUND`를 반환하며, `commentCount`는 중복으로 감소하지 않습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 사용자입니다.
          - `COMMENT_ACCESS_DENIED` (403 FORBIDDEN): 본인이 작성한 댓글만 삭제할 수 있습니다.
          - `COMMENT_NOT_FOUND` (404 NOT_FOUND): 댓글을 찾을 수 없습니다.
          """
  )
  ResponseEntity<Void> deleteComment(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId,
      @PathVariable(name = "comment-id") UUID commentId
  );
}
