package shop.dontouch.dontouch_be.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.auth.dto.request.LoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.SignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.response.AuthResponse;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserResponse;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;
import shop.dontouch.dontouch_be.global.security.JwtProvider;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public AuthResponse signup(SignupRequest request) {
    if(userRepository.existsByLoginId(request.getLoginId())) {
      throw new CustomException(ErrorCode.USER_LOGIN_ID_DUPLICATE);
    }
    if(userRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(ErrorCode.USER_EMAIL_DUPLICATE);
    }
    if(userRepository.existsByNickname(request.getNickname())) {
      throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
    }

    User user = User.builder()
        .email(request.getEmail())
        .loginId(request.getLoginId())
        .password(passwordEncoder.encode(request.getPassword()))
        .nickname(request.getNickname())
        .age(request.getAge())
        .gender(request.getGender() != null ? request.getGender() : UserGender.NOT_SELECTED)
        .jobType(request.getJobType() != null ? request.getJobType() : UserJobType.OTHER)
        .region(request.getRegion() != null ? request.getRegion() : UserRegion.SEOUL)
        .build();

    User savedUser;
    try {
      savedUser = userRepository.saveAndFlush(user);
    } catch (DataIntegrityViolationException e) {
        throw new CustomException(ErrorCode.USER_DUPLICATE);
    }

    String accessToken = jwtProvider.createAccessToken(savedUser);

    return AuthResponse.builder()
        .accessToken(accessToken)
        .tokenType("Bearer")
        .user(UserResponse.from(savedUser))
        .build();
  }

  public AuthResponse login(LoginRequest request) {
    User user = userRepository.findByLoginId(request.getLoginId())
        .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

    if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new CustomException(ErrorCode.LOGIN_FAILED);
    }
    if(user.getStatus() == UserStatus.WITHDRAWN) {
      throw new CustomException(ErrorCode.USER_ALREADY_WITHDRAWN);
    }

    String accessToken = jwtProvider.createAccessToken(user);

    return AuthResponse.builder()
        .accessToken(accessToken)
        .tokenType("Bearer")
        .user(UserResponse.from(user))
        .build();
  }
}
