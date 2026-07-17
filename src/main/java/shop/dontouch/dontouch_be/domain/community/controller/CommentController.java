package shop.dontouch.dontouch_be.domain.community.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import shop.dontouch.dontouch_be.global.common.dto.PageResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/posts/{post-id}/comments")
@RequiredArgsConstructor
public class CommentController implements CommentControllerDocs {

  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<CommentResponse> createComment(
    @AuthenticationPrincipal CustomUserDetails currentUser,
    @PathVariable(name = "post-id") UUID postId,
    @Valid @RequestBody CommentRequest request
  ) {
    return ResponseEntity.ok(
      commentService.createComment(currentUser.getUserId(), postId, request)
    );
  }

  @GetMapping
  public ResponseEntity<PageResponse<CommentResponse>> getComments(
    @PathVariable(name = "post-id") UUID postId,
    @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC)
    Pageable pageable
  ) {
    return ResponseEntity.ok(commentService.getComments(postId, pageable));
  }

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