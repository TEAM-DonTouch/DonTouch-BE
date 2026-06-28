package shop.dontouch.dontouch_be.domain.auth.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@RedisHash(value = "refreshToken")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

  // Redis Key
  @Id
  private String token;


  private String userId;

  @TimeToLive
  private Long expiration;

  // 로그인 / 회원가 시 최초 생할 때 사용
  public RefreshToken(String token, String userId, Long expiration) {
    this.token = token;
    this.userId = userId;
    this.expiration = expiration;
  }
}
