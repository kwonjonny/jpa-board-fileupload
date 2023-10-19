package board.jpa.querydsl.service;

import board.jpa.querydsl.dto.reply.ReplyCreateDTO;
import board.jpa.querydsl.dto.reply.ReplyDTO;
import board.jpa.querydsl.dto.reply.ReplyListDTO;
import board.jpa.querydsl.dto.reply.ReplyUpdateDTO;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;

public interface ReplyService {
    Long createReply(ReplyCreateDTO replyCreateDTO);
    ReplyDTO readReply(Long rno);
    Long deleteReply(Long rno);
    PageResponseDTO<ReplyListDTO> listReply(PageRequestDTO pageRequestDTO, Long bno);
    Long updateReply(ReplyUpdateDTO replyUpdateDTO);
}
