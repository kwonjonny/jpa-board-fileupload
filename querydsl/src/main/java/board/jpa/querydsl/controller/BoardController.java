package board.jpa.querydsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import board.jpa.querydsl.dto.board.BoardCreateDTO;
import board.jpa.querydsl.dto.board.BoardDTO;
import board.jpa.querydsl.dto.board.BoardListDTO;
import board.jpa.querydsl.dto.board.BoardUpdateDTO;
import board.jpa.querydsl.response.ResponseEntity;
import board.jpa.querydsl.service.BoardService;
import board.jpa.querydsl.util.cookie.ManagementCookie;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("spring/board/")
public class BoardController {

    private final BoardService boardService;
    private final ManagementCookie managementCookie;

    @Autowired
    public BoardController(final BoardService boardService, final ManagementCookie managementCookie) {
        log.info("Inject BoardService");
        this.boardService = boardService;
        this.managementCookie = managementCookie;
    }

    // GET | Create Board
    @GetMapping("create")
    public String getCreateBoard(Model model) {
        log.info("GET | Create Board Controller");
        model.addAttribute("response", ResponseEntity.success("게시글 생성 페이지입니다."));
        return "spring/board/create";
    }

    // POST | Create Board
    @PostMapping("create")
    public String postCreateBoard(@Valid final BoardCreateDTO boardCreateDTO,
            final RedirectAttributes redirectAttributes) {
        log.info("POST | Create Board Controller");
        final Long createBoard = boardService.createBoard(boardCreateDTO);
        redirectAttributes.addFlashAttribute("response", ResponseEntity.success("게시물 생성 완료."));
        return "redirect:/spring/board/list";
    }

    // GET | Read Board
    @GetMapping("read/{bno}")
    public String getReadBoard(@PathVariable("bno") final Long bno, final Model model, final HttpServletRequest request,
            final HttpServletResponse response) {
        log.info("GET | Read Board Controller");
        if (managementCookie.createCookie(request, response, bno)) {
            log.info("Making Cookie");
            boardService.incrementViewCount(bno);
        }
        final BoardDTO list = boardService.readBoard(bno);
        model.addAttribute("response", ResponseEntity.success(list));
        return "spring/board/read";
    }

    // GET | List Board
    @GetMapping("list")
    public String getListBoard(final PageRequestDTO pageRequestDTO, final Model model) {
        log.info("GET | List Board Controller");
        final PageResponseDTO<BoardListDTO> list = boardService.listBoard(pageRequestDTO);
        model.addAttribute("response", ResponseEntity.success(list));
        return "spring/board/list";
    }

    // GET | Update Board
    @GetMapping("update/{bno}")
    public String getUpdateBoard(@PathVariable("bno") final Long bno, final Model model) {
        log.info("GET | Update Board Controller");
        final BoardDTO list = boardService.readBoard(bno);
        model.addAttribute("response", ResponseEntity.success(list));
        return "spring/board/update";
    }

    // POST | Update Board
    @PostMapping("update")
    public String postUpdateBoard(@Valid final BoardUpdateDTO boardUpdateDTO,
            final RedirectAttributes redirectAttributes) {
        log.info("POST | Update Board Controller");
        final Long updateBoard = boardService.updateBoard(boardUpdateDTO);
        redirectAttributes.addFlashAttribute("response", ResponseEntity.success("게시물 수정 완료."));
        return "redirect:/spring/board/read/" + boardUpdateDTO.getBno();
    }

    // POST | Delete Board
    @PostMapping("delete/{bno}")
    public String postDeleteBoard(@PathVariable("bno") final Long bno, final RedirectAttributes redirectAttributes) {
        log.info("POST | Delete Board Controller");
        final Long deleteBoard = boardService.deleteBoard(bno);
        redirectAttributes.addFlashAttribute("message", ResponseEntity.success("게시물 삭제 완료."));
        return "redirect:/spring/board/list";
    }
}