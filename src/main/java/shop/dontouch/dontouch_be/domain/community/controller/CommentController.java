package shop.dontouch.dontouch_be.domain.community.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.community.dto.request.CommentRequest;
import shop.dontouch.dontouch_be.domain.community.dto.response.CommentResponse;
import shop.dontouch.dontouch_be.domain.community.service.CommentService;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/posts/{post-id}/comments")
@RequiredArgsConstructor
public class CommentController implements CommentControllerDocs {

  private final CommentService commentService;

  @LogMonitoring
  @PostMapping
  public ResponseEntity<CommentResponse> createComment(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId,
      @Valid @RequestBody CommentRequest request
  ) {
    CommentResponse response = commentService.createComment(currentUser.getUserId(), postId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @LogMonitoring
  @GetMapping
  public ResponseEntity<List<CommentResponse>> getComments(
      @PathVariable(name = "post-id") UUID postId
  ) {
    List<CommentResponse> responses = commentService.getComments(postId);
    return ResponseEntity.ok(responses);
  }

  @LogMonitoring
  @DeleteMapping("/{comment-id}")
  public ResponseEntity<Void> deleteComment(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId,
      @PathVariable(name = "comment-id") UUID commentId
  ) {
    commentService.deleteComment(currentUser.getUserId(), postId, commentId);
    return ResponseEntity.noContent().build();
  }
}
