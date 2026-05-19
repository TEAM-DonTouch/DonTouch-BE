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

import shop.dontouch.dontouch_be.domain.user.constant.Gender;
import shop.dontouch.dontouch_be.domain.user.constant.JobType;
import shop.dontouch.dontouch_be.domain.user.constant.Region;
import shop.dontouch.dontouch_be.domain.user.constant.Role;
import shop.dontouch.dontouch_be.domain.user.constant.Status;
import shop.dontouch.dontouch_be.domain.user.dto.UserDto;
import shop.dontouch.dontouch_be.global.common.BaseEntity;

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

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(nullable = false, unique = true, length = 30)
  private String nickname;

  @Column(length = 500)
  private String profileImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Role userRole = Role.GENERAL_USER;

  @Column()
  private Integer age;

  @Enumerated(EnumType.STRING)
  @Column(length = 15)
  @Builder.Default
  private Gender gender = Gender.NOT_SELECTED;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private JobType userJobType = JobType.OTHER;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Region userRegion = Region.SEOUL;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private Status userStatus = Status.ACTIVE;

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

  public void updateStatus(Status userStatus) {
    this.userStatus = Objects.requireNonNull(userStatus, "userStatus must not be null");
  }

  public void updateRole(Role userRole) {
    this.userRole = Objects.requireNonNull(userRole, "userRole must not be null");
  }

  public void withdraw() {
    this.userStatus = Status.WITHDRAWN;
  }
}
