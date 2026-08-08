package shop.dontouch.dontouch_be.global.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
   * DB 제약 조건 위반(unique, FK, not null 등) 처리
   *
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
      DataIntegrityViolationException e
  ) {
    log.error("처리되지 않은 DB 제약 조건 위반", e);

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.DATA_INTEGRITY_VIOLATION)
        .errorMessage(ErrorCode.DATA_INTEGRITY_VIOLATION.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
    log.warn("AccessDeniedException: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.ACCESS_DENIED)
        .errorMessage(ErrorCode.ACCESS_DENIED.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
  }

  /**
   * 매핑되지 않은 경로 요청 처리 (예: URL 오타, 스캐너의 무작위 요청)
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
    log.warn("존재하지 않는 경로 요청: {}", e.getResourcePath());

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.RESOURCE_NOT_FOUND)
        .errorMessage(ErrorCode.RESOURCE_NOT_FOUND.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  /**
   * 지원하지 않는 HTTP 메서드로 호출한 경우 (예: POST 전용 엔드포인트를 GET으로 호출)
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e
  ) {
    log.warn("지원하지 않는 HTTP 메서드: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.METHOD_NOT_ALLOWED)
        .errorMessage(ErrorCode.METHOD_NOT_ALLOWED.getMessage())
        .build();

    // RFC 9110: 405 응답에는 허용되는 메서드를 Allow 헤더로 알려야 한다
    HttpHeaders headers = new HttpHeaders();
    if (e.getSupportedHttpMethods() != null) {
      headers.setAllow(e.getSupportedHttpMethods());
    }

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(headers).body(response);
  }

  /**
   * 지원하지 않는 Content-Type 으로 요청한 경우
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException e
  ) {
    log.warn("지원하지 않는 Content-Type: {}", e.getMessage());

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.UNSUPPORTED_MEDIA_TYPE)
        .errorMessage(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
  }

  /**
   * 기타 예외를 처리하는 핸들러 메서드
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    // Spring MVC 표준 예외는 자신의 상태 코드를 알고 있다(ErrorResponse 구현).
    // 개별 핸들러를 빠뜨리더라도 4xx가 500으로 둔갑하지 않도록 그 값을 살려준다.
    if (e instanceof org.springframework.web.ErrorResponse springError) {
      HttpStatus status = HttpStatus.resolve(springError.getStatusCode().value());

      if (status != null && status.is4xxClientError()) {
        log.warn("처리되지 않은 요청 오류 {} -> {}", e.getClass().getSimpleName(), status.value());

        ErrorResponse response = ErrorResponse.builder()
            .errorCode(ErrorCode.INVALID_REQUEST)
            .errorMessage(ErrorCode.INVALID_REQUEST.getMessage())
            .build();

        return ResponseEntity.status(status).body(response);
      }
    }

    log.error("Unhandled exception", e);

    ErrorResponse response = ErrorResponse.builder()
        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
        .errorMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
