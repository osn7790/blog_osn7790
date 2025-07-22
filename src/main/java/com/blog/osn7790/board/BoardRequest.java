package com.blog.osn7790.board;

import com.blog.osn7790.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class BoardRequest {

    // 게시글 저장 DTO
    @Data
    public static class SaveDTO {
        @NotNull(message = "제목은 필수입니다")
        private String title;
        @NotNull
        @Size(min = 4, max = 500, message = "내용은 4자 이상 500자 이하로 입력해주세요")
        private String content;


        public Board toEntity(User user) {
            return Board.builder()
                    .title(this.title)
                    .user(user)
                    .content(this.content)
                    .build();
        }


    }

    // 게시글 수정용 DTO 설계
    @Data
    public static class UpdateDTO {
        @NotNull(message = "제목은 필수입니다")
        private String title;
        @Size(min = 4, max = 500, message = "내용은 4자 이상 500자 이하로 입력해주세요")
        private String content;



    }
}
