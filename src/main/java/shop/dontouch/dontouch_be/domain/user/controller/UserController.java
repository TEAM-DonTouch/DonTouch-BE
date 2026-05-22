package shop.dontouch.dontouch_be.domain.user.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import shop.dontouch.dontouch_be.domain.user.dto.request.UserStatusUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserResponse;
import shop.dontouch.dontouch_be.domain.user.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponse> createUser(
      @Valid @RequestBody UserCreateRequest request
  ) {
    return ResponseEntity.ok(userService.createUser(request));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(userService.getUser(userId));
  }

  @PatchMapping("/{userId}")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable UUID userId,
      @Valid @RequestBody UserUpdateRequest request
  ) {
    return ResponseEntity.ok(userService.updateUser(userId, request));
  }

  @PatchMapping("/{userId}/status")
  public ResponseEntity<UserResponse> updateUserStatus(
      @PathVariable UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest request
  ) {
    return ResponseEntity.ok(userService.updateUserStatus(userId, request));
  }

  @PatchMapping("/{userId}/role")
  public ResponseEntity<UserResponse> updateUserRole(
      @PathVariable UUID userId,
      @Valid @RequestBody UserRoleUpdateRequest request
  ) {
    return ResponseEntity.ok(userService.updateUserRole(userId, request));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<UserResponse> deleteUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(userService.deleteUser(userId));
  }
}