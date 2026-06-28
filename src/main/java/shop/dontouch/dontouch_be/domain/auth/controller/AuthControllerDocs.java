package shop.dontouch.dontouch_be.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.auth.dto.request.LoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.RefreshTokenRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.SignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.response.AuthResponse;

public interface AuthControllerDocs {

  @Operation(
      summary = "회원가입",
      description = """
          ### 요청 파라미터
          Request Body(JSON)

          - `email` (String, required): 이메일
          - `loginId` (String, required): 로그인 아이디 (4자 이상 30자 이하)
          - `password` (String, required): 비밀번호 (8자 이상 72자 이하)
          - `nickname` (String, required): 닉네임 (최대 30자)
          - `age` (Integer, optional): 나이 (1 이상)
          - `gender` (UserGender, optional): 성별
            - `NOT_SELECTED`, `MALE`, `FEMALE`
          - `jobType` (UserJobType, optional): 직업 유형
            - `EMPLOYED`, `SELF_EMPLOYED`, `STUDENT`, `UNEMPLOYED`, `OTHER`
          - `region` (UserRegion, optional): 지역
            - `SEOUL`, `BUSAN`, `DAEGU`, `INCHEON`, `GWANGJU`, `DAEJEON`, `ULSAN`, `SEJONG`, `GYEONGGI`, `GANGWON`, `CHUNGBUK`, `CHUNGNAM`, `JEONBUK`, `JEONNAM`, `GYEONGBUK`, `GYEONGNAM`, `JEJU`

          요청 예시
          ```json
          {
            "email": "user@example.com",
            "loginId": "testuser01",
            "password": "password1234",
            "nickname": "테스트유저",
            "age": 25,
            "gender": "NOT_SELECTED",
            "jobType": "OTHER",
            "region": "SEOUL"
          }
          ```

          ### 응답 데이터
          - `accessToken` (String): 발급된 액세스 토큰
          - `refreshToken` (String): 발급된 리프레시 토큰
          - `user` (UserResponse): 가입된 유저 정보
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
          1. 이메일, 아이디, 비밀번호, 닉네임을 필수로 입력합니다.
          2. 선택 항목(나이, 성별, 직업, 지역)을 원하는 경우 입력합니다.
          3. 요청 성공 시 `accessToken`과 `refreshToken`을 안전하게 저장합니다.
          4. 이후 API 요청 시 `Authorization: Bearer {accessToken}` 헤더를 사용합니다.

          ### 유의 사항
          - `email`은 올바른 이메일 형식이어야 합니다.
          - `loginId`는 4자 이상 30자 이하이어야 합니다.
          - `password`는 8자 이상 72자 이하이어야 합니다.
          - `nickname`은 공백만으로 구성될 수 없으며 최대 30자입니다.
          - 이미 사용 중인 아이디, 이메일, 닉네임은 사용할 수 없습니다.
          - 비밀번호는 암호화되어 저장됩니다.
          - 미입력 선택 항목의 기본값: `gender` → `NOT_SELECTED`, `jobType` → `OTHER`, `region` → `SEOUL`

          ### 예외 처리
          - `USER_LOGIN_ID_DUPLICATE` (409 CONFLICT): 이미 사용 중인 아이디입니다.
          - `USER_EMAIL_DUPLICATE` (409 CONFLICT): 이미 사용 중인 이메일입니다.
          - `USER_NICKNAME_DUPLICATE` (409 CONFLICT): 이미 사용 중인 닉네임입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request);

  @Operation(
      summary = "로그인",
      description = """
          ### 요청 파라미터
          Request Body(JSON)

          - `loginId` (String, required): 로그인 아이디
          - `password` (String, required): 비밀번호

          요청 예시
          ```json
          {
            "loginId": "testuser01",
            "password": "password1234"
          }
          ```

          ### 응답 데이터
          - `accessToken` (String): 발급된 액세스 토큰
          - `refreshToken` (String): 발급된 리프레시 토큰
          - `user` (UserResponse): 로그인한 유저 정보

          ### 사용 방법
          1. 아이디와 비밀번호를 입력합니다.
          2. 요청 성공 시 `accessToken`과 `refreshToken`을 안전하게 저장합니다.
          3. 이후 인증이 필요한 API 요청 시 `Authorization: Bearer {accessToken}` 헤더를 사용합니다.

          ### 유의 사항
          - `loginId`와 `password`는 필수 입력값입니다.
          - 존재하지 않는 아이디이거나 비밀번호가 일치하지 않으면 로그인에 실패합니다.
          - 탈퇴 처리된 유저는 로그인할 수 없습니다.

          ### 예외 처리
          - `LOGIN_FAILED` (401 UNAUTHORIZED): 아이디 또는 비밀번호가 일치하지 않습니다.
          - `USER_ALREADY_WITHDRAWN` (409 CONFLICT): 이미 탈퇴한 사용자입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request);

