package shop.dontouch.dontouch_be.domain.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.dontouch.dontouch_be.domain.community.dto.request.PostRequest;
import shop.dontouch.dontouch_be.domain.community.dto.response.PostLikeResponse;
import shop.dontouch.dontouch_be.domain.community.dto.response.PostResponse;
import shop.dontouch.dontouch_be.global.common.dto.PageResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface PostControllerDocs {

  @Operation(
      summary = "게시글 작성",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          Request Body(JSON)

          - `title` (String, required): 게시글 제목 (최대 30자, 공백 불가)
          - `content` (String, required): 게시글 내용 (최대 255자, 공백 불가)

          ### 응답 데이터
          - `postId` (UUID): 생성된 게시글 ID
          - `authorId`, `authorNickname`, `authorProfileImageUrl`: 작성자 정보
          - `viewCount`, `likeCount`, `commentCount`: 모두 0으로 초기화
          - `isLiked` (boolean): 항상 false (작성 직후이므로)
          - `createdAt`, `updatedAt`

          ### 예외 처리
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          """
  )
  ResponseEntity<PostResponse> createPost(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody PostRequest request
  );

  @Operation(
      summary = "게시글 목록 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          Query Parameter

          - `sort` (String, optional, 기본값 `latest`): `latest`(최신순) 또는 `popular`(인기순, likeCount desc)
          - `page` (int, optional, 기본값 0)
          - `size` (int, optional, 기본값 10)
          - `following` (boolean, optional, 기본값 false): true면 내가 팔로우한 유저의 게시글만 조회 (팔로잉 탭)

          ### 응답 데이터
          `PageResponse<PostResponse>` — `content`(게시글 목록), `page`, `size`, `totalElements`, `totalPages`, `hasNext`

          각 게시글은 `commentCount`, `isLiked`(요청자 기준)를 포함합니다.

          ### 유의 사항
          - 삭제된 게시글은 조회되지 않습니다.
          """
  )
  ResponseEntity<PageResponse<PostResponse>> getPosts(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @RequestParam(name = "sort", defaultValue = "latest") String sort,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size,
      @RequestParam(name = "following", defaultValue = "false") boolean following
  );

  @Operation(
      summary = "게시글 상세 조회",
      description = """
          ### 요청 파라미터
          Path Variable

          - `post-id` (UUID, required): 조회할 게시글 ID

          ### 응답 데이터
          `PostResponse` (commentCount, isLiked 포함)

          ### 유의 사항
          - 조회 시마다 `viewCount`가 1 증가합니다.
          - 삭제된 게시글은 조회되지 않습니다.

          ### 예외 처리
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          """
  )
  ResponseEntity<PostResponse> getPost(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId
  );

  @Operation(
      summary = "게시글 수정",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (작성자 본인만 가능)

          Path Variable

          - `post-id` (UUID, required): 수정할 게시글 ID

          Request Body(JSON)

          - `title` (String, required): 최대 30자
          - `content` (String, required): 최대 255자

          ### 응답 데이터
          `PostResponse`

          ### 예외 처리
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          - `POST_ACCESS_DENIED` (403 FORBIDDEN): 본인이 작성한 게시글만 수정/삭제할 수 있습니다.
          """
  )
  ResponseEntity<PostResponse> updatePost(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId,
      @Valid @RequestBody PostRequest request
  );

  @Operation(
      summary = "게시글 삭제",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (작성자 본인만 가능)

          Path Variable

          - `post-id` (UUID, required): 삭제할 게시글 ID

          ### 응답 데이터
          없음 (204 No Content)

          ### 유의 사항
          - 실제 DB Row 삭제가 아닌 Soft Delete 방식입니다.

          ### 예외 처리
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          - `POST_ACCESS_DENIED` (403 FORBIDDEN): 본인이 작성한 게시글만 수정/삭제할 수 있습니다.
          """
  )
  ResponseEntity<Void> deletePost(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId
  );

  @Operation(
      summary = "게시글 좋아요 토글",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          Path Variable

          - `post-id` (UUID, required): 좋아요를 토글할 게시글 ID

          ### 응답 데이터
          - `likeCount` (int): 토글 후 게시글의 총 좋아요 수
          - `isLiked` (boolean): 토글 후 현재 요청자의 좋아요 여부

          ### 유의 사항
          - 좋아요 상태가 아니면 좋아요를 등록하고, 이미 좋아요한 상태면 취소합니다.
          - 좋아요 취소는 Soft Delete가 아닌 물리 삭제이므로, 재좋아요가 항상 가능합니다.

          ### 예외 처리
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          """
  )
  ResponseEntity<PostLikeResponse> toggleLike(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId
  );
}
