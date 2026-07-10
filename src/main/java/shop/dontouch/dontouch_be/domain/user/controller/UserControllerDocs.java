package shop.dontouch.dontouch_be.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserCreateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserRoleUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserSettingsUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserStatusUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserResponse;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserSettingsResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

public interface UserControllerDocs {

  // ==================== 본인 전용 ====================

  @Operation(
      summary = "내 정보 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          ### 응답 데이터
          - `id` (UUID): 유저 ID
          - `email` (String): 이메일
          - `nickname` (String): 닉네임
          - `profileImageUrl` (String): 프로필 이미지 URL
          - `userRole` (UserRole): 유저 권한
          - `age` (Integer): 나이
          - `gender` (UserGender): 성별
          - `userJobType` (UserJobType): 직업 유형
          - `userRegion` (UserRegion): 지역
          - `userStatus` (UserStatus): 유저 상태

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 별도의 요청값 없이 호출합니다.
          3. 요청 성공 시 본인 유저 정보를 반환합니다.

          ### 유의 사항
          - 액세스 토큰이 유효해야 요청할 수 있습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          """
  )
  ResponseEntity<UserResponse> getMe(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser
  );

  @Operation(
      summary = "내 정보 수정",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          Request Body(JSON) — 수정할 필드만 포함

          - `nickname` (String, optional): 닉네임 (최대 30자)
          - `profileImageUrl` (String, optional): 프로필 이미지 URL (최대 500자)
          - `age` (Integer, optional): 나이 (1 이상)
          - `gender` (UserGender, optional): 성별
            - `NOT_SELECTED`, `MALE`, `FEMALE`
          - `userJobType` (UserJobType, optional): 직업 유형
            - `EMPLOYED`, `SELF_EMPLOYED`, `STUDENT`, `UNEMPLOYED`, `OTHER`
          - `userRegion` (UserRegion, optional): 지역
            - `SEOUL`, `BUSAN`, `DAEGU`, `INCHEON`, `GWANGJU`, `DAEJEON`, `ULSAN`, `SEJONG`, `GYEONGGI`, `GANGWON`, `CHUNGBUK`, `CHUNGNAM`, `JEONBUK`, `JEONNAM`, `GYEONGBUK`, `GYEONGNAM`, `JEJU`

          요청 예시
          ```json
          {
            "nickname": "새닉네임",
            "age": 27
          }
          ```

          ### 응답 데이터
          수정된 유저 정보 (UserResponse)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 수정할 필드만 Request Body에 포함하여 요청합니다.
          3. 요청 성공 시 변경된 유저 정보를 반환합니다.

          ### 유의 사항
          - 전달하지 않은 필드는 기존 값이 유지됩니다.
          - `nickname`은 공백만으로 구성될 수 없으며 최대 30자입니다.
          - 다른 유저가 사용 중인 닉네임으로 변경할 수 없습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          - `USER_NICKNAME_DUPLICATE` (409 CONFLICT): 이미 사용 중인 닉네임입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<UserResponse> updateMe(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody UserUpdateRequest request
  );

  @Operation(
      summary = "회원 탈퇴",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          ### 응답 데이터
          없음 (204 No Content)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 `Bearer {accessToken}`을 입력합니다.
          2. 별도의 요청값 없이 호출합니다.
          3. 요청 성공 시 상태가 `WITHDRAWN`으로 변경됩니다.

          ### 유의 사항
          - 실제 DB 삭제가 아닌 상태를 탈퇴(`WITHDRAWN`)로 변경합니다.
          - 탈퇴 후 동일 아이디로 로그인이 불가합니다.
          - 이미 탈퇴한 계정은 재탈퇴할 수 없습니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          - `USER_ALREADY_WITHDRAWN` (409 CONFLICT): 이미 탈퇴한 사용자입니다.
          """
  )
  ResponseEntity<Void> deleteMe(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser
  );

  @Operation(
      summary = "내 알림/보안 설정 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          ### 응답 데이터
          - `pushNotificationEnabled` (boolean): 푸시 알림 수신 여부
          - `biometricLoginEnabled` (boolean): 생체 인증 로그인 사용 여부
          - `updatedAt` (LocalDateTime)

          ### 유의 사항
          - 설정이 아직 없는 경우 기본값(`pushNotificationEnabled=true`, `biometricLoginEnabled=false`)으로 자동 생성 후 반환합니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          """
  )
  ResponseEntity<UserSettingsResponse> getMySettings(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser
  );

  @Operation(
      summary = "내 알림/보안 설정 수정",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          Request Body(JSON) — 변경할 필드만 포함

          - `pushNotificationEnabled` (boolean, optional)
          - `biometricLoginEnabled` (boolean, optional)

          요청 예시
          ```json
          {
            "biometricLoginEnabled": true
          }
          ```

          ### 응답 데이터
          변경된 설정 정보 (UserSettingsResponse)

          ### 유의 사항
          - 전달하지 않은 필드는 기존 값이 유지됩니다.
          - 설정이 아직 없는 경우 기본값으로 생성한 뒤 전달된 필드를 적용합니다.

          ### 예외 처리
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 토큰입니다.
          """
  )
  ResponseEntity<UserSettingsResponse> updateMySettings(
      @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody UserSettingsUpdateRequest request
  );

  // ==================== ADMIN 전용 ====================

