package board.jpa.querydsl.service;


import board.jpa.querydsl.dto.BoardCreateDTO;
import board.jpa.querydsl.dto.BoardDTO;
import board.jpa.querydsl.dto.BoardListDTO;
import board.jpa.querydsl.dto.BoardUpdateDTO;
import board.jpa.querydsl.util.PageRequestDTO;
import board.jpa.querydsl.util.PageResponseDTO;

public interface BoardService {
    Long createBoard(BoardCreateDTO boardCreateDTO);
    BoardDTO readBoard(Long bno);
    Long updateBoard(BoardUpdateDTO boardUpdateDTO);
    Long deleteBoard(Long bno);
    PageResponseDTO<BoardListDTO> listBoard(PageRequestDTO pageRequest);
}
