package board.jpa.querydsl.repository.serach;

import board.jpa.querydsl.dto.member.MemberListDTO;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;

public interface MemberSearch {
    PageResponseDTO<MemberListDTO> listMember(PageRequestDTO pageRequestDTO);
}
