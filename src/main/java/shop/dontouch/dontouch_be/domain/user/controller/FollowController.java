package shop.dontouch.dontouch_be.domain.user.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.user.dto.response.FollowResponse;
import shop.dontouch.dontouch_be.domain.user.service.FollowService;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FollowController implements FollowControllerDocs {

  private final FollowService followService;

  @LogMonitoring
  @PostMapping("/{user-id}/follow")
  public ResponseEntity<FollowResponse> toggleFollow(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "user-id") UUID userId
  ) {
    FollowResponse response = followService.toggleFollow(currentUser.getUserId(), userId);
    return ResponseEntity.ok(response);
  }
}
