package shop.dontouch.dontouch_be.domain.finance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Category extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "user_id", nullable = false)
//  private User user;

  @Column(nullable = false, unique = true, length = 10)
  private String name;

  public void updateName(String name) {
    this.name = name;
  }
}
