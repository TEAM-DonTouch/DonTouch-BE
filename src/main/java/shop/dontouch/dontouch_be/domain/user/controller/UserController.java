package shop.dontouch.dontouch_be.domain.user.controller;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.user.dto.UserDto;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserCreateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserStatusUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.request.UserUpdateRequest;
import shop.dontouch.dontouch_be.domain.user.dto.response.UserResponse;
import shop.dontouch.dontouch_be.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
    UserDto userDto = userService.createUser(request.toDto());
    return ResponseEntity.ok(UserResponse.from(userDto));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
    UserDto userDto = userService.getUser(userId);
    return ResponseEntity.ok(UserResponse.from(userDto));
  }

  @PatchMapping("/{userId}")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable UUID userId,
      @RequestBody UserUpdateRequest request
  ) {
    UserDto userDto = userService.updateUser(userId, request.toDto());
    return ResponseEntity.ok(UserResponse.from(userDto));
  }

  @PatchMapping("/{userId}/status")
  public ResponseEntity<UserResponse> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request
  ) {
    UserDto userDto = userService.updateUserStatus(userId, request.toDto());
    return ResponseEntity.ok(UserResponse.from(userDto));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<UserResponse> deleteUser(@PathVariable UUID userId) {
    UserDto userDto = userService.deleteUser(userId);
    return ResponseEntity.ok(UserResponse.from(userDto));
  }
}