  @Operation(
      summary = "[ADMIN] 유저 직접 생성",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Request Body(JSON)

          - `loginId` (String, required): 로그인 아이디 (4자 이상 30자 이하)
          - `password` (String, required): 비밀번호 (8자 이상 72자 이하)
          - `email` (String, required): 이메일
          - `nickname` (String, required): 닉네임 (최대 30자)
          - `profileImageUrl` (String, optional): 프로필 이미지 URL (최대 500자)
          - `age` (Integer, optional): 나이 (1 이상)
          - `gender` (UserGender, optional): 성별
            - `NOT_SELECTED`, `MALE`, `FEMALE`
          - `userJobType` (UserJobType, optional): 직업 유형
            - `EMPLOYED`, `SELF_EMPLOYED`, `STUDENT`, `UNEMPLOYED`, `OTHER`
          - `userRegion` (UserRegion, optional): 지역
            - `SEOUL`, `BUSAN`, `DAEGU`, `INCHEON`, `GWANGJU`, `DAEJEON`, `ULSAN`, `SEJONG`, `GYEONGGI`, `GANGWON`, `CHUNGBUK`, `CHUNGNAM`, `JEONBUK`, `JEONNAM`, `GYEONGBUK`, `GYEONGNAM`, `JEJU`

          요청 예시
          ```json
          {
            "loginId": "adminuser01",
            "password": "adminpass1234",
            "email": "admin@example.com",
            "nickname": "어드민유저",
            "age": 30,
            "gender": "MALE",
            "userJobType": "EMPLOYED",
            "userRegion": "SEOUL"
          }
          ```

          ### 응답 데이터
          생성된 유저 정보 (UserResponse)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 ADMIN 계정의 `Bearer {accessToken}`을 입력합니다.
          2. 생성할 유저 정보를 Request Body에 포함하여 요청합니다.
          3. 요청 성공 시 생성된 유저 정보를 반환합니다.

          ### 유의 사항
          - `gender` 미입력 시 `NOT_SELECTED`가 기본값으로 저장됩니다.
          - `userJobType` 미입력 시 `OTHER`가 기본값으로 저장됩니다.
          - `userRegion` 미입력 시 `SEOUL`이 기본값으로 저장됩니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `USER_LOGIN_ID_DUPLICATE` (409 CONFLICT): 이미 사용 중인 아이디입니다.
          - `USER_EMAIL_DUPLICATE` (409 CONFLICT): 이미 사용 중인 이메일입니다.
          - `USER_NICKNAME_DUPLICATE` (409 CONFLICT): 이미 사용 중인 닉네임입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request);

  @Operation(
      summary = "[ADMIN] 전체 유저 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          ### 응답 데이터
          전체 유저 목록 (List<UserResponse>)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 ADMIN 계정의 `Bearer {accessToken}`을 입력합니다.
          2. 별도의 요청값 없이 호출합니다.
          3. 요청 성공 시 전체 유저 목록을 반환합니다.

          ### 유의 사항
          - 삭제된 유저 데이터는 조회되지 않습니다.
          - `deletedAt IS NULL` 조건이 적용된 데이터만 조회됩니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          """
  )
  ResponseEntity<List<UserResponse>> getAllUsers();

  @Operation(
      summary = "[ADMIN] 특정 유저 조회",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Path Variable

          - `user-id` (UUID, required): 조회할 유저 ID

          요청 예시
          ```text
          /api/users/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
          ```

          ### 응답 데이터
          유저 정보 (UserResponse)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 ADMIN 계정의 `Bearer {accessToken}`을 입력합니다.
          2. 조회할 유저 ID를 Path Variable로 전달합니다.
          3. 요청 성공 시 해당 유저 정보를 반환합니다.

          ### 유의 사항
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          """
  )
  ResponseEntity<UserResponse> getUser(@PathVariable(name = "user-id") UUID userId);

  @Operation(
      summary = "[ADMIN] 유저 상태 변경",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Path Variable

          - `user-id` (UUID, required): 상태를 변경할 유저 ID

          Request Body(JSON)

          - `userStatus` (UserStatus, required): 변경할 상태
            - `ACTIVE`: 활성
            - `DORMANT`: 휴면
            - `SUSPENDED`: 정지
            - `WITHDRAWN`: 탈퇴

          요청 예시
          ```json
          {
            "userStatus": "INACTIVE"
          }
          ```

          ### 응답 데이터
          변경된 유저 정보 (UserResponse)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 ADMIN 계정의 `Bearer {accessToken}`을 입력합니다.
          2. 상태를 변경할 유저 ID를 Path Variable로 전달합니다.
          3. 변경할 상태값을 Request Body에 포함하여 요청합니다.
          4. 요청 성공 시 변경된 유저 정보를 반환합니다.

          ### 유의 사항
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<UserResponse> updateUserStatus(
      @PathVariable(name = "user-id") UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest request
  );

  @Operation(
      summary = "[ADMIN] 유저 권한 변경",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (ADMIN 권한 필요)

          Path Variable

          - `user-id` (UUID, required): 권한을 변경할 유저 ID

          Request Body(JSON)

          - `userRole` (UserRole, required): 변경할 권한
            - `GENERAL_USER`: 일반 유저
            - `ADMIN`: 관리자

          요청 예시
          ```json
          {
            "userRole": "ADMIN"
          }
          ```

          ### 응답 데이터
          변경된 유저 정보 (UserResponse)

          ### 사용 방법
          1. Swagger UI 상단 `Authorize` 버튼에 ADMIN 계정의 `Bearer {accessToken}`을 입력합니다.
          2. 권한을 변경할 유저 ID를 Path Variable로 전달합니다.
          3. 변경할 권한값을 Request Body에 포함하여 요청합니다.
          4. 요청 성공 시 변경된 유저 정보를 반환합니다.

          ### 유의 사항
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.

          ### 예외 처리
          - `ACCESS_DENIED` (403 FORBIDDEN): ADMIN 권한이 필요합니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<UserResponse> updateUserRole(
      @PathVariable(name = "user-id") UUID userId,
      @Valid @RequestBody UserRoleUpdateRequest request
  );
}
