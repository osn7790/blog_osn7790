package com.blog.osn7790.reply;

import com.blog.osn7790._core.errors.exception.Exception400;
import com.blog.osn7790.board.Board;
import com.blog.osn7790.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class ReplyRequest {

    @Data
    public static class SaveDTO {
        private Long boardId;
        @NotNull
        @Size(min = 4, max = 200, message = "내용은 4자 이상 200자 이하로 입력해주세요")
        private String comment; // 댓글 내용


        /**
         * 보통 SAVE DTO에 toEntity 메서드를 만들게 된다
         * 멤버 변수에 없는 데이터가 필요할 때는
         * 외부에서 주입 받으면 된다.
         */

        public Reply toEntity(User sessionUser, Board board) {
            return Reply.builder()
                    .comment(comment.trim())
                    .user(sessionUser)
                    .board(board)
                    .build();
        }

    }

}
