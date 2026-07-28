package shop.dontouch.dontouch_be.domain.community.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.community.dto.request.CommentRequest;
import shop.dontouch.dontouch_be.domain.community.dto.response.CommentResponse;
import shop.dontouch.dontouch_be.domain.community.service.CommentService;
import shop.dontouch.dontouch_be.global.common.dto.PageResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/posts/{post-id}/comments")
@RequiredArgsConstructor
@Validated
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
  public ResponseEntity<PageResponse<CommentResponse>> getComments(
    @PathVariable(name = "post-id") UUID postId,
    @RequestParam(name = "sort", defaultValue = "oldest") String sort,
    @RequestParam(name = "page", defaultValue = "0")
    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page,
    @RequestParam(name = "size", defaultValue = "20")
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    @Max(value = 50, message = "페이지 크기는 50 이하여야 합니다.") int size
  ) {
    return ResponseEntity.ok(commentService.getComments(postId, sort, page, size));
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