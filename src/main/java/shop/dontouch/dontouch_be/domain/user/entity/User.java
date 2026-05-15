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

import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;
import shop.dontouch.dontouch_be.domain.user.constant.UserRole;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.domain.user.dto.UserDto;
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

  @Column(nullable = false, unique = true, length = 30)
  private String nickname;

  @Column(length = 500)
  private String profileImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private UserRole userRole = UserRole.GENERAL_USER;

  @Column()
  private Integer age;

  @Enumerated(EnumType.STRING)
  @Column(length = 15)
  @Builder.Default
  private UserGender gender = UserGender.NOT_SELECTED;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private UserJobType userJobType = UserJobType.OTHER;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private UserRegion userRegion = UserRegion.SEOUL;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private UserStatus userStatus = UserStatus.ACTIVE;

  public void updateUser(UserDto dto) {
    if (dto.getNickname() != null) {
      this.nickname = dto.getNickname();
    }

    if (dto.getProfileImageUrl() != null) {
      this.profileImageUrl = dto.getProfileImageUrl();
    }

    if (dto.getAge() != null) {
      this.age = dto.getAge();
    }

    if (dto.getGender() != null) {
      this.gender = dto.getGender();
    }

    if (dto.getUserJobType() != null) {
      this.userJobType = dto.getUserJobType();
    }

    if (dto.getUserRegion() != null) {
      this.userRegion = dto.getUserRegion();
    }
  }

  public void updateStatus(UserStatus userStatus) {
    this.userStatus = userStatus;
  }

  public void updateRole(UserRole userRole) {
    this.userRole = userRole;
  }

  public void withdraw() {
    this.userStatus = UserStatus.WITHDRAWN;
  }
}
