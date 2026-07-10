package shop.dontouch.dontouch_be.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import shop.dontouch.dontouch_be.domain.user.dto.response.FollowResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface FollowControllerDocs {

  @Operation(
      summary = "팔로우 토글",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          Path Variable

          - `user-id` (UUID, required): 팔로우/언팔로우할 대상 유저 ID

          ### 응답 데이터
          - `userId` (UUID): 대상 유저 ID
          - `isFollowing` (boolean): 토글 후 팔로우 상태

          ### 유의 사항
          - 팔로우 상태가 아니면 팔로우를 등록하고, 이미 팔로우한 상태면 취소합니다.
          - 언팔로우는 물리 삭제이므로 재팔로우가 항상 가능합니다.
          - 자기 자신은 팔로우할 수 없습니다.

          ### 예외 처리
          - `FOLLOW_SELF_NOT_ALLOWED` (400 BAD_REQUEST): 자기 자신은 팔로우할 수 없습니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          """
  )
  ResponseEntity<FollowResponse> toggleFollow(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "user-id") UUID userId
  );
}
