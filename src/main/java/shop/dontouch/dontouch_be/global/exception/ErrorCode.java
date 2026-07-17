package shop.dontouch.dontouch_be.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // Global
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다."),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

  // Transaction
  TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "거래를 찾을 수 없습니다."),

  // Category
  CATEGORY_NAME_DUPLICATE(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),
  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
  CATEGORY_IN_USE(HttpStatus.CONFLICT, "이미 거래에서 사용 중인 카테고리는 삭제할 수 없습니다."),

  // BUDGET
  BUDGET_NOT_FOUND(HttpStatus.NOT_FOUND, "예산을 찾을 수 없습니다."),
  BUDGET_PERIOD_DATE_REQUIRED(HttpStatus.BAD_REQUEST, "CUSTOM 기간은 시작일과 종료일을 모두 입력해야 합니다."),
  BUDGET_PERIOD_DATE_INVALID(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 늦을 수 없습니다."),

  // User
  USER_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
  USER_NICKNAME_DUPLICATE(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  USER_WITHDRAWN(HttpStatus.FORBIDDEN, "탈퇴한 사용자입니다."),
  USER_ALREADY_WITHDRAWN(HttpStatus.CONFLICT, "이미 탈퇴한 사용자입니다."),
  USER_SUSPENDED(HttpStatus.FORBIDDEN, "정지된 계정입니다."),
  USER_ALREADY_SUSPENDED(HttpStatus.CONFLICT, "이미 정지된 사용자입니다."),
  USER_SETTINGS_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 설정을 찾을 수 없습니다."),

  //Auth
  USER_LOGIN_ID_DUPLICATE(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
  LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다. 아이디 또는 비밀번호를 확인해주세요."),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
  TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
  REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "리프레시 토큰을 찾을 수 없습니다."),
  SOCIAL_SIGNUP_REQUIRED(HttpStatus.NOT_FOUND, "소셜 회원가입이 필요합니다."),
  SOCIAL_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "소셜 로그인에 실패했습니다."),
  USER_DUPLICATE(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
  SOCIAL_SIGNUP_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "소셜 회원가입 토큰이 유효하지 않습니다."),

  // Post
  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
  POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "본인이 작성한 게시글만 수정/삭제할 수 있습니다."),

  // Comment
  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
  COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 삭제할 수 있습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
