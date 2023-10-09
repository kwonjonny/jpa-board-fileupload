package board.jpa.querydsl.repository.serach;

import board.jpa.querydsl.dto.like.LikeToggleDTO;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;

public interface LikeSearch {
    PageResponseDTO<LikeToggleDTO> listLike(PageRequestDTO pageRequestDTO);
}
