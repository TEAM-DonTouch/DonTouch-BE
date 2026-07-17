package shop.dontouch.dontouch_be.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.auth.dto.request.AppleOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.AppleOAuthSignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.GoogleOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.GoogleOAuthSignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.KakaoOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.KakaoOAuthSignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.response.AuthResponse;
import shop.dontouch.dontouch_be.domain.auth.dto.response.OAuthLoginResponse;

public interface OAuthControllerDocs {

  @Operation(
    summary = "구글 소셜 로그인",
    description = """
          ### 요청 파라미터
          Request Body(JSON)

          - `idToken` (String, required): 구글 로그인 후 클라이언트에서 발급받은 ID 토큰

          요청 예시
          ```json
          {
            "idToken": "google-id-token"
          }
          ```

          ### 응답 데이터

          #### 1. 이미 가입된 유저인 경우
          - `signupRequired` (Boolean): false
          - `accessToken` (String): 발급된 액세스 토큰
          - `refreshToken` (String): 발급된 리프레시 토큰
          - `user` (UserResponse): 로그인한 유저 정보
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

          응답 예시
          ```json
          {
            "signupRequired": false,
            "accessToken": "access-token",
            "refreshToken": "refresh-token",
            "user": {
              "id": "uuid",
              "email": "user@example.com",
              "nickname": "구글유저"
            }
          }
          ```

          #### 2. 회원가입이 필요한 유저인 경우
          - `signupRequired` (Boolean): true
          - `loginPlatform` (LoginPlatform): 소셜 로그인 플랫폼
          - `signupToken` (String): 소셜 회원가입에 사용할 임시 토큰

          응답 예시
          ```json
          {
            "signupRequired": true,
            "loginPlatform": "GOOGLE",
            "signupToken": "temporary-signup-token"
          }
          ```

          ### 사용 방법
          1. 클라이언트에서 구글 로그인을 진행합니다.
          2. 구글에서 발급받은 `idToken`을 Request Body에 담아 요청합니다.
          3. 이미 가입된 유저라면 `signupRequired=false`와 함께 토큰이 발급됩니다.
          4. 미가입 유저라면 `signupRequired=true`와 `signupToken`이 반환됩니다.
          5. 미가입 유저는 닉네임 입력 후 구글 소셜 회원가입 API에 `signupToken`과 `nickname`을 전달합니다.

          ### 유의 사항
          - `idToken`은 만료되지 않은 유효한 구글 ID 토큰이어야 합니다.
          - 구글 ID 토큰의 audience가 서버에 설정된 Google Client ID와 일치해야 합니다.
          - `signupToken`은 소셜 회원가입 API에서만 사용합니다.
          - 정지 또는 탈퇴 처리된 유저는 로그인할 수 없습니다.

          ### 예외 처리
          - `SOCIAL_LOGIN_FAILED` (401 UNAUTHORIZED): 소셜 로그인에 실패했습니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 계정입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<OAuthLoginResponse> googleLogin(@Valid @RequestBody GoogleOAuthLoginRequest request);

  @Operation(
    summary = "구글 소셜 회원가입",
    description = """
          ### 요청 파라미터
          Request Body(JSON)

          - `signupToken` (String, required): 구글 소셜 로그인 API에서 발급받은 회원가입용 임시 토큰
          - `nickname` (String, required): 닉네임 (최대 30자)

          요청 예시
          ```json
          {
            "signupToken": "temporary-signup-token",
            "nickname": "구글유저"
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
          1. 구글 소셜 로그인 API 응답에서 `signupRequired=true`인지 확인합니다.
          2. 함께 반환된 `signupToken`을 저장합니다.
          3. 사용자가 입력한 `nickname`과 `signupToken`을 Request Body에 담아 요청합니다.
          4. 요청 성공 시 회원가입과 로그인이 함께 처리됩니다.
          5. 발급된 `accessToken`과 `refreshToken`을 안전하게 저장합니다.

          ### 유의 사항
          - `signupToken`은 소셜 로그인 API에서 발급된 유효한 토큰이어야 합니다.
          - `signupToken`이 만료되었거나 이미 사용된 경우 회원가입할 수 없습니다.
          - 이미 가입된 이메일 또는 닉네임은 사용할 수 없습니다.
          - 동일한 구글 계정으로 중복 가입할 수 없습니다.
          - `nickname`은 공백만으로 구성될 수 없으며 최대 30자입니다.

          ### 예외 처리
          - `SOCIAL_SIGNUP_TOKEN_INVALID` (401 UNAUTHORIZED): 소셜 회원가입 토큰이 유효하지 않습니다.
          - `USER_EMAIL_DUPLICATE` (409 CONFLICT): 이미 사용 중인 이메일입니다.
          - `USER_NICKNAME_DUPLICATE` (409 CONFLICT): 이미 사용 중인 닉네임입니다.
          - `USER_DUPLICATE` (409 CONFLICT): 이미 가입된 소셜 계정입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<AuthResponse> googleSignup(@Valid @RequestBody GoogleOAuthSignupRequest request);

  @Operation(
    summary = "카카오 소셜 로그인",
    description = """
          ### 요청 파라미터
          Request Body(JSON)

          - `code` (String, required): 카카오 로그인 후 발급받은 인가 코드

          요청 예시
          ```json
          {
            "code": "kakao-authorization-code"
          }
          ```

          ### 응답 데이터

          #### 1. 이미 가입된 유저인 경우
          - `signupRequired` (Boolean): false
          - `accessToken` (String): 발급된 액세스 토큰
          - `refreshToken` (String): 발급된 리프레시 토큰
          - `user` (UserResponse): 로그인한 유저 정보

          #### 2. 회원가입이 필요한 유저인 경우
          - `signupRequired` (Boolean): true
          - `loginPlatform` (LoginPlatform): 소셜 로그인 플랫폼
          - `signupToken` (String): 소셜 회원가입에 사용할 임시 토큰

          응답 예시
          ```json
          {
            "signupRequired": true,
            "loginPlatform": "KAKAO",
            "signupToken": "temporary-signup-token"
          }
          ```

          ### 사용 방법
          1. 클라이언트에서 카카오 로그인 페이지로 사용자를 이동시킵니다.
          2. 카카오 로그인 성공 후 redirect URI로 전달된 `code` 값을 확인합니다.
          3. 전달받은 `code`를 Request Body에 담아 요청합니다.
          4. 이미 가입된 유저라면 `signupRequired=false`와 함께 토큰이 발급됩니다.
          5. 미가입 유저라면 `signupRequired=true`와 `signupToken`이 반환됩니다.
          6. 미가입 유저는 닉네임 입력 후 카카오 소셜 회원가입 API에 `signupToken`과 `nickname`을 전달합니다.

          ### 유의 사항
          - 카카오 인가 코드는 1회용이며, 재사용할 수 없습니다.
          - 서버에 설정된 카카오 redirect URI와 인가 코드 발급 시 사용한 redirect URI가 일치해야 합니다.
          - `signupToken`은 소셜 회원가입 API에서만 사용합니다.
          - 정지 또는 탈퇴 처리된 유저는 로그인할 수 없습니다.

          ### 예외 처리
          - `SOCIAL_LOGIN_FAILED` (401 UNAUTHORIZED): 소셜 로그인에 실패했습니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 계정입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<OAuthLoginResponse> kakaoLogin(@Valid @RequestBody KakaoOAuthLoginRequest request);

  @Operation(
    summary = "카카오 소셜 회원가입",
    description = """
          ### 요청 파라미터
          Request Body(JSON)

          - `signupToken` (String, required): 카카오 소셜 로그인 API에서 발급받은 회원가입용 임시 토큰
          - `nickname` (String, required): 닉네임 (최대 30자)

          요청 예시
          ```json
          {
            "signupToken": "temporary-signup-token",
            "nickname": "카카오유저"
          }
          ```

          ### 응답 데이터
          - `accessToken` (String): 발급된 액세스 토큰
          - `refreshToken` (String): 발급된 리프레시 토큰
          - `user` (UserResponse): 가입된 유저 정보

          ### 사용 방법
          1. 카카오 소셜 로그인 API 응답에서 `signupRequired=true`인지 확인합니다.
          2. 함께 반환된 `signupToken`을 저장합니다.
          3. 사용자가 입력한 `nickname`과 `signupToken`을 Request Body에 담아 요청합니다.
          4. 요청 성공 시 회원가입과 로그인이 함께 처리됩니다.
          5. 발급된 `accessToken`과 `refreshToken`을 안전하게 저장합니다.

          ### 유의 사항
          - `signupToken`은 소셜 로그인 API에서 발급된 유효한 토큰이어야 합니다.
          - `signupToken`이 만료되었거나 이미 사용된 경우 회원가입할 수 없습니다.
          - 이미 가입된 이메일 또는 닉네임은 사용할 수 없습니다.
          - 동일한 카카오 계정으로 중복 가입할 수 없습니다.
          - `nickname`은 공백만으로 구성될 수 없으며 최대 30자입니다.

          ### 예외 처리
          - `SOCIAL_SIGNUP_TOKEN_INVALID` (401 UNAUTHORIZED): 소셜 회원가입 토큰이 유효하지 않습니다.
          - `USER_EMAIL_DUPLICATE` (409 CONFLICT): 이미 사용 중인 이메일입니다.
          - `USER_NICKNAME_DUPLICATE` (409 CONFLICT): 이미 사용 중인 닉네임입니다.
          - `USER_DUPLICATE` (409 CONFLICT): 이미 가입된 소셜 계정입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<AuthResponse> kakaoSignup(@Valid @RequestBody KakaoOAuthSignupRequest request);

  @Operation(
    summary = "애플 소셜 로그인",
    description = """
          ### 요청 파라미터
          Request Body(JSON)

          - `identityToken` (String, required): 애플 로그인 후 클라이언트에서 발급받은 identityToken

          요청 예시
          ```json
          {
            "identityToken": "apple-identity-token"
          }
          ```

          ### 응답 데이터

          #### 1. 이미 가입된 유저인 경우
          - `signupRequired` (Boolean): false
          - `accessToken` (String): 발급된 액세스 토큰
          - `refreshToken` (String): 발급된 리프레시 토큰
          - `user` (UserResponse): 로그인한 유저 정보

          #### 2. 회원가입이 필요한 유저인 경우
          - `signupRequired` (Boolean): true
          - `loginPlatform` (LoginPlatform): 소셜 로그인 플랫폼
          - `signupToken` (String): 소셜 회원가입에 사용할 임시 토큰

          응답 예시
          ```json
          {
            "signupRequired": true,
            "loginPlatform": "APPLE",
            "signupToken": "temporary-signup-token"
          }
          ```

          ### 사용 방법
          1. 클라이언트에서 애플 로그인을 진행합니다.
          2. 애플에서 발급받은 `identityToken`을 Request Body에 담아 요청합니다.
          3. 이미 가입된 유저라면 `signupRequired=false`와 함께 토큰이 발급됩니다.
          4. 미가입 유저라면 `signupRequired=true`와 `signupToken`이 반환됩니다.
          5. 미가입 유저는 닉네임 입력 후 애플 소셜 회원가입 API에 `signupToken`과 `nickname`을 전달합니다.

          ### 유의 사항
          - `identityToken`은 만료되지 않은 유효한 애플 identityToken이어야 합니다.
          - 애플 identityToken의 audience가 서버에 설정된 Apple Client ID와 일치해야 합니다.
          - `signupToken`은 소셜 회원가입 API에서만 사용합니다.
          - 정지 또는 탈퇴 처리된 유저는 로그인할 수 없습니다.

          ### 예외 처리
          - `SOCIAL_LOGIN_FAILED` (401 UNAUTHORIZED): 소셜 로그인에 실패했습니다.
          - `USER_SUSPENDED` (403 FORBIDDEN): 정지된 계정입니다.
          - `USER_WITHDRAWN` (403 FORBIDDEN): 탈퇴한 계정입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<OAuthLoginResponse> appleLogin(@Valid @RequestBody AppleOAuthLoginRequest request);

  @Operation(
    summary = "애플 소셜 회원가입",
    description = """
          ### 요청 파라미터
          Request Body(JSON)

          - `signupToken` (String, required): 애플 소셜 로그인 API에서 발급받은 회원가입용 임시 토큰
          - `nickname` (String, required): 닉네임 (최대 30자)

          요청 예시
          ```json
          {
            "signupToken": "temporary-signup-token",
            "nickname": "애플유저"
          }
          ```

          ### 응답 데이터
          - `accessToken` (String): 발급된 액세스 토큰
          - `refreshToken` (String): 발급된 리프레시 토큰
          - `user` (UserResponse): 가입된 유저 정보

          ### 사용 방법
          1. 애플 소셜 로그인 API 응답에서 `signupRequired=true`인지 확인합니다.
          2. 함께 반환된 `signupToken`을 저장합니다.
          3. 사용자가 입력한 `nickname`과 `signupToken`을 Request Body에 담아 요청합니다.
          4. 요청 성공 시 회원가입과 로그인이 함께 처리됩니다.
          5. 발급된 `accessToken`과 `refreshToken`을 안전하게 저장합니다.

          ### 유의 사항
          - `signupToken`은 소셜 로그인 API에서 발급된 유효한 토큰이어야 합니다.
          - `signupToken`이 만료되었거나 이미 사용된 경우 회원가입할 수 없습니다.
          - 이미 가입된 이메일 또는 닉네임은 사용할 수 없습니다.
          - 동일한 애플 계정으로 중복 가입할 수 없습니다.
          - `nickname`은 공백만으로 구성될 수 없으며 최대 30자입니다.

          ### 예외 처리
          - `SOCIAL_SIGNUP_TOKEN_INVALID` (401 UNAUTHORIZED): 소셜 회원가입 토큰이 유효하지 않습니다.
          - `USER_EMAIL_DUPLICATE` (409 CONFLICT): 이미 사용 중인 이메일입니다.
          - `USER_NICKNAME_DUPLICATE` (409 CONFLICT): 이미 사용 중인 닉네임입니다.
          - `USER_DUPLICATE` (409 CONFLICT): 이미 가입된 소셜 계정입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<AuthResponse> appleSignup(@Valid @RequestBody AppleOAuthSignupRequest request);
}