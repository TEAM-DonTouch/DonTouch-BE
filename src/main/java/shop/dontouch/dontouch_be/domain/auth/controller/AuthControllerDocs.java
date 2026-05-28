package shop.dontouch.dontouch_be.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.auth.dto.request.LoginRequest;
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
          - `password` (String, required): 비밀번호 (8자 이상 100자 이하)
          - `nickname` (String, required): 닉네임 (최대 30자)
          - `age` (Integer, optional): 나이
          - `gender` (UserGender, optional): 성별
          - `jobType` (UserJobType, optional): 직업 유형
          - `region` (UserRegion, optional): 지역
          
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
          - `tokenType` (String): 토큰 타입
          - `user` (UserResponse): 가입된 유저 정보
          
          `user` 데이터는 아래 정보를 포함합니다.
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
          1. 회원가입에 필요한 이메일, 아이디, 비밀번호, 닉네임을 입력합니다.
          2. 선택값인 나이, 성별, 직업 유형, 지역을 입력할 수 있습니다.
          3. 요청 성공 시 회원가입된 유저 정보와 액세스 토큰을 반환합니다.
          
          ### 유의 사항
          - `email`은 올바른 이메일 형식이어야 합니다.
          - `loginId`는 4자 이상 30자 이하이어야 합니다.
          - `password`는 8자 이상 100자 이하이어야 합니다.
          - `nickname`은 공백일 수 없으며 최대 30자까지 입력 가능합니다.
          - 이미 사용 중인 아이디, 이메일, 닉네임은 사용할 수 없습니다.
          - 비밀번호는 암호화되어 저장됩니다.
          
          ### 예외 처리
          - `USER_LOGIN_ID_DUPLICATE` (409 CONFLICT): 이미 사용 중인 아이디입니다.
          - `USER_EMAIL_DUPLICATE` (409 CONFLICT): 이미 사용 중인 이메일입니다.
          - `USER_NICKNAME_DUPLICATE` (409 CONFLICT): 이미 사용 중인 닉네임입니다.
          - `USER_DUPLICATE` (409 CONFLICT): 이미 사용 중인 이메일 또는 닉네임입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<AuthResponse> signup(
      @Valid @RequestBody SignupRequest request
  );

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
          - `tokenType` (String): 토큰 타입
          - `user` (UserResponse): 로그인한 유저 정보
          
          `user` 데이터는 아래 정보를 포함합니다.
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
          1. 로그인 아이디와 비밀번호를 입력합니다.
          2. 인증 성공 시 액세스 토큰과 유저 정보를 반환합니다.
          3. 반환된 토큰은 이후 인증이 필요한 API 요청에 사용할 수 있습니다.
          
          ### 유의 사항
          - `loginId`와 `password`는 필수 입력값입니다.
          - 존재하지 않는 아이디이거나 비밀번호가 일치하지 않으면 로그인에 실패합니다.
          - 탈퇴 처리된 유저는 로그인할 수 없습니다.
          
          ### 예외 처리
          - `LOGIN_FAILED` (401 UNAUTHORIZED): 로그인에 실패했습니다.
          - `USER_ALREADY_WITHDRAWN` (409 CONFLICT): 이미 탈퇴한 사용자입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<AuthResponse> login(
      @Valid @RequestBody LoginRequest request
  );
}
