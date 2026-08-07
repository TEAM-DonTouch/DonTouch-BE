package shop.dontouch.dontouch_be.domain.user.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserCreateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserRoleUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserSettingsUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserStatusUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserResponse;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserSettingsResponse;
import shop.dontouch.dontouch_be.domain.user.service.UserService;
import shop.dontouch.dontouch_be.domain.user.service.UserSettingsService;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

  private final UserService userService;
  private final UserSettingsService userSettingsService;



  @LogMonitoring
  @GetMapping("/me")
  public ResponseEntity<UserResponse> getMe(
      @AuthenticationPrincipal CustomUserDetails currentUser
  ) {
    return ResponseEntity.ok(userService.getUser(currentUser.getUserId()));
  }

  @LogMonitoring
  @PatchMapping("/me")
  public ResponseEntity<UserResponse> updateMe(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody UserUpdateRequest request
  ) {
    return ResponseEntity.ok(userService.updateUser(currentUser.getUserId(), request));
  }

  @LogMonitoring
  @DeleteMapping("/me")
  public ResponseEntity<Void> deleteMe(
      @AuthenticationPrincipal CustomUserDetails currentUser
  ) {
    userService.deleteUser(currentUser.getUserId());
    return ResponseEntity.noContent().build();
  }

  @LogMonitoring
  @GetMapping("/me/settings")
  public ResponseEntity<UserSettingsResponse> getMySettings(
      @AuthenticationPrincipal CustomUserDetails currentUser
  ) {
    return ResponseEntity.ok(userSettingsService.getSettings(currentUser.getUserId()));
  }

  @LogMonitoring
  @PatchMapping("/me/settings")
  public ResponseEntity<UserSettingsResponse> updateMySettings(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody UserSettingsUpdateRequest request
  ) {
    return ResponseEntity.ok(userSettingsService.updateSettings(currentUser.getUserId(), request));
  }

  ///  ADMIN

  @LogMonitoring(logParameters = false)
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/admin")
  public ResponseEntity<UserResponse> createUser(
      @Valid @RequestBody UserCreateRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.createUser(request));
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/{user-id}")
  public ResponseEntity<UserResponse> getUser(
      @PathVariable(name = "user-id") UUID userId
  ) {
    return ResponseEntity.ok(userService.getUser(userId));
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @PatchMapping("/{user-id}/status")
  public ResponseEntity<UserResponse> updateUserStatus(
      @PathVariable(name = "user-id") UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest request
  ) {
    return ResponseEntity.ok(userService.updateUserStatus(userId, request));
  }

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @PatchMapping("/{user-id}/role")
  public ResponseEntity<UserResponse> updateUserRole(
      @PathVariable(name = "user-id") UUID userId,
      @Valid @RequestBody UserRoleUpdateRequest request
  ) {
    return ResponseEntity.ok(userService.updateUserRole(userId, request));
  }
}
