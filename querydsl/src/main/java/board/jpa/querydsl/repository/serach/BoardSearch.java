package board.jpa.querydsl.repository.serach;

import board.jpa.querydsl.dto.board.BoardListDTO;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;

public interface BoardSearch {
    PageResponseDTO<BoardListDTO> listBoard(PageRequestDTO pageRequestDTO); 
}
