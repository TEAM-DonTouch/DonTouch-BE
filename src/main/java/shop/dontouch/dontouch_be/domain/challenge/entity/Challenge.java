package shop.dontouch.dontouch_be.domain.challenge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import shop.dontouch.dontouch_be.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SQLRestriction("deleted_at IS NULL")
public class Challenge extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false, length = 50)
  private String title;

  @Column(length = 255)
  private String description;

  @Column(nullable = false)
  private int targetValue;

  @Column(nullable = false)
  @Builder.Default
  private int participantCount = 0;

  public void increaseParticipantCount() {
    this.participantCount += 1;
  }
}
