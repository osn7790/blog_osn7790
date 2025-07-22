package com.blog.osn7790.board;

import com.blog.osn7790._core.common.PageLink;
import com.blog.osn7790._core.errors.exception.Exception400;
import com.blog.osn7790._core.errors.exception.Exception403;
import com.blog.osn7790.user.User;
import com.blog.osn7790.util.Define;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 수정하기 화면 요청

    /**
     * 게시글 수정 화면 요청
     */
    @GetMapping("/board/{id}/board-update")
    public String updateForm(@PathVariable(name = "id") Long boardId, HttpServletRequest request, HttpSession session) {
        log.info("게시글 수정 폼 요청 - boardId : {}", boardId);

        Board board = boardService.findById(boardId);
        User user = (User)session.getAttribute(Define.SESSION_USER);
        if(!board.isOwner(user.getId())) {
            throw new Exception403("본인 게시글만 수정 할 수 있습니다.");
        }
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.checkBoardOwner(boardId, sessionUser.getId());
        request.setAttribute("board", boardService.findById(boardId));
        return "board/board-update";

    }


    @PostMapping("/board/{id}/update-form")
    public String update(@Valid BoardRequest.UpdateDTO updateDTO, Errors errors, @PathVariable(name = "id") Long boardId, HttpSession session) {

        log.info("게시글 수정 기능 요청 - boardId : {}, 새 제목 {} ", boardId, updateDTO.getTitle());

        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);

        if (errors.hasErrors()) {
            FieldError firstError = errors.getFieldErrors().get(0);
            throw new Exception400(firstError.getDefaultMessage());
        }

        boardService.updateById(boardId, updateDTO, sessionUser);
        return "redirect:/board/" + boardId;
    }



    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") long id, HttpSession session) {

        log.info("게시글 삭제 요청 - ID : {}", id);

        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.deleteById(id,sessionUser);

        return "redirect:/";
    }


    // 게시글 작성 화면 요청

    @GetMapping("/board/save-form")
    public String saveForm(HttpSession session) {

        log.info("게시글 삭제 요청 ");
        return "board/save-form";
    }


    @PostMapping("/board/save")
    public String save(@Valid BoardRequest.SaveDTO saveDTO, Errors errors, HttpSession session) {

        log.info("게시글 작성 기능 요청 ");
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.save(saveDTO, sessionUser);

        return "redirect:/";
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "size", defaultValue = "3") int size) {
        // 현재 페이지 번호, 한 페이지당 게시글 갯수
        Pageable pageable = PageRequest.of(page -1, size, Sort.by("id").descending());
        Page<Board> boardPage = boardService.findAll(pageable);

        List<PageLink> pageLinks = new ArrayList<>();
        for(int i = 0; i < boardPage.getTotalPages(); i++) {
            pageLinks.add(new PageLink(i, i + 1, i == boardPage.getNumber()));
        }

        Integer previousPageNumber = boardPage.hasPrevious() ? boardPage.getNumber() : null;
        Integer nextPageNumber = boardPage.hasNext() ? boardPage.getNumber() + 2 : null;


        model.addAttribute("boardPage", boardPage);
        model.addAttribute("pageLinks", pageLinks);
        model.addAttribute("previousPageNumber", previousPageNumber);
        model.addAttribute("nextPageNumber", nextPageNumber);

        return "index";
    }


    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model, HttpSession session) {
        log.info("게시글 상세 보기 요청 - ID : {}", id);

        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        Board board = boardService.findByIdWithReplies(id, sessionUser);
        model.addAttribute("board", board);
        return "board/detail";
    }

}
