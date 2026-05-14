package shop.dontouch.dontouch_be.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import shop.dontouch.dontouch_be.domain.user.constant.MemberGender;
import shop.dontouch.dontouch_be.domain.user.constant.MemberJobType;
import shop.dontouch.dontouch_be.domain.user.constant.MemberRegion;
import shop.dontouch.dontouch_be.domain.user.constant.MemberRole;
import shop.dontouch.dontouch_be.domain.user.constant.MemberStatus;
import shop.dontouch.dontouch_be.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(nullable = false, unique = true, length = 10)
  private String nickname;

  @Column(length = 500)
  private String profileImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private MemberRole memberRole = MemberRole.GENERAL_USER;

  @Column(nullable = false)
  private int age;

  @Enumerated(EnumType.STRING)
  @Column(length = 15)
  @Builder.Default
  private MemberGender gender = MemberGender.NOT_SELECTED;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private MemberJobType memberJobType = MemberJobType.OTHER;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private MemberRegion memberRegion = MemberRegion.SEOUL;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private MemberStatus memberStatus = MemberStatus.ACTIVE;
}
