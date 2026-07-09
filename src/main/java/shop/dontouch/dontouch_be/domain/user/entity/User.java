package shop.dontouch.dontouch_be.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;
import shop.dontouch.dontouch_be.domain.user.constant.UserRole;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.global.common.BaseEntity;
import shop.dontouch.dontouch_be.domain.user.constant.LoginPlatform;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false, unique = true, length = 30)
  private String loginId;

  @Column(nullable = false, length = 255)
  private String password;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  private LoginPlatform loginPlatform = LoginPlatform.LOCAL;

  @Column(length = 255)
  private String providerId;

  @Column(nullable = false, unique = true, length = 30)
  private String nickname;

  @Column(length = 500)
  private String profileImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private UserRole role = UserRole.GENERAL_USER;

  @Column
  private Integer age;

  @Enumerated(EnumType.STRING)
  @Column(length = 15)
  @Builder.Default
  private UserGender gender = UserGender.NOT_SELECTED;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private UserJobType jobType = UserJobType.OTHER;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private UserRegion region = UserRegion.SEOUL;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private UserStatus status = UserStatus.ACTIVE;

  public void updateUser(
      String nickname,
      String profileImageUrl,
      Integer age,
      UserGender gender,
      UserJobType jobType,
      UserRegion region
  ) {
    if (nickname != null) {
      this.nickname = nickname;
    }

    if (profileImageUrl != null) {
      this.profileImageUrl = profileImageUrl;
    }

    if (age != null) {
      this.age = age;
    }

    if (gender != null) {
      this.gender = gender;
    }

    if (jobType != null) {
      this.jobType = jobType;
    }

    if (region != null) {
      this.region = region;
    }
  }

  public void updateStatus(UserStatus userStatus) {
    this.status = Objects.requireNonNull(userStatus, "userStatus must not be null");
  }

  public void updateRole(UserRole userRole) {
    this.role = Objects.requireNonNull(userRole, "userRole must not be null");
  }

  public void withdraw() {
    this.status = UserStatus.WITHDRAWN;
  }
}
