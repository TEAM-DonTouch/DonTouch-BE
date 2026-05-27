package shop.dontouch.dontouch_be.global.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shop.dontouch.dontouch_be.domain.user.entity.User;

@Component
public class JwtProvider {
  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.access-token-expiration}")
  private long accessTokenExpiration;

  private Key key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String createAccessToken(User user) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + accessTokenExpiration);

    return Jwts.builder()
        .subject(user.getId().toString())
        .claim("loginId", user.getLoginId())
        .claim("email", user.getEmail())
        .claim("role", user.getRole().name())
        .issuedAt(now)
        .expiration(expiration)
        .signWith(key)
        .compact();
  }
}
