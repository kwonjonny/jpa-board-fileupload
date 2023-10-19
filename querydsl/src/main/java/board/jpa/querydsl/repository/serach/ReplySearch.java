package board.jpa.querydsl.repository.serach;

import board.jpa.querydsl.dto.reply.ReplyListDTO;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;

public interface ReplySearch {
    PageResponseDTO<ReplyListDTO> listReply(PageRequestDTO pageRequestDTO, Long bno);
}
