package shop.dontouch.dontouch_be.domain.user.service;

import org.hibernate.exception.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserResponse createUser(UserCreateRequest request) {
    if (request == null) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }
    if (userRepository.existsByLoginId(request.getLoginId())) {
      throw new CustomException(ErrorCode.USER_LOGIN_ID_DUPLICATE);
    }
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
        .loginId(request.getLoginId())
        .password(passwordEncoder.encode(request.getPassword()))
        .email(request.getEmail())
        .nickname(request.getNickname())
        .profileImageUrl(request.getProfileImageUrl())
        .age(request.getAge())
        .gender(request.getGender() != null ? request.getGender() : UserGender.NOT_SELECTED)
        .jobType(request.getUserJobType() != null ? request.getUserJobType() : UserJobType.OTHER)
        .region(request.getUserRegion() != null ? request.getUserRegion() : UserRegion.SEOUL)
        .build();

    try {
      User savedEntity = userRepository.saveAndFlush(entity);
      return UserResponse.from(savedEntity);
    } catch (DataIntegrityViolationException e) {
      if (isUniqueConstraintViolation(e)) {
        throw new CustomException(ErrorCode.USER_DUPLICATE);
      }

      throw e;
    }
  }

  private boolean isUniqueConstraintViolation(DataIntegrityViolationException e) {
    Throwable cause = e.getCause();

    while (cause != null) {
      if (cause instanceof ConstraintViolationException constraintViolationException) {
        SQLException sqlException = constraintViolationException.getSQLException();

        //23505는 많은 SQL 데이터베이스에서 '유니크 제약조건 위반(Unique Violation, 중복 데이터 입력 에러)'을 뜻하는 SQLState 코드입니다.
        return sqlException != null && "23505".equals(sqlException.getSQLState());
      }

      cause = cause.getCause();
    }

    return false;
  }

  public UserResponse getUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return UserResponse.from(user);
  }

  public List<UserResponse> getAllUsers() {
    return userRepository.findAll()
        .stream()
        .map(UserResponse::from)
        .toList();
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

    if (user.getStatus() == request.getUserStatus()) {
      if (request.getUserStatus() == UserStatus.SUSPENDED) {
        throw new CustomException(ErrorCode.USER_ALREADY_SUSPENDED);
      }
      if (request.getUserStatus() == UserStatus.WITHDRAWN) {
        throw new CustomException(ErrorCode.USER_ALREADY_WITHDRAWN);
      }
    }

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
  public void deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (user.getStatus() == UserStatus.WITHDRAWN) {
      throw new CustomException(ErrorCode.USER_ALREADY_WITHDRAWN);
    }

    user.withdraw();
  }
}
