package shop.dontouch.dontouch_be.domain.auth.repository;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import shop.dontouch.dontouch_be.domain.auth.oauth.SocialSignupInfo;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Repository
@RequiredArgsConstructor
public class SocialSignupTokenRedisRepository {

  private static final String KEY_PREFIX = "socialSignup:";
  private static final Duration EXPIRATION = Duration.ofMinutes(10);

  private final StringRedisTemplate stringRedisTemplate;
  private final JsonMapper jsonMapper;

  public void save(String signupToken, SocialSignupInfo signupInfo) {
    try {
      String value = jsonMapper.writeValueAsString(signupInfo);
      stringRedisTemplate.opsForValue()
        .set(KEY_PREFIX + signupToken, value, EXPIRATION);
    } catch (JacksonException e) {
      throw new IllegalStateException("소셜 회원가입 정보 직렬화 실패", e);
    }
  }

  public Optional<SocialSignupInfo> findByToken(String signupToken) {
    String value = stringRedisTemplate.opsForValue().get(KEY_PREFIX + signupToken);

    if (value == null) {
      return Optional.empty();
    }

    try {
      return Optional.of(jsonMapper.readValue(value, SocialSignupInfo.class));
    } catch (JacksonException e) {
      throw new IllegalStateException("소셜 회원가입 정보 역직렬화 실패", e);
    }
  }

  public void delete(String signupToken) {
    stringRedisTemplate.delete(KEY_PREFIX + signupToken);
  }
}