package shop.dontouch.dontouch_be.domain.challenge.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.challenge.dto.request.ChallengeRequest;
import shop.dontouch.dontouch_be.domain.challenge.dto.response.ChallengeResponse;
import shop.dontouch.dontouch_be.domain.challenge.service.ChallengeService;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController implements ChallengeControllerDocs {

  private final ChallengeService challengeService;

  @LogMonitoring
  @GetMapping
  public ResponseEntity<List<ChallengeResponse>> getChallenges(
      @AuthenticationPrincipal CustomUserDetails currentUser
  ) {
    return ResponseEntity.ok(challengeService.getChallenges(currentUser.getUserId()));
  }

  @LogMonitoring
  @GetMapping("/me")
  public ResponseEntity<List<ChallengeResponse>> getMyChallenges(
      @AuthenticationPrincipal CustomUserDetails currentUser
  ) {
    return ResponseEntity.ok(challengeService.getMyChallenges(currentUser.getUserId()));
  }

  @LogMonitoring
  @PostMapping("/{challenge-id}/join")
  public ResponseEntity<ChallengeResponse> joinChallenge(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "challenge-id") UUID challengeId
  ) {
    ChallengeResponse response = challengeService.joinChallenge(currentUser.getUserId(), challengeId);
    return ResponseEntity.ok(response);
  }

  ///  ADMIN

  @LogMonitoring
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping
  public ResponseEntity<ChallengeResponse> createChallenge(
      @Valid @RequestBody ChallengeRequest request
  ) {
    ChallengeResponse response = challengeService.createChallenge(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
