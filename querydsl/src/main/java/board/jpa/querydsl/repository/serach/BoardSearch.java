package board.jpa.querydsl.repository.serach;

import board.jpa.querydsl.dto.BoardListDTO;
import board.jpa.querydsl.util.PageRequestDTO;
import board.jpa.querydsl.util.PageResponseDTO;

public interface BoardSearch {
    PageResponseDTO<BoardListDTO> listBoard(PageRequestDTO pageRequestDTO); 
    
}
