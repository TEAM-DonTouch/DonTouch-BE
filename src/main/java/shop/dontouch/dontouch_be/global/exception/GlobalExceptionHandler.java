package shop.dontouch.dontouch_be.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * CustomException을 처리하는 핸들러 메서드
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    log.error("CustomException 발생: {}", e.getErrorCode(), e);

    ErrorCode errorCode = e.getErrorCode();

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
    log.error("MethodArgumentNotValidException 발생: {}", e.getMessage(), e);

    // 에러 메시지 중 첫 번째 것을 가져옵니다.
    String errorMessage = e.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(fieldError->fieldError.getDefaultMessage())
        .orElse(ErrorCode.INVALID_INPUT_VALUE.getMessage());

    // ErrorCode.INVALID_INPUT_VALUE(400)를 기본으로 사용하되, 메시지만 DTO에서 전달된 메시지로 설정합니다.
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
    log.error("HttpMessageNotReadableException 발생: {}", e.getMessage(), e);

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
    log.error("Unhandled exception 발생: {}", e.getMessage(), e);

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
        .errorMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
