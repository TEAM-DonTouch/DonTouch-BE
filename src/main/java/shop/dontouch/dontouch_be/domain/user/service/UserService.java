package shop.dontouch.dontouch_be.domain.user.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserCreateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserRoleUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserStatusUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserResponse;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j //로그를 찍을 수 있게 해주는 Lombok 어노테이션
@Service
@RequiredArgsConstructor //생성자를 자동으로 만들어주는 Lombok 어노테이션
@Transactional(readOnly = true) //클래스의 메서드들이 기본적으로 읽기 전용 트랜잭션으로 실행된다는 뜻
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public UserResponse createUser(UserCreateRequest request) {
    // 이메일 중복 체크
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(ErrorCode.USER_EMAIL_DUPLICATE);
    }
    //nickName 중복 체크
    if (userRepository.existsByNickname(request.getNickname())) {
      throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
    }

    // Entity 저장
    User entity = User.builder()
        .email(request.getEmail())
        .nickname(request.getNickname())
        .profileImageUrl(request.getProfileImageUrl())
        .age(request.getAge())
        .gender(request.getGender() != null ? request.getGender() : UserGender.NOT_SELECTED)
        .jobType(request.getUserJobType() != null ? request.getUserJobType() : UserJobType.OTHER)
        .region(request.getUserRegion() != null ? request.getUserRegion() : UserRegion.SEOUL)
        .build();

    User savedEntity = userRepository.save(entity);
    return UserResponse.from(savedEntity);
  }

  @Transactional
  public UserResponse getUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return UserResponse.from(user);
  }

  @Transactional
  public UserResponse updateUser(UUID userId, UserUpdateRequest request) {
    if (request == null) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (request.getNickname() != null
        && userRepository.existsByNicknameAndIdNot(request.getNickname(), userId)) {
      throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
    }

    user.updateUser(
        request.getNickname(),
        request.getProfileImageUrl(),
        request.getAge(),
        request.getGender(),
        request.getUserJobType(),
        request.getUserRegion()
    );

    return UserResponse.from(user);
  }

  @Transactional
  public UserResponse updateUserStatus(UUID userId, UserStatusUpdateRequest request) {
    if (request == null || request.getUserStatus() == null) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    user.updateStatus(request.getUserStatus());

    return UserResponse.from(user);
  }

  @Transactional
  public UserResponse updateUserRole(UUID userId, UserRoleUpdateRequest request) {
    if (request == null || request.getUserRole() == null) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    user.updateRole(request.getUserRole());

    return UserResponse.from(user);
  }

  @Transactional
  public UserResponse deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (user.getStatus() == UserStatus.WITHDRAWN) {
      throw new CustomException(ErrorCode.USER_ALREADY_WITHDRAWN);
    }

    user.withdraw();

    return UserResponse.from(user);
  }
}
