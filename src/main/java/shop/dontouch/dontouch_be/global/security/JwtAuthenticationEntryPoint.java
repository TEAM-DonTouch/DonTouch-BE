package shop.dontouch.dontouch_be.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;
import shop.dontouch.dontouch_be.global.exception.ErrorResponse;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;


  @Override
  public void commence(
      // 유효한 자격증명을 제공하지 않고 접근하려 할 때 401
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException, ServletException {
    writeErrorResponse(response, ErrorCode.TOKEN_INVALID);
  }

  private void writeErrorResponse(
      HttpServletResponse response,
      ErrorCode errorCode
  ) throws IOException, ServletException {
    response.setStatus(errorCode.getHttpStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    ErrorResponse errorResponse = ErrorResponse.builder()
        .errorCode(errorCode)
        .errorMessage(errorCode.getMessage())
        .build();

    response.getWriter().print(objectMapper.writeValueAsString(errorResponse));

  }
}
