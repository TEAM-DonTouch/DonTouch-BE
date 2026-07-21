package shop.dontouch.dontouch_be.domain.user.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserSettingsUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserSettingsResponse;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.entity.UserSettings;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.domain.user.repository.UserSettingsRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSettingsService {

  private final UserSettingsRepository userSettingsRepository;
  private final UserRepository userRepository;

  public UserSettingsResponse getSettings(UUID userId) {
    return UserSettingsResponse.from(getOrCreateSettings(userId));
  }

  @Transactional
  public UserSettingsResponse updateSettings(UUID userId, UserSettingsUpdateRequest request) {
    UserSettings userSettings = getOrCreateSettings(userId);

    userSettings.updateSettings(
        request.getPushNotificationEnabled(),
        request.getBiometricLoginEnabled()
    );

    return UserSettingsResponse.from(userSettings);
  }

  @Transactional
  public void createDefaultSettings(User user) {
    if (userSettingsRepository.existsByUserId(user.getId())) {
      return;
    }

    UserSettings userSettings = UserSettings.builder()
        .user(user)
        .build();

    userSettingsRepository.save(userSettings);
  }

  @Transactional
  public UserSettings getOrCreateSettings(UUID userId) {
    return userSettingsRepository.findByUserId(userId)
        .orElseGet(() -> createSettings(userId));
  }

  private UserSettings createSettings(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("createSettings: 존재하지 않는 userId:{}", userId);
          return new CustomException(ErrorCode.USER_NOT_FOUND);
        });

    try {
      return userSettingsRepository.saveAndFlush(
          UserSettings.builder()
              .user(user)
              .build()
      );
    } catch (DataIntegrityViolationException e) {
      log.warn("createSettings: 동시 요청으로 설정이 이미 생성됨, userId={}", userId);
      throw new CustomException(ErrorCode.USER_SETTINGS_CONFLICT);
    }
  }
}
