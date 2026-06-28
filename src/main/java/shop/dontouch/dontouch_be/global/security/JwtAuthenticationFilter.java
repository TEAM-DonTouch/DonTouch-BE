package shop.dontouch.dontouch_be.global.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;
import shop.dontouch.dontouch_be.global.exception.ErrorResponse;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final CustomUserDetailsService customUserDetailsService;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // Authorization 헤더에서 JWT 토큰 추출
    String token = resolveToken(request);

    if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // 토큰 유효성 검사
      try {
        // 서명 검증 + 만료 확인
        jwtProvider.validateToken(token);

        // 토큰에서 사용자 정보 추출
        String userId = jwtProvider.extractUserId(token);

        // 사용자 정보로 UserDetails 객체 생성
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

        if (!userDetails.isEnabled()) {
          writeErrorResponse(response, ErrorCode.USER_ALREADY_WITHDRAWN);
          return;
        }

        // SecurityContext에 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );

        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (CustomException e) {
        writeErrorResponse(response, e.getErrorCode());
        return;
      } catch (JwtException e) {
        writeErrorResponse(response, ErrorCode.TOKEN_INVALID);
        return;
      }
    }

    // 다음 필터로 요청을 전달
    filterChain.doFilter(request, response);

  }

  private String resolveToken(HttpServletRequest request) {
    // Authorization 헤더에서 토큰 추출
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7); // "Bearer  이후의 토큰 부분만 반환
    }
    return null;
  }

  private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setStatus(errorCode.getHttpStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    ErrorResponse errorResponse = ErrorResponse.builder()
        .errorCode(errorCode)
        .errorMessage(errorCode.getMessage())
        .build();

    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    response.getWriter().flush();
  }

}
