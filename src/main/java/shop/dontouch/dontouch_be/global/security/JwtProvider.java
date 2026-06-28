package shop.dontouch.dontouch_be.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.crypto.SecretKey;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class JwtProvider {

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.access-token-expiration}")
  private Long accessTokenExpiration;

  @Value("${jwt.refresh-token-expiration}")
  private Long refreshTokenExpiration;

  private SecretKey key;

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String createAccessToken(User user) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + accessTokenExpiration);

    return Jwts.builder()
        .subject(user.getId().toString())
        .claim("role", user.getRole().name())
        .issuedAt(now)
        .expiration(expirationDate)
        .signWith(key, SIG.HS256)
        .compact();
  }

  // Refresh Token 생성
  public String createRefreshToken() {
    byte[] bytes = new byte[32]; // 256-bit
    SECURE_RANDOM.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  public String extractUserId(String token) {
    return getClaims(token).getSubject();
  }

  public void validateToken(String token) {
    try {
      getClaims(token);
    } catch (ExpiredJwtException e) {
      throw new CustomException(ErrorCode.TOKEN_EXPIRED);
    } catch (JwtException | IllegalArgumentException e) {
      throw new CustomException(ErrorCode.TOKEN_INVALID);
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public Long getRefreshTokenExpiration() {
    return refreshTokenExpiration;
  }
}