  @Operation(
      summary = "토큰 재발급",
      description = """
          ### 요청 파라미터
          Request Body(JSON)

          - `refreshToken` (String, required): 보유 중인 리프레시 토큰

          요청 예시
          ```json
          {
            "refreshToken": "v9JmYQq4vWq2pQ9y3K6z8sFh0u2nLxA1bC4dE5fG6hI"
          }
          ```

          ### 응답 데이터
          - `accessToken` (String): 새로 발급된 액세스 토큰
          - `refreshToken` (String): 새로 발급된 리프레시 토큰 (Token Rotation — 기존 토큰 즉시 폐기)
          - `user` (UserResponse): 유저 정보

          ### 사용 방법
          1. 저장된 `refreshToken`을 Request Body에 담아 요청합니다.
          2. 요청 성공 시 새로 발급된 `accessToken`과 `refreshToken`을 저장합니다.
          3. 기존 `refreshToken`은 즉시 무효화되므로 반드시 새 토큰으로 교체합니다.

          ### 유의 사항
          - 재발급 후 이전 리프레시 토큰은 즉시 무효화됩니다.
          - 새로 발급된 리프레시 토큰을 저장해야 다음 재발급이 가능합니다.
          - 만료된 리프레시 토큰은 Redis에서 자동 삭제되며 재발급이 불가합니다.

          ### 예외 처리
          - `REFRESH_TOKEN_NOT_FOUND` (401 UNAUTHORIZED): 존재하지 않거나 만료된 리프레시 토큰입니다.
          - `USER_NOT_FOUND` (404 NOT_FOUND): 유저 정보를 찾을 수 없습니다.
          """
  )
  ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request);

  @Operation(
      summary = "로그아웃",
      description = """
          ### 요청 파라미터
          Header: `Authorization: Bearer {accessToken}` (필수)

          Request Body(JSON)

          - `refreshToken` (String, required): 보유 중인 리프레시 토큰

          요청 예시
          ```json
          {
            "refreshToken": "v9JmYQq4vWq2pQ9y3K6z8sFh0u2nLxA1bC4dE5fG6hI"
          }
          ```

          ### 응답 데이터
          없음 (204 No Content)

          ### 사용 방법
          1. `Authorization` 헤더에 액세스 토큰을 담아 요청합니다.
          2. 보유 중인 `refreshToken`을 Request Body에 포함합니다.
          3. 요청 성공 시 서버의 리프레시 토큰이 삭제되며 이후 재발급이 불가합니다.
          4. 클라이언트에서도 `accessToken`과 `refreshToken`을 함께 폐기합니다.

          ### 유의 사항
          - 액세스 토큰이 유효해야 요청할 수 있습니다.
          - 서버의 리프레시 토큰이 삭제되어 이후 토큰 재발급이 불가합니다.
          - 액세스 토큰은 만료 시까지 서버에서 유효하므로 클라이언트에서도 반드시 폐기합니다.

          ### 예외 처리
          - `REFRESH_TOKEN_NOT_FOUND` (401 UNAUTHORIZED): 존재하지 않거나 만료된 리프레시 토큰입니다.
          - `TOKEN_INVALID` (401 UNAUTHORIZED): 유효하지 않은 액세스 토큰입니다.
          """
  )
  ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request);
}
