package shop.dontouch.dontouch_be.domain.community.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.community.entity.Post;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

  private UUID postId;
  private UUID authorId;
  private String authorNickname;
  private String authorProfileImageUrl;
  private String title;
  private String content;
  private int viewCount;
  private int likeCount;
  private int commentCount;
  private boolean liked;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static PostResponse of(Post post, boolean liked) {
    return PostResponse.builder()
        .postId(post.getId())
        .authorId(post.getUser().getId())
        .authorNickname(post.getUser().getNickname())
        .authorProfileImageUrl(post.getUser().getProfileImageUrl())
        .title(post.getTitle())
        .content(post.getContent())
        .viewCount(post.getViewCount())
        .likeCount(post.getLikeCount())
        .commentCount(post.getCommentCount())
        .liked(liked)
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
