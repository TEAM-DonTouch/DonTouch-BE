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
import shop.dontouch.dontouch_be.domain.community.dto.request.PostRequest;
import shop.dontouch.dontouch_be.domain.community.dto.request.PostUpdateRequest;
import shop.dontouch.dontouch_be.domain.community.dto.response.PostLikeResponse;
import shop.dontouch.dontouch_be.domain.community.dto.response.PostResponse;
import shop.dontouch.dontouch_be.global.common.dto.PageResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface PostControllerDocs {

  @Operation(
      summary = "게시글 작성",
      description = """
          커뮤니티에 새 게시글을 작성합니다.

          ### 요청 파라미터
          Header

          - `Authorization: Bearer {accessToken}` (required)

          Request Body(JSON)

          - `title` (String, required): 게시글 제목 (공백 불가, 최대 30자)
          - `content` (String, required): 게시글 내용 (공백 불가, 최대 255자)

          요청 예시
          ```json
          {
            "title": "이번 달 절약 성공기",
            "content": "이번 달에 식비를 20% 줄였어요."
          }
          ```

          ### 응답 데이터
          `201 CREATED` + `PostResponse`

          - `postId` (UUID): 생성된 게시글 ID
          - `authorId` (UUID): 작성자 ID
          - `authorNickname` (String): 작성자 닉네임
          - `authorProfileImageUrl` (String): 작성자 프로필 이미지 URL (없으면 null)
          - `title` (String): 제목
          - `content` (String): 내용
          - `viewCount` (int): 조회수 - 항상 0
          - `likeCount` (int): 좋아요 수 - 항상 0
          - `commentCount` (int): 댓글 수 - 항상 0
          - `isLiked` (boolean): 요청자의 좋아요 여부 - 항상 false
          - `createdAt` (LocalDateTime): 작성 일시
          - `updatedAt` (LocalDateTime): 수정 일시 (작성 직후에는 `createdAt`과 동일)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. `title`, `content`를 담아 요청합니다.
          3. 성공 시 생성된 게시글 정보가 `201 CREATED`로 반환됩니다.

          ### 유의 사항
          - 작성자는 토큰의 사용자로 자동 지정되며, 요청 본문으로 지정할 수 없습니다.
          - 제목과 내용 모두 필수이며, 공백만 입력할 수 없습니다.

          ### 예외 처리
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 제목/내용이 비어 있거나 길이 제한을 초과했습니다.
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 사용자입니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          """
  )
  ResponseEntity<PostResponse> createPost(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody PostRequest request
  );

  @Operation(
      summary = "게시글 목록 조회",
      description = """
          커뮤니티 게시글 목록을 페이지 단위로 조회합니다.

          ### 요청 파라미터
          Header

          - `Authorization: Bearer {accessToken}` (required)

          Query Parameter

          - `sort` (String, optional, 기본값 `latest`): 정렬 기준
            - `latest`: 최신순 (작성일 내림차순)
            - `popular`: 인기순 (좋아요 수 내림차순, 동점 시 작성일 내림차순)
            - `views`: 조회순 (조회수 내림차순, 동점 시 작성일 내림차순)
            - 그 외 값을 보내면 `latest`로 처리됩니다.
            - 엔티티 필드명을 직접 지정하는 방식(`?sort=likeCount,desc` 등)은 무시됩니다.
          - `page` (int, optional, 기본값 0): 페이지 번호 (0부터 시작)
          - `size` (int, optional, 기본값 10): 페이지당 게시글 수 (1 이상 50 이하)

          ### 응답 데이터
          `200 OK` + `PageResponse<PostResponse>`

          - `content` (List): 게시글 목록 (각 항목은 게시글 작성 API의 `PostResponse`와 동일한 구조)
          - `page` (int): 현재 페이지 번호
          - `size` (int): 페이지 크기
          - `totalElements` (long): 전체 게시글 수
          - `totalPages` (int): 전체 페이지 수
          - `hasNext` (boolean): 다음 페이지 존재 여부

          응답 예시
          ```json
          {
            "content": [
              {
                "postId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
                "authorId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
                "authorNickname": "절약왕",
                "authorProfileImageUrl": null,
                "title": "이번 달 절약 성공기",
                "content": "이번 달에 식비를 20% 줄였어요.",
                "viewCount": 12,
                "likeCount": 3,
                "commentCount": 1,
                "isLiked": true,
                "createdAt": "2026-07-28T10:00:00",
                "updatedAt": "2026-07-28T10:00:00"
              }
            ],
            "page": 0,
            "size": 10,
            "totalElements": 1,
            "totalPages": 1,
            "hasNext": false
          }
          ```

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 첫 페이지는 파라미터 없이 호출하고, 이후 `page`를 1씩 증가시켜 요청합니다.
          3. `hasNext`가 `false`가 되면 마지막 페이지입니다.
          4. 정렬을 바꾸려면 `sort` 값을 변경하고 `page=0`부터 다시 요청합니다.

          ### 유의 사항
          - 삭제된 게시글은 목록에 포함되지 않습니다.
          - `size`가 50을 초과하거나 `page`가 음수이면 `INVALID_INPUT_VALUE` (400)를 반환합니다.
          - `isLiked`는 요청한 사용자 기준으로 계산됩니다.
          - 이 API는 조회수를 증가시키지 않습니다. 조회수는 상세 조회에서만 증가합니다.

          ### 예외 처리
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): `page`/`size` 값이 허용 범위를 벗어났습니다.
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 사용자입니다.
          """
  )
  ResponseEntity<PageResponse<PostResponse>> getPosts(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @RequestParam(name = "sort", defaultValue = "latest") String sort,
      @RequestParam(name = "page", defaultValue = "0")
      @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page,
      @RequestParam(name = "size", defaultValue = "10")
      @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
      @Max(value = 50, message = "페이지 크기는 50 이하여야 합니다.") int size
  );

  @Operation(
      summary = "게시글 상세 조회",
      description = """
          게시글 1건을 조회하고 조회수를 1 증가시킵니다.

          ### 요청 파라미터
          Header

          - `Authorization: Bearer {accessToken}` (required)

          Path Variable

          - `post-id` (UUID, required): 조회할 게시글 ID

          ### 응답 데이터
          `200 OK` + `PostResponse` (게시글 작성 API와 동일한 구조)

          - `viewCount`는 이번 조회가 반영된 값으로 반환됩니다.
          - `isLiked`는 요청한 사용자 기준으로 계산됩니다.

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 목록 조회로 받은 `postId`를 `post-id`에 넣어 호출합니다.

          ### 유의 사항
          - 호출할 때마다 `viewCount`가 1 증가합니다. 본인 게시글을 조회해도 증가합니다.
          - 삭제된 게시글은 조회할 수 없습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 사용자입니다.
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          """
  )
  ResponseEntity<PostResponse> getPost(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId
  );

  @Operation(
      summary = "게시글 수정",
      description = """
          본인이 작성한 게시글의 제목과 내용을 수정합니다. 변경할 항목만 보내는 부분 수정 방식입니다.

          ### 요청 파라미터
          Header

          - `Authorization: Bearer {accessToken}` (required, 작성자 본인만 가능)

          Path Variable

          - `post-id` (UUID, required): 수정할 게시글 ID

          Request Body(JSON) — 변경할 필드만 포함

          - `title` (String, optional): 게시글 제목 (최대 30자, 공백만 입력 불가)
          - `content` (String, optional): 게시글 내용 (최대 255자, 공백만 입력 불가)

          요청 예시 (제목만 수정)
          ```json
          {
            "title": "이번 달 절약 성공기 (수정)"
          }
          ```

          요청 예시 (제목과 내용 모두 수정)
          ```json
          {
            "title": "이번 달 절약 성공기 (수정)",
            "content": "식비를 20% 줄인 방법을 정리했어요."
          }
          ```

          ### 응답 데이터
          `200 OK` + `PostResponse` (수정이 반영된 게시글 정보)

          - `updatedAt`은 이번 수정이 반영된 시각으로 갱신되어 반환됩니다.

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 수정할 게시글의 `postId`와 변경할 항목만 담아 요청합니다.
          3. 응답의 `PostResponse`로 화면을 갱신하면 됩니다. 별도 재조회가 필요 없습니다.

          ### 유의 사항
          - 전달하지 않은 필드는 기존 값이 그대로 유지됩니다.
          - 값을 `null`로 보내는 것과 필드를 생략하는 것은 동일하게 "변경 없음"으로 처리됩니다.
            따라서 제목이나 내용을 빈 값으로 만들 수는 없습니다.
          - 두 필드를 모두 생략하면 아무것도 변경되지 않으며, `updatedAt`도 갱신되지 않습니다.
          - 작성자 본인만 수정할 수 있습니다. 관리자 권한으로도 타인의 게시글은 수정할 수 없습니다.
          - 수정해도 `viewCount`, `likeCount`, `commentCount`는 변하지 않습니다.

          ### 예외 처리
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 제목/내용이 길이 제한을 초과했거나 공백만 입력됐습니다.
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 사용자입니다.
          - `POST_ACCESS_DENIED` (403 FORBIDDEN): 본인이 작성한 게시글만 수정/삭제할 수 있습니다.
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          """
  )
  ResponseEntity<PostResponse> updatePost(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId,
      @Valid @RequestBody PostUpdateRequest request
  );

  @Operation(
      summary = "게시글 삭제",
      description = """
          본인이 작성한 게시글을 삭제합니다.

          ### 요청 파라미터
          Header

          - `Authorization: Bearer {accessToken}` (required, 작성자 본인만 가능)

          Path Variable

          - `post-id` (UUID, required): 삭제할 게시글 ID

          ### 응답 데이터
          `204 NO_CONTENT` (응답 본문 없음)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 삭제할 게시글의 `postId`를 `post-id`에 넣어 호출합니다.

          ### 유의 사항
          - 실제 DB Row를 지우지 않는 Soft Delete 방식이며, 삭제된 게시글은 목록/상세 조회에서 제외됩니다.
          - 작성자 본인만 삭제할 수 있습니다.
          - 게시글에 달린 댓글과 좋아요 데이터는 함께 삭제되지 않고 남지만, 삭제된 게시글에는 접근할 수 없습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 사용자입니다.
          - `POST_ACCESS_DENIED` (403 FORBIDDEN): 본인이 작성한 게시글만 수정/삭제할 수 있습니다.
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          """
  )
  ResponseEntity<Void> deletePost(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId
  );

  @Operation(
      summary = "게시글 좋아요 토글",
      description = """
          게시글 좋아요를 등록하거나 취소합니다. 하나의 API가 두 동작을 모두 처리합니다.

          ### 요청 파라미터
          Header

          - `Authorization: Bearer {accessToken}` (required)

          Path Variable

          - `post-id` (UUID, required): 좋아요를 토글할 게시글 ID

          Request Body 없음

          ### 응답 데이터
          `200 OK` + `PostLikeResponse`

          - `likeCount` (int): 토글이 반영된 게시글의 총 좋아요 수
          - `isLiked` (boolean): 토글이 반영된 요청자의 좋아요 여부
            - `true`: 이번 요청으로 좋아요가 등록됨
            - `false`: 이번 요청으로 좋아요가 취소됨

          응답 예시
          ```json
          {
            "likeCount": 4,
            "isLiked": true
          }
          ```

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 좋아요 버튼을 누를 때마다 동일한 요청을 보냅니다.
          3. 응답의 `isLiked`와 `likeCount`로 화면 상태를 바로 갱신하면 됩니다. 별도 재조회가 필요 없습니다.

          ### 유의 사항
          - 좋아요가 없으면 등록하고, 이미 좋아요한 상태면 취소합니다.
          - 좋아요 취소는 Soft Delete가 아닌 물리 삭제이므로 재등록이 항상 가능합니다.
          - 한 사용자는 한 게시글에 좋아요를 1회만 등록할 수 있습니다.
          - 본인이 작성한 게시글에도 좋아요를 누를 수 있습니다.
          - 버튼 연타 등으로 동일한 요청이 동시에 도착하면 `POST_LIKE_CONFLICT` (409)가 반환될 수 있습니다.
            이때는 실패가 아니라 이미 처리된 상태이므로, 게시글 정보를 다시 조회해 화면을 갱신하면 됩니다.
            클라이언트에서 좋아요 버튼에 debounce를 적용하면 대부분 예방됩니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 사용자입니다.
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          - `POST_LIKE_CONFLICT` (409 CONFLICT): 좋아요 요청이 동시에 처리되어 반영하지 못했습니다.
          """
  )
  ResponseEntity<PostLikeResponse> toggleLike(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId
  );
}
