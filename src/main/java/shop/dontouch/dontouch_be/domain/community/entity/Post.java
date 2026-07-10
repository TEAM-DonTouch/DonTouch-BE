package shop.dontouch.dontouch_be.domain.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import shop.dontouch.dontouch_be.domain.user.entity.User;
import shop.dontouch.dontouch_be.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SQLRestriction("deleted_at IS NULL")
public class Post extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, length = 30)
  private String title;

  @Column(nullable = false, length = 255)
  private String content;

  @Column(nullable = false)
  @Builder.Default
  private int viewCount = 0;

  @Column(nullable = false)
  @Builder.Default
  private int likeCount = 0;

  @Column(nullable = false)
  @Builder.Default
  private int commentCount = 0;

  public void updatePost(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public void increaseViewCount() {
    this.viewCount += 1;
  }

  public void increaseLikeCount() {
    this.likeCount += 1;
  }

  public void decreaseLikeCount() {
    this.likeCount = Math.max(0, this.likeCount - 1);
  }

  public void increaseCommentCount() {
    this.commentCount += 1;
  }

  public void decreaseCommentCount() {
    this.commentCount = Math.max(0, this.commentCount - 1);
  }
}
