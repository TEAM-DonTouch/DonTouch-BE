package shop.dontouch.dontouch_be.domain.user.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class UserController implements UserControllerDocs {

  private final UserService userService;

  @PostMapping("/admin")
  public ResponseEntity<UserResponse> createUser(
      @Valid @RequestBody UserCreateRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.createUser(request));
  }

  @GetMapping("/{user-id}")
  public ResponseEntity<UserResponse> getUser(
      @PathVariable(name = "user-id") UUID userId) {
    return ResponseEntity.ok(userService.getUser(userId));
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PatchMapping("/{user-id}")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable(name = "user-id") UUID userId,
      @Valid @RequestBody UserUpdateRequest request
  ) {
    return ResponseEntity.ok(userService.updateUser(userId, request));
  }

  @PatchMapping("/{user-id}/status")
  public ResponseEntity<UserResponse> updateUserStatus(
      @PathVariable(name = "user-id") UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest request
  ) {
    return ResponseEntity.ok(userService.updateUserStatus(userId, request));
  }

  @PatchMapping("/{user-id}/role")
  public ResponseEntity<UserResponse> updateUserRole(
      @PathVariable(name = "user-id") UUID userId,
      @Valid @RequestBody UserRoleUpdateRequest request
  ) {
    return ResponseEntity.ok(userService.updateUserRole(userId, request));
  }

  @DeleteMapping("/{user-id}")
  public ResponseEntity<Void> deleteUser(
      @PathVariable(name = "user-id") UUID userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }
}
