package shop.dontouch.dontouch_be.global.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * CustomException을 처리하는 핸들러 메서드
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    ErrorCode errorCode = e.getErrorCode();

    log.warn("CustomException: code={}, message={}",
        errorCode,
        errorCode.getMessage()
    );

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(errorCode)
        .errorMessage(errorCode.getMessage())
        .build();

    return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
  }

  /**
   * @Valid 검증 실패 시 예외 처리 메서드
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

    // 에러 메시지 중 첫 번째 것을 가져옵니다.
    String errorMessage = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .findFirst()
        .map(fieldError -> fieldError.getDefaultMessage())
        .orElse(ErrorCode.INVALID_INPUT_VALUE.getMessage());

    log.warn("Validation failed: {}", errorMessage);

    // ErrorCode.INVALID_INPUT_VALUE(400)를 기본으로 사용하되, 메시지만 DTO에서 전달된 메시지로 설정합니다.
    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INVALID_INPUT_VALUE)
        .errorMessage(errorMessage)
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * @RequestParam, @PathVariable 등의 검증 실패 처리
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException e
  ) {
    String errorMessage = e.getConstraintViolations()
        .stream()
        .findFirst()
        .map(violation -> violation.getMessage())
        .orElse(ErrorCode.INVALID_INPUT_VALUE.getMessage());

    log.warn("Constraint violation: {}", errorMessage);

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INVALID_INPUT_VALUE)
        .errorMessage(errorMessage)
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * PathVariable 타입 변환 실패 처리
   * 예: UUID 자리에 이상한 문자열 입력
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e
  ) {
    String errorMessage = "요청 파라미터 형식이 올바르지 않습니다.";

    log.warn("Type mismatch: parameter={}, value={}", e.getName(), e.getValue());

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INVALID_INPUT_VALUE)
        .errorMessage(errorMessage)
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * 필수 요청 파라미터 누락 처리
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException e
  ) {
    String errorMessage = "필수 요청 파라미터가 누락되었습니다.";

    log.warn("Missing request parameter: {}", e.getParameterName());

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INVALID_INPUT_VALUE)
        .errorMessage(errorMessage)
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * JSON 파싱 오류를 처리하는 핸들러 메서드
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    log.warn("Invalid request body: {}", e.getMostSpecificCause().getMessage());

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INVALID_REQUEST)
        .errorMessage(ErrorCode.INVALID_REQUEST.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * 기타 예외를 처리하는 핸들러 메서드
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Unhandled exception", e);

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
        .errorMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
