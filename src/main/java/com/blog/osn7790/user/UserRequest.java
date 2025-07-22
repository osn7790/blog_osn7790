package com.blog.osn7790.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO {
        @NotBlank(message = "이름은 필수입니다")
        private String username;
        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 입력해주세요")
        private String password;
        @NotEmpty(message = "이메일은 필수입니다")
        @Pattern(
                regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$",
                message = "이메일 형식으로 작성해주세요"
        )
        private String email;

        public User toEntity() {
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }
        // 유효성 검사는 AOP에게 넘김. 컨트롤러에서 @Valid로 처리

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        @NotBlank(message = "사용자명을 입력해주세요")
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요")  // null과 빈 문자열 자동 검사
        @Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 입력해주세요")
        private String password;


    }

    @Data
    public static class UpdateDTO{
        @NotBlank(message = "비밀번호를 입력해주세요")  // null과 빈 문자열 자동 검사
        @Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 입력해주세요")
        private String password;
        @NotEmpty(message = "이메일은 필수입니다")  // 필수 입력 검증
        @Pattern(
                regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$",
                message = "이메일 형식으로 작성해주세요"
        )
        private String email;

    }


}
