package shop.dontouch.dontouch_be.domain.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.community.dto.request.CommentRequest;
import shop.dontouch.dontouch_be.domain.community.dto.response.CommentResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface CommentControllerDocs {

  @Operation(
      summary = "댓글 작성",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          Path Variable

          - `post-id` (UUID, required): 댓글을 작성할 게시글 ID

          Request Body(JSON)

          - `content` (String, required): 댓글 내용 (최대 100자, 공백 불가)

          ### 응답 데이터
          `CommentResponse`

          ### 유의 사항
          - 댓글 작성 시 게시글의 `commentCount`가 1 증가합니다.

          ### 예외 처리
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          """
  )
  ResponseEntity<CommentResponse> createComment(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId,
      @Valid @RequestBody CommentRequest request
  );

  @Operation(
      summary = "댓글 목록 조회",
      description = """
          ### 요청 파라미터
          Path Variable

          - `post-id` (UUID, required): 조회할 게시글 ID

          ### 응답 데이터
          `List<CommentResponse>` (createdAt 오름차순, 페이징 없음)

          ### 예외 처리
          - `POST_NOT_FOUND` (404 NOT_FOUND): 게시글을 찾을 수 없습니다.
          """
  )
  ResponseEntity<List<CommentResponse>> getComments(
      @PathVariable(name = "post-id") UUID postId
  );

  @Operation(
      summary = "댓글 삭제",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (작성자 본인만 가능)

          Path Variable

          - `post-id` (UUID, required): 게시글 ID
          - `comment-id` (UUID, required): 삭제할 댓글 ID

          ### 응답 데이터
          없음 (204 No Content)

          ### 유의 사항
          - Soft Delete 방식이며, 삭제 시 게시글의 `commentCount`가 1 감소합니다.

          ### 예외 처리
          - `COMMENT_NOT_FOUND` (404 NOT_FOUND): 댓글을 찾을 수 없습니다.
          - `COMMENT_ACCESS_DENIED` (403 FORBIDDEN): 본인이 작성한 댓글만 삭제할 수 있습니다.
          """
  )
  ResponseEntity<Void> deleteComment(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId,
      @PathVariable(name = "comment-id") UUID commentId
  );
}
