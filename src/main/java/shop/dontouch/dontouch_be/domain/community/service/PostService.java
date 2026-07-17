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

  private final PostRepository postRepository;
  private final PostLikeRepository postLikeRepository;
  private final UserRepository userRepository;

  @Transactional
  public PostResponse createPost(UUID userId, PostRequest request) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Post post = Post.builder()
        .user(user)
        .title(request.getTitle())
        .content(request.getContent())
        .build();

    Post savedPost = postRepository.save(post);
    return PostResponse.of(savedPost, false);
  }

  public PageResponse<PostResponse> getPosts(
      UUID userId, String sort, int page, int size
  ) {
    Pageable pageable = PageRequest.of(page, size, resolveSort(sort));
    Page<Post> posts = postRepository.findAll(pageable);

    List<UUID> postIds = posts.map(Post::getId).toList();
    Set<UUID> likedPostIds = Set.copyOf(
        postLikeRepository.findLikedPostIdsByUserIdAndPostIdIn(userId, postIds)
    );

    Page<PostResponse> responses = posts.map(
        post -> PostResponse.of(post, likedPostIds.contains(post.getId()))
    );

    return PageResponse.from(responses);
  }

  @Transactional
  public PostResponse getPost(UUID userId, UUID postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    post.increaseViewCount();

    boolean isLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
    return PostResponse.of(post, isLiked);
  }

  @Transactional
  public PostResponse updatePost(UUID userId, UUID postId, PostRequest request) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    if (!post.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
    }

    post.updatePost(request.getTitle(), request.getContent());

    boolean isLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
    return PostResponse.of(post, isLiked);
  }

  @Transactional
  public void deletePost(UUID userId, UUID postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    if (!post.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
    }

    post.delete();
  }

  private Sort resolveSort(String sort) {
    if (SORT_POPULAR.equalsIgnoreCase(sort)) {
      return Sort.by(Sort.Direction.DESC, "likeCount")
          .and(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    return Sort.by(Sort.Direction.DESC, "createdAt");
  }
}
