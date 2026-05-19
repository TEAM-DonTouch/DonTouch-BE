package shop.dontouch.dontouch_be.domain.user.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.domain.user.dto.UserDto;
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
  public UserDto createUser(UserDto userDto) {
    if (userDto == null || userDto.getEmail() == null || userDto.getEmail().isBlank()) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }
    if (userDto.getNickname() == null || userDto.getNickname().isBlank()) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }

    // 이메일 중복 체크
    if (userRepository.existsByEmail(userDto.getEmail())) {
      throw new CustomException(ErrorCode.USER_EMAIL_DUPLICATE);
    }
    //nickName 중복 체크
    if (userRepository.existsByNickname(userDto.getNickname())) {
      throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
    }

    // Entity 변환 및 저장
    User entity = User.builder()
        .email(userDto.getEmail())
        .nickname(userDto.getNickname())
        .profileImageUrl(userDto.getProfileImageUrl())
        .age(userDto.getAge())
        .gender(userDto.getGender())
        .userJobType(userDto.getUserJobType() != null ? userDto.getUserJobType() : UserJobType.OTHER)
        .userRegion(userDto.getUserRegion() != null ? userDto.getUserRegion() : UserRegion.SEOUL)
        .build();

    User savedEntity = userRepository.save(entity);
    return UserDto.entityToDto(savedEntity);
  }

  @Transactional(readOnly = true)
  public UserDto getUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return UserDto.entityToDto(user);
  }

  @Transactional
  public UserDto updateUser(UUID userId, UserDto userDto) {
    if (userDto == null) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    //nickName 중복 체크
    if (userDto.getNickname() != null &&
        userRepository.existsByNicknameAndIdNot(userDto.getNickname(), userId)) {
      throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
    }

    user.updateUser(userDto);

    return UserDto.entityToDto(user);
  }

  @Transactional
  public UserDto updateUserStatus(UUID userId, UserDto userDto) {
    if (userDto == null || userDto.getUserStatus() == null) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    user.updateStatus(userDto.getUserStatus());

    return UserDto.entityToDto(user);
  }

  @Transactional
  public UserDto updateUserRole(UUID userId, UserDto userDto) {
    if (userDto == null || userDto.getUserRole() == null) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    user.updateRole(userDto.getUserRole());

    return UserDto.entityToDto(user);
  }

  @Transactional
  public UserDto deleteUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (user.getUserStatus() == UserStatus.WITHDRAWN) {
      throw new CustomException(ErrorCode.USER_ALREADY_WITHDRAWN);
    }

    user.withdraw();

    return UserDto.entityToDto(user);
  }
}
