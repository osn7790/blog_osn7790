package com.blog.osn7790.board;

import com.blog.osn7790.reply.Reply;
import com.blog.osn7790.user.User;
import com.blog.osn7790.util.MyDateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "board_tb")
@Entity
public class Board {
    private static final Logger log = LoggerFactory.getLogger(Board.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 외래키 컬럼명 명시
    private User user;



    @CreationTimestamp
    private Timestamp createdAt;


    @Transient
    private boolean isBoardOwner;

    public Board(String title, String content, String username) {
        this.title = title;
        this.content = content;
    }

    public boolean isOwner(Long checkUserId) {
        log.info("게시글 소유자 확인 요청 - 작성자 : {}", checkUserId);
        return this.user.getId().equals(checkUserId);
    }


    public String getTime(){

        return MyDateUtil.timestampFormat(createdAt);
    }

    @OrderBy("id DESC") // 정렬 옵션 설정
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.REMOVE)
    List<Reply> replies = new ArrayList<>();





}
