package shop.dontouch.dontouch_be.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.dontouch.dontouch_be.domain.user.constant.UserGender;
import shop.dontouch.dontouch_be.domain.user.constant.UserJobType;
import shop.dontouch.dontouch_be.domain.user.constant.UserRegion;
import shop.dontouch.dontouch_be.domain.user.constant.UserStatus;
import shop.dontouch.dontouch_be.domain.user.entity.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 정보 DTO")
public class UserDto {
  @Schema(description = "회원 ID")
  private UUID id;

  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "올바른 이메일 형식이 아닙니다.")
  @Schema(description = "이메일", example = "user@example.com")
  private String email;

  @NotBlank(message = "닉네임은 필수입니다.")
  @Size(min = 2, max = 50, message = "닉네임은 2자 이상 30자 이하여야 합니다.")
  @Schema(description = "닉네임", example = "돈더치")
  private String nickname;

  @Schema(description = "프로필 이미지")
  private String profileImageUrl;

  @Schema(description = "나이", example = "20")
  private Integer age;

  @Schema(description = "성별", example = "MALE")
  private UserGender gender;

  @Schema(description = "직업", example = "OTHER")
  private UserJobType userJobType;

  @Schema(description = "지역", example = "SEOUL")
  private UserRegion userRegion;

  @Schema(description = "계정 상태", example = "ACTIVE")
  private UserStatus userStatus;

  public static UserDto entityToDto(User entity) {
    return UserDto.builder()
        .id(entity.getId())
        .email(entity.getEmail())
        .nickname(entity.getNickname())
        .age(entity.getAge())
        .gender(entity.getGender())
        .userJobType(entity.getUserJobType())
        .userRegion(entity.getUserRegion())
        .userStatus(entity.getUserStatus())
        .build();
  }
}
