package board.jpa.querydsl.service;

import board.jpa.querydsl.dto.member.MemberConvertDTO;
import board.jpa.querydsl.dto.member.MemberCreateDTO;
import board.jpa.querydsl.dto.member.MemberListDTO;
import board.jpa.querydsl.dto.member.MemberUpdateDTO;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;

public interface MemberService {
    void createMember(MemberCreateDTO memberCreateDTO);
    MemberConvertDTO readMember(String email);
    void updateMember(MemberUpdateDTO memberUpdateDTO);
    void deleteMember(String email);
    PageResponseDTO<MemberListDTO> listMember(PageRequestDTO pageRequestDTO);
}
