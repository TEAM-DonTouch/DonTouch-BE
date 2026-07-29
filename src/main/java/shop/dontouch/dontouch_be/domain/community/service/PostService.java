package shop.dontouch.dontouch_be.domain.community.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dontouch.dontouch_be.domain.community.dto.request.PostRequest;
import shop.dontouch.dontouch_be.domain.community.dto.request.PostUpdateRequest;
import shop.dontouch.dontouch_be.domain.community.dto.response.PostResponse;
import shop.dontouch.dontouch_be.domain.community.entity.Post;
import shop.dontouch.dontouch_be.domain.community.repository.PostLikeRepository;
import shop.dontouch.dontouch_be.domain.community.repository.PostRepository;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.domain.user.repository.UserRepository;
import shop.dontouch.dontouch_be.global.common.dto.PageResponse;
import shop.dontouch.dontouch_be.global.exception.CustomException;
import shop.dontouch.dontouch_be.global.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

  private static final String SORT_POPULAR = "popular";
  private static final String SORT_VIEWS = "views";

  private final PostRepository postRepository;
  private final PostLikeRepository postLikeRepository;
  private final UserRepository userRepository;

  @Transactional
  public PostResponse createPost(UUID userId, PostRequest request) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> {
        log.warn("createPost: 존재하지 않는 userId={}", userId);
        return new CustomException(ErrorCode.USER_NOT_FOUND);
      });

    Post post = Post.builder()
      .user(user)
      .title(request.getTitle())
      .content(request.getContent())
      .build();

    Post savedPost = postRepository.save(post);
    log.info("게시글 작성 완료: postId={}, userId={}", savedPost.getId(), userId);

    return PostResponse.of(savedPost, false);
  }

  public PageResponse<PostResponse> getPosts(UUID userId, String sort, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, resolveSort(sort));
    Page<Post> posts = postRepository.findAllWithUser(pageable);

    List<UUID> postIds = posts.map(Post::getId).toList();

    Set<UUID> likedPostIds = postIds.isEmpty()
      ? Set.of()
      : Set.copyOf(postLikeRepository.findLikedPostIdsByUserIdAndPostIdIn(userId, postIds));

    Page<PostResponse> responses = posts.map(
      post -> PostResponse.of(post, likedPostIds.contains(post.getId()))
    );

    return PageResponse.from(responses);
  }

  @Transactional
  public PostResponse getPost(UUID userId, UUID postId) {
    int updated = postRepository.increaseViewCount(postId);

    if (updated == 0) {
      log.warn("getPost: 존재하지 않는 postId={}", postId);
      throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }

    Post post = postRepository.findByIdWithUser(postId)
      .orElseThrow(() -> {
        log.warn("getPost: 삭제된 postId={}", postId);
        return new CustomException(ErrorCode.POST_NOT_FOUND);
      });

    boolean isLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);

    return PostResponse.of(post, isLiked);
  }

  @Transactional
  public PostResponse updatePost(UUID userId, UUID postId, PostUpdateRequest request) {
    Post post = postRepository.findByIdWithUser(postId)
      .orElseThrow(() -> {
        log.warn("updatePost: 존재하지 않는 postId={}", postId);
        return new CustomException(ErrorCode.POST_NOT_FOUND);
      });

    if (!post.getUser().getId().equals(userId)) {
      log.warn("updatePost: 작성자가 아닌 사용자의 수정 시도, postId={}, userId={}", postId, userId);
      throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
    }

    post.updatePost(request.getTitle(), request.getContent());

    // updatedAt(@LastModifiedDate)은 flush 시점에 갱신되므로, 응답 전에 명시적으로 flush 한다
    Post updatedPost = postRepository.saveAndFlush(post);
    log.info("게시글 수정 완료: postId={}, userId={}", postId, userId);

    boolean isLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);

    return PostResponse.of(updatedPost, isLiked);
  }

  @Transactional
  public void deletePost(UUID userId, UUID postId) {
    Post post = postRepository.findByIdWithUser(postId)
      .orElseThrow(() -> {
        log.warn("deletePost: 존재하지 않는 postId={}", postId);
        return new CustomException(ErrorCode.POST_NOT_FOUND);
      });

    if (!post.getUser().getId().equals(userId)) {
      log.warn("deletePost: 작성자가 아닌 사용자의 삭제 시도, postId={}, userId={}", postId, userId);
      throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
    }

    post.delete();
    log.info("게시글 삭제 완료: postId={}, userId={}", postId, userId);
  }

  private Sort resolveSort(String sort) {
    if (SORT_POPULAR.equalsIgnoreCase(sort)) {
      return Sort.by(Sort.Direction.DESC, "likeCount")
        .and(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    if (SORT_VIEWS.equalsIgnoreCase(sort)) {
      return Sort.by(Sort.Direction.DESC, "viewCount")
        .and(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    return Sort.by(Sort.Direction.DESC, "createdAt");
  }
}
