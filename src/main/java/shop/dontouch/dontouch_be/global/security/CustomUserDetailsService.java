package shop.dontouch.dontouch_be.global.security;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  // username = userId
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UUID userId;

    // JWT subject에서 꺼낸 username를 UUID로 변환
    // 만약 변환이 실패하면 토큰이 유효하지 않다고 판단하고 예외를 던짐
    try {
      userId = UUID.fromString(username);
    } catch (IllegalArgumentException e) {
      throw new CustomException(ErrorCode.TOKEN_INVALID);
    }

    return userRepository.findById(userId)
        .map(CustomUserDetails::new)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }
}
