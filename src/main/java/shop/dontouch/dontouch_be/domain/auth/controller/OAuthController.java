package shop.dontouch.dontouch_be.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dontouch.dontouch_be.domain.auth.dto.request.AppleOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.AppleOAuthSignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.GoogleOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.GoogleOAuthSignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.KakaoOAuthLoginRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.request.KakaoOAuthSignupRequest;
import shop.dontouch.dontouch_be.domain.auth.dto.response.AuthResponse;
import shop.dontouch.dontouch_be.domain.auth.service.OAuthService;

@RestController
@RequestMapping("/api/auth/oauth")
@RequiredArgsConstructor
public class OAuthController {

  private final OAuthService oAuthService;

  @PostMapping("/google/login")
  public ResponseEntity<AuthResponse> googleLogin(
    @Valid @RequestBody GoogleOAuthLoginRequest request
  ) {
    return ResponseEntity.ok(oAuthService.googleLogin(request));
  }

  @PostMapping("/google/signup")
  public ResponseEntity<AuthResponse> googleSignup(
    @Valid @RequestBody GoogleOAuthSignupRequest request
  ) {
    return ResponseEntity.ok(oAuthService.googleSignup(request));
  }

  @PostMapping("/kakao/login")
  public ResponseEntity<AuthResponse> kakaoLogin(
    @Valid @RequestBody KakaoOAuthLoginRequest request
  ) {
    return ResponseEntity.ok(oAuthService.kakaoLogin(request));
  }

  @PostMapping("/kakao/signup")
  public ResponseEntity<AuthResponse> kakaoSignup(
    @Valid @RequestBody KakaoOAuthSignupRequest request
  ) {
    return ResponseEntity.ok(oAuthService.kakaoSignup(request));
  }

  @PostMapping("/apple/login")
  public ResponseEntity<AuthResponse> appleLogin(
    @Valid @RequestBody AppleOAuthLoginRequest request
  ) {
    return ResponseEntity.ok(oAuthService.appleLogin(request));
  }

  @PostMapping("/apple/signup")
  public ResponseEntity<AuthResponse> appleSignup(
    @Valid @RequestBody AppleOAuthSignupRequest request
  ) {
    return ResponseEntity.ok(oAuthService.appleSignup(request));
  }
}