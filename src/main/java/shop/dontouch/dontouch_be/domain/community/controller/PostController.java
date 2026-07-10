package shop.dontouch.dontouch_be.domain.community.controller;

import com.chuseok22.logging.annotation.LogMonitoring;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.dontouch.dontouch_be.domain.community.dto.request.PostRequest;
import shop.dontouch.dontouch_be.domain.community.dto.response.PostLikeResponse;
import shop.dontouch.dontouch_be.domain.community.dto.response.PostResponse;
import shop.dontouch.dontouch_be.domain.community.service.PostLikeService;
import shop.dontouch.dontouch_be.domain.community.service.PostService;
import shop.dontouch.dontouch_be.global.common.dto.PageResponse;
import shop.dontouch.dontouch_be.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController implements PostControllerDocs {

  private final PostService postService;
  private final PostLikeService postLikeService;

  @LogMonitoring
  @PostMapping
  public ResponseEntity<PostResponse> createPost(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody PostRequest request
  ) {
    PostResponse response = postService.createPost(currentUser.getUserId(), request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @LogMonitoring
  @GetMapping
  public ResponseEntity<PageResponse<PostResponse>> getPosts(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @RequestParam(name = "sort", defaultValue = "latest") String sort,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size,
      @RequestParam(name = "following", defaultValue = "false") boolean following
  ) {
    PageResponse<PostResponse> responses =
        postService.getPosts(currentUser.getUserId(), sort, page, size, following);
    return ResponseEntity.ok(responses);
  }

  @LogMonitoring
  @GetMapping("/{post-id}")
  public ResponseEntity<PostResponse> getPost(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId
  ) {
    PostResponse response = postService.getPost(currentUser.getUserId(), postId);
    return ResponseEntity.ok(response);
  }

  @LogMonitoring
  @PatchMapping("/{post-id}")
  public ResponseEntity<PostResponse> updatePost(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId,
      @Valid @RequestBody PostRequest request
  ) {
    PostResponse response = postService.updatePost(currentUser.getUserId(), postId, request);
    return ResponseEntity.ok(response);
  }

  @LogMonitoring
  @DeleteMapping("/{post-id}")
  public ResponseEntity<Void> deletePost(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId
  ) {
    postService.deletePost(currentUser.getUserId(), postId);
    return ResponseEntity.noContent().build();
  }

  @LogMonitoring
  @PostMapping("/{post-id}/likes")
  public ResponseEntity<PostLikeResponse> toggleLike(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @PathVariable(name = "post-id") UUID postId
  ) {
    PostLikeResponse response = postLikeService.toggleLike(currentUser.getUserId(), postId);
    return ResponseEntity.ok(response);
  }
}
