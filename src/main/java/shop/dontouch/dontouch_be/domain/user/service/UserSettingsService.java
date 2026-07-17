package shop.dontouch.dontouch_be.domain.user.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserSettingsUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserSettingsResponse;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.entity.UserSettings;
import shop.dontouch.dontouch_be.domain.user.repository.UserSettingsRepository;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSettingsService {

  private final UserSettingsRepository userSettingsRepository;

  public UserSettingsResponse getSettings(UUID userId) {
    return UserSettingsResponse.from(getSettingsOrThrow(userId));
  }

  @Transactional
  public UserSettingsResponse updateSettings(UUID userId, UserSettingsUpdateRequest request) {
    UserSettings userSettings = getSettingsOrThrow(userId);

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

  private UserSettings getSettingsOrThrow(UUID userId) {
    return userSettingsRepository.findByUserId(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_SETTINGS_NOT_FOUND));
  }
}