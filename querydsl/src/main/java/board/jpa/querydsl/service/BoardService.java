package board.jpa.querydsl.service;

import board.jpa.querydsl.dto.board.BoardCreateDTO;
import board.jpa.querydsl.dto.board.BoardDTO;
import board.jpa.querydsl.dto.board.BoardListDTO;
import board.jpa.querydsl.dto.board.BoardUpdateDTO;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;

public interface BoardService {
    Long createBoard(BoardCreateDTO boardCreateDTO);

    BoardDTO readBoard(Long bno);

    Long updateBoard(BoardUpdateDTO boardUpdateDTO);

    Long deleteBoard(Long bno);

    PageResponseDTO<BoardListDTO> listBoard(PageRequestDTO pageRequest);

    Integer incrementViewCount(Long bno);
}
