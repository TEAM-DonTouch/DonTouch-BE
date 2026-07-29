package shop.dontouch.dontouch_be.domain.auth.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
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
import shop.dontouch.dontouch_be.domain.auth.dto.response.OAuthLoginResponse;
import shop.dontouch.dontouch_be.domain.auth.service.OAuthService;

@RestController
@RequestMapping("/api/auth/oauth")
@RequiredArgsConstructor
public class OAuthController implements OAuthControllerDocs {

  private final OAuthService oAuthService;

  @LogMonitoring(logParameters = false)
  @PostMapping("/google/login")
  public ResponseEntity<OAuthLoginResponse> googleLogin(
    @Valid @RequestBody GoogleOAuthLoginRequest request
  ) {
    return ResponseEntity.ok(oAuthService.googleLogin(request));
  }

  @LogMonitoring(logParameters = false)
  @PostMapping("/google/signup")
  public ResponseEntity<AuthResponse> googleSignup(
    @Valid @RequestBody GoogleOAuthSignupRequest request
  ) {
    return ResponseEntity.ok(oAuthService.googleSignup(request));
  }

  @LogMonitoring(logParameters = false)
  @PostMapping("/kakao/login")
  public ResponseEntity<OAuthLoginResponse> kakaoLogin(
    @Valid @RequestBody KakaoOAuthLoginRequest request
  ) {
    return ResponseEntity.ok(oAuthService.kakaoLogin(request));
  }

  @LogMonitoring(logParameters = false)
  @PostMapping("/kakao/signup")
  public ResponseEntity<AuthResponse> kakaoSignup(
    @Valid @RequestBody KakaoOAuthSignupRequest request
  ) {
    return ResponseEntity.ok(oAuthService.kakaoSignup(request));
  }

  @LogMonitoring(logParameters = false)
  @PostMapping("/apple/login")
  public ResponseEntity<OAuthLoginResponse> appleLogin(
    @Valid @RequestBody AppleOAuthLoginRequest request
  ) {
    return ResponseEntity.ok(oAuthService.appleLogin(request));
  }

  @LogMonitoring(logParameters = false)
  @PostMapping("/apple/signup")
  public ResponseEntity<AuthResponse> appleSignup(
    @Valid @RequestBody AppleOAuthSignupRequest request
  ) {
    return ResponseEntity.ok(oAuthService.appleSignup(request));
  }
}