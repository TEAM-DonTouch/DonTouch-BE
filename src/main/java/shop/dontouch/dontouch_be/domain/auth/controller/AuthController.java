package shop.dontouch.dontouch_be.domain.auth.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.auth.dto.request.LoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.RefreshTokenRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.SignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.response.AuthResponse;
import shop.dontouch.dontouch_be.domain.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  @LogMonitoring(logParameters = false)
  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> signup(
      @Valid @RequestBody SignupRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(authService.signup(request));
  }

  @LogMonitoring(logParameters = false)
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(
      @Valid @RequestBody LoginRequest request
  ) {
    return ResponseEntity.ok(authService.login(request));
  }

  @LogMonitoring(logParameters = false)
  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refresh(
      @Valid @RequestBody RefreshTokenRequest request
  ) {
    return ResponseEntity.ok(authService.refresh(request));
  }

  @LogMonitoring(logParameters = false)
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @Valid @RequestBody RefreshTokenRequest request
  ) {
    authService.logout(request);
    return ResponseEntity.noContent().build();
  }
}
