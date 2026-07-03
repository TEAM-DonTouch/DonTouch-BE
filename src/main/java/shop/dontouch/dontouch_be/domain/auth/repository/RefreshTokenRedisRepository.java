package shop.dontouch.dontouch_be.domain.auth.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {

  private static final String KEY_PREFIX = "refreshToken:";

  private final StringRedisTemplate stringRedisTemplate;

  public void save(String tokenKey, String userId, long expirationSeconds) {
    stringRedisTemplate.opsForValue().set(
        KEY_PREFIX + tokenKey,
        userId,
        java.time.Duration.ofSeconds(expirationSeconds)
    );
  }

  public boolean delete(String tokenKey) {
    Boolean result = stringRedisTemplate.delete(KEY_PREFIX + tokenKey);
    return Boolean.TRUE.equals(result);
  }

  public Optional<String> rotate(String oldTokenKey, String newTokenKey, long expirationSeconds) {
    String oldRedisKey = KEY_PREFIX + oldTokenKey;
    String newRedisKey = KEY_PREFIX + newTokenKey;

    String script = """
                local userId = redis.call('GET', KEYS[1])
                if not userId then
                    return nil
                end

                redis.call('DEL', KEYS[1])
                redis.call('SET', KEYS[2], userId, 'EX', ARGV[1])

                return userId
                """;

    RedisScript<String> redisScript = RedisScript.of(script, String.class);

    String userId = stringRedisTemplate.execute(
        redisScript,
        List.of(oldRedisKey, newRedisKey),
        String.valueOf(expirationSeconds)
    );

    return Optional.ofNullable(userId);
  }
}