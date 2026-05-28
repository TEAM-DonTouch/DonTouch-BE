package shop.dontouch.dontouch_be.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserCreateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserRoleUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserStatusUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserResponse;

public interface UserControllerDocs {

  @Operation(
      summary = "관리자용 유저 생성",
      description = """
          ### 요청 파라미터
          Request Body(JSON)
          
          - `loginId` (String, required): 로그인 아이디 (4자 이상 30자 이하)
          - `password` (String, required): 비밀번호 (8자 이상 100자 이하)
          - `email` (String, required): 이메일
          - `nickname` (String, required): 닉네임
          - `profileImageUrl` (String, optional): 프로필 이미지 URL
          - `age` (Integer, optional): 나이
          - `gender` (UserGender, optional): 성별
          - `userJobType` (UserJobType, optional): 직업 유형
          - `userRegion` (UserRegion, optional): 지역
          
          요청 예시
          ```json
          {
            "loginId": "admincreated01",
            "password": "password1234",
            "email": "admincreated@example.com",
            "nickname": "관리자생성유저",
            "profileImageUrl": "https://example.com/profile.png",
            "age": 25,
            "gender": "NOT_SELECTED",
            "userJobType": "OTHER",
            "userRegion": "SEOUL"
          }
          ```
          
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
          1. 관리자 권한으로 생성할 유저 정보를 입력합니다.
          2. 필수값인 아이디, 비밀번호, 이메일, 닉네임을 입력합니다.
          3. 요청 성공 시 생성된 유저 정보를 반환합니다.
          
          ### 유의 사항
          - 현재 엔드포인트는 `/api/users/admin`입니다.
          - `loginId`, `email`, `nickname`은 중복될 수 없습니다.
          - 비밀번호는 암호화되어 저장됩니다.
          - `gender` 미입력 시 `NOT_SELECTED`가 기본값으로 저장됩니다.
          - `userJobType` 미입력 시 `OTHER`가 기본값으로 저장됩니다.
          - `userRegion` 미입력 시 `SEOUL`이 기본값으로 저장됩니다.
          
          ### 예외 처리
          - `USER_LOGIN_ID_DUPLICATE` (409 CONFLICT): 이미 사용 중인 아이디입니다.
          - `USER_EMAIL_DUPLICATE` (409 CONFLICT): 이미 사용 중인 이메일입니다.
          - `USER_NICKNAME_DUPLICATE` (409 CONFLICT): 이미 사용 중인 닉네임입니다.
          - `USER_DUPLICATE` (409 CONFLICT): 이미 사용 중인 이메일 또는 닉네임입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<UserResponse> createUser(
      @Valid @RequestBody UserCreateRequest request
  );

  @Operation(
      summary = "유저 단건 조회",
      description = """
          ### 요청 파라미터
          Path Variable
          
          - `user-id` (UUID, required): 조회할 유저 ID
          
          요청 예시
          ```text
          /api/users/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
          ```
          
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
          1. 조회할 유저 ID를 Path Variable로 전달합니다.
          2. 해당 유저 정보를 반환합니다.
          
          ### 유의 사항
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.
          
          ### 예외 처리
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          """
  )
  ResponseEntity<UserResponse> getUser(
      @PathVariable(name = "user-id") UUID userId
  );

  @Operation(
      summary = "전체 유저 조회",
      description = """
          ### 요청 파라미터
          없음
          
          ### 응답 데이터
          유저 목록(List<UserResponse>)
          
          각 유저 데이터는 아래 정보를 포함합니다.
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
          1. 별도의 요청값 없이 호출합니다.
          2. 저장된 전체 유저 목록을 반환합니다.
          
          ### 유의 사항
          - 현재 구현 기준 Repository의 `findAll()` 결과를 반환합니다.
          - 추후 관리자 권한 전용 API로 제한하는 것을 권장합니다.
          
          ### 예외 처리
          - `INTERNAL_SERVER_ERROR` (500 INTERNAL_SERVER_ERROR): 서버에 문제가 발생했습니다.
          """
  )
  ResponseEntity<List<UserResponse>> getAllUsers();

  @Operation(
      summary = "유저 정보 수정",
      description = """
          ### 요청 파라미터
          Path Variable
          
          - `user-id` (UUID, required): 수정할 유저 ID
          
          Request Body(JSON)
          
          아래 필드는 선택적으로 수정 가능합니다.
          
          - `nickname` (String, optional): 닉네임
          - `profileImageUrl` (String, optional): 프로필 이미지 URL
          - `age` (Integer, optional): 나이
          - `gender` (UserGender, optional): 성별
          - `userJobType` (UserJobType, optional): 직업 유형
          - `userRegion` (UserRegion, optional): 지역
          
          요청 예시
          ```json
          {
            "nickname": "수정된닉네임",
            "profileImageUrl": "https://example.com/new-profile.png",
            "age": 26,
            "gender": "NOT_SELECTED",
            "userJobType": "OTHER",
            "userRegion": "SEOUL"
          }
          ```
          
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
          1. 수정할 유저 ID를 Path Variable로 전달합니다.
          2. 수정할 필드만 Request Body에 포함하여 요청합니다.
          3. 수정 완료 후 변경된 유저 정보를 반환합니다.
          
          ### 유의 사항
          - 전달하지 않은 필드는 기존 값이 유지됩니다.
          - `nickname`은 공백만 입력할 수 없습니다.
          - `nickname`은 최대 30자까지 입력 가능합니다.
          - `profileImageUrl`은 최대 500자까지 입력 가능합니다.
          - `age`는 1 이상이어야 합니다.
          - 다른 유저가 이미 사용 중인 닉네임으로 수정할 수 없습니다.
          
          ### 예외 처리
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          - `USER_NICKNAME_DUPLICATE` (409 CONFLICT): 이미 사용 중인 닉네임입니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<UserResponse> updateUser(
      @PathVariable(name = "user-id") UUID userId,
      @Valid @RequestBody UserUpdateRequest request
  );

  @Operation(
      summary = "유저 상태 변경",
      description = """
          ### 요청 파라미터
          Path Variable
          
          - `user-id` (UUID, required): 상태를 변경할 유저 ID
          
          Request Body(JSON)
          
          - `userStatus` (UserStatus, required): 변경할 유저 상태
          
          요청 예시
          ```json
          {
            "userStatus": "ACTIVE"
          }
          ```
          
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
          - `userStatus` (UserStatus): 변경된 유저 상태
          
          ### 사용 방법
          1. 상태를 변경할 유저 ID를 Path Variable로 전달합니다.
          2. 변경할 상태값을 Request Body에 입력합니다.
          3. 변경 완료 후 유저 정보를 반환합니다.
          
          ### 유의 사항
          - `userStatus`는 필수 입력값입니다.
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.
          - 권한이 필요한 민감 API이므로 추후 ADMIN 권한 제한을 권장합니다.
          
          ### 예외 처리
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<UserResponse> updateUserStatus(
      @PathVariable(name = "user-id") UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest request
  );

  @Operation(
      summary = "유저 권한 변경",
      description = """
          ### 요청 파라미터
          Path Variable
          
          - `user-id` (UUID, required): 권한을 변경할 유저 ID
          
          Request Body(JSON)
          
          - `userRole` (UserRole, required): 변경할 유저 권한
          
          요청 예시
          ```json
          {
            "userRole": "ADMIN"
          }
          ```
          
          ### 응답 데이터
          - `id` (UUID): 유저 ID
          - `email` (String): 이메일
          - `nickname` (String): 닉네임
          - `profileImageUrl` (String): 프로필 이미지 URL
          - `userRole` (UserRole): 변경된 유저 권한
          - `age` (Integer): 나이
          - `gender` (UserGender): 성별
          - `userJobType` (UserJobType): 직업 유형
          - `userRegion` (UserRegion): 지역
          - `userStatus` (UserStatus): 유저 상태
          
          ### 사용 방법
          1. 권한을 변경할 유저 ID를 Path Variable로 전달합니다.
          2. 변경할 권한값을 Request Body에 입력합니다.
          3. 변경 완료 후 유저 정보를 반환합니다.
          
          ### 유의 사항
          - `userRole`은 필수 입력값입니다.
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.
          - 권한 변경 API는 민감한 기능이므로 ADMIN 권한 제한이 필요합니다.
          
          ### 예외 처리
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          - `INVALID_INPUT_VALUE` (400 BAD_REQUEST): 유효하지 않은 입력값입니다.
          """
  )
  ResponseEntity<UserResponse> updateUserRole(
      @PathVariable(name = "user-id") UUID userId,
      @Valid @RequestBody UserRoleUpdateRequest request
  );

  @Operation(
      summary = "유저 삭제",
      description = """
          ### 요청 파라미터
          Path Variable
          
          - `user-id` (UUID, required): 삭제할 유저 ID
          
          요청 예시
          ```text
          /api/users/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa
          ```
          
          ### 응답 데이터
          없음 (204 No Content)
          
          ### 사용 방법
          1. 삭제할 유저 ID를 Path Variable로 전달합니다.
          2. 요청 성공 시 응답 본문 없이 204 상태 코드가 반환됩니다.
          
          ### 유의 사항
          - 실제 DB Row 삭제가 아니라 유저 상태를 탈퇴 상태로 변경하는 방식입니다.
          - 이미 탈퇴한 유저를 다시 삭제하려고 하면 예외가 발생합니다.
          - 존재하지 않는 유저 ID로 요청 시 예외가 발생합니다.
          
          ### 예외 처리
          - `USER_NOT_FOUND` (404 NOT_FOUND): 사용자를 찾을 수 없습니다.
          - `USER_ALREADY_WITHDRAWN` (409 CONFLICT): 이미 탈퇴한 사용자입니다.
          """
  )
  ResponseEntity<Void> deleteUser(
      @PathVariable(name = "user-id") UUID userId
  );
}
