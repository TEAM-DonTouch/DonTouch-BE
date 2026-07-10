package shop.dontouch.dontouch_be.domain.challenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.challenge.dto.request.ChallengeRequest;
import shop.dontouch.dontouch_be.domain.challenge.dto.response.ChallengeResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface ChallengeControllerDocs {

  @Operation(
      summary = "전체 챌린지 목록 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          ### 응답 데이터
          `List<ChallengeResponse>` — 각 챌린지마다 요청자 기준 `isJoined`, `progress`(0.0~1.0, 미참여 시 0.0)를 포함합니다.

          ### 예외 처리
          - `INTERNAL_SERVER_ERROR` (500 INTERNAL_SERVER_ERROR): 서버에 문제가 발생했습니다.
          """
  )
  ResponseEntity<List<ChallengeResponse>> getChallenges(
      @AuthenticationPrincipal CustomUserDetails currentUser
  );

  @Operation(
      summary = "내가 참여 중인 챌린지 목록 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          ### 응답 데이터
          `List<ChallengeResponse>` — 요청자가 참여한 챌린지만 포함, `isJoined`는 항상 true

          ### 예외 처리
          - `INTERNAL_SERVER_ERROR` (500 INTERNAL_SERVER_ERROR): 서버에 문제가 발생했습니다.
          """
  )
  ResponseEntity<List<ChallengeResponse>> getMyChallenges(
      @AuthenticationPrincipal CustomUserDetails currentUser
  );

  @Operation(
      summary = "챌린지 참여",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}`

          Path Variable

          - `challenge-id` (UUID, required): 참여할 챌린지 ID

          ### 응답 데이터
          `ChallengeResponse`

          ### 유의 사항
          - 이미 참여 중인 챌린지에 다시 요청해도 에러 없이 현재 상태를 그대로 반환합니다(멱등).
          - 최초 참여 시에만 `participantCount`가 1 증가합니다.

          ### 예외 처리
          - `CHALLENGE_NOT_FOUND` (404 NOT_FOUND): 챌린지를 찾을 수 없습니다.
          """
  )
  ResponseEntity<ChallengeResponse> joinChallenge(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "challenge-id") UUID challengeId
  );

  @Operation(
      summary = "[ADMIN] 챌린지 생성",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Request Body(JSON)

          - `title` (String, required): 챌린지 제목 (최대 50자, 공백 불가)
          - `description` (String, optional): 챌린지 설명 (최대 255자)
          - `targetValue` (Integer, required): 100% 달성 기준값 (양수)

          ### 응답 데이터
          `ChallengeResponse` (participantCount=0, isJoined=false, progress=0.0)

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<ChallengeResponse> createChallenge(
      @Valid @RequestBody ChallengeRequest request
  );
}
