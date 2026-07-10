package shop.dontouch.dontouch_be.domain.user.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserSettingsUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserSettingsResponse;
import shop.dontouch.dontouch_be.domain.user.entity.UserSettings;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.domain.user.repository.UserSettingsRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSettingsService {

  private final UserSettingsRepository userSettingsRepository;
  private final UserRepository userRepository;

  @Transactional
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

  private UserSettings getOrCreateSettings(UUID userId) {
    return userSettingsRepository.findByUserId(userId)
        .orElseGet(() -> userSettingsRepository.save(
            UserSettings.builder()
                .user(userRepository.getReferenceById(userId))
                .build()
        ));
  }
}
