package com.blog.osn7790.board;

import com.blog.osn7790._core.errors.exception.Exception403;
import com.blog.osn7790._core.errors.exception.Exception404;
import com.blog.osn7790.reply.Reply;
import com.blog.osn7790.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {
    private final BoardJpaRepository boardJpaRepository;

    /**
     * 게시글 저장
     */
    @jakarta.transaction.Transactional
    public Board save(@Valid BoardRequest.SaveDTO saveDTO, User sessionUser) {

        log.info("게시글 저장 서비스 처리 시작 - 제목 {} , 작성자 {}",
                saveDTO.getTitle(), sessionUser.getUsername());

        Board board = saveDTO.toEntity(sessionUser);
        boardJpaRepository.save(board);

        log.info("게시글 저장 완료 - ID {} , 제목 {}",
                board.getId(), board.getTitle());

        return board;
    }

    /**
     * 게시글 목록 조회 - 페이징 처리
     */
    public Page<Board> findAll(Pageable pageable) {

        Page<Board> boardPage = boardJpaRepository.findAllJoinUser(pageable);
        return boardPage;
    }

    //
    public Board findByIdWithReplies(Long id, User sessionUser) {
        // 1. 게시글 조회
        Board board = boardJpaRepository.findByIdJoinUser(id).orElseThrow(
                () ->  new Exception404("게시글을 찾을 수 없습니다"));
        if(sessionUser  != null) {
            boolean isBoardOwner = board.isOwner(sessionUser.getId());
            board.setBoardOwner(isBoardOwner);
        }

        List<Reply> replies = board.getReplies();

        // 댓글 소유권 설정 (삭제 버튼 표시용)
        if(sessionUser != null) {

            replies.forEach(reply -> {
                boolean isReplyOwner = reply.isOwner(sessionUser.getId());
                reply.setReplyOwner(isReplyOwner);

            });
        }
        return board;
    }


    /**
     * 게시글 상세 조회
     */
    public Board findById(Long id) {

        log.info("게시글 상세 조회 서비스 시작 - ID {}", id);
        Board board = boardJpaRepository.findByIdJoinUser(id).orElseThrow(() -> {
            log.warn("게시글 조회 실패 - ID {}", id);
            return new Exception404("게시글을 찾을 수 없습니다");
        });
        log.info("게시글 상세 조회 완료 - 제목 {}", board.getTitle());
        return board;
    }

    /**
     *  게시글 수정(권한 체크 포함)
     */
    @jakarta.transaction.Transactional
    public Board updateById(Long id, @Valid BoardRequest.UpdateDTO updateDTO,
                            User sessionUser) {

        log.info("게시글 수정 서비스 시작 - 게시글 ID {}", id);
        Board board = boardJpaRepository.findById(id).orElseThrow(() ->{
            log.warn("게시글 조회 실패 - ID {}", id);
            return new Exception404("해당 게시글이 존재하지 않습니다.");
        });

        if(!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 수정 가능");
        }
        board.setTitle(updateDTO.getTitle());
        board.setContent(updateDTO.getContent());

        log.info("게시글 수정 완료 - 게시글 ID {}, 게시글 제목 {}", id, board.getTitle());
        return board;

    }

    /**
     * 게시글 삭제
     */
    @jakarta.transaction.Transactional
    public void deleteById(Long id, User sessionUser){

        log.info("게시글 삭제 서비스 시작 - 게시글 ID {}", id);
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> {
            throw new Exception404("삭제할 게시글이 없습니다");
        });
        if(! board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 삭제할 수 있습니다");
        }
        boardJpaRepository.deleteById(id);
        log.info("게시글 삭제 완료 - 게시글 ID {}", id);
    }


    public void checkBoardOwner(Long boardId, Long userId) {
        Board board = findById(boardId);
        if(!board.isOwner(userId)) {
            throw new Exception403("본인 게시글만 수정할 수 있습니다.");
        }
    }

}
