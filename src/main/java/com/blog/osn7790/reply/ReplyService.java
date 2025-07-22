package com.blog.osn7790.reply;

import com.blog.osn7790._core.errors.exception.Exception403;
import com.blog.osn7790._core.errors.exception.Exception404;
import com.blog.osn7790.board.Board;
import com.blog.osn7790.board.BoardJpaRepository;
import com.blog.osn7790.user.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor // final  키워드를 가진 멤버를 초기화 해
@Service // IoC 대상
public class ReplyService {

    private static final Logger log = LoggerFactory.getLogger(ReplyService.class);
    private final ReplyJpaRepository replyJpaRepository;
    private final BoardJpaRepository boardJpaRepository;

    // 댓글 저장 기능

    @Transactional
    public void save(ReplyRequest.SaveDTO saveDTO, User sessionUser) {

        log.info("댓글 저장 서비스 처리 시작 - 게시글 ID {}, 작성자 {} ",
                saveDTO.getBoardId(), sessionUser.getUsername());

        Board board = boardJpaRepository.findById(saveDTO.getBoardId())
                .orElseThrow(() -> new Exception404("존재하지 않는 게시글에는 댓글 작성 불가"));

        Reply reply = saveDTO.toEntity(sessionUser, board);

        replyJpaRepository.save(reply);

        }


    // 댓글 삭제 기능
    @Transactional
    public void deleteById(Long replyId, User sessionUser) {
        log.info("댓글 삭제 서비스 처리 시작 - 댓글 ID {}");

        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new Exception404("삭제할 댓글이 없습니다"));

        if(!reply.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 댓글만 삭제 할 수 있음");
        }

        replyJpaRepository.deleteById(replyId);

    }

}
