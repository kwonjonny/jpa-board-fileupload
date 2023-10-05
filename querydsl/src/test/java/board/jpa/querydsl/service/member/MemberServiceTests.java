package board.jpa.querydsl.service.member;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.dto.member.MemberConvertDTO;
import board.jpa.querydsl.dto.member.MemberCreateDTO;
import board.jpa.querydsl.dto.member.MemberListDTO;
import board.jpa.querydsl.dto.member.MemberUpdateDTO;
import board.jpa.querydsl.service.MemberService;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    private static final String JUNIT_TEST_MEMBER_EMAIL = "thistrik@naver.com";
    private static final String JUNIT_TEST_MEMBER_PHONE = "010-3099-0648";
    private static final String JUNIT_TEST_MEMBER_NAME = "권성준";
    private static final String JUNIT_TEST_MEMBER_ROLE = "ROLE_USER";
    private static final String JUNIT_TEST_MEMBER_PW = "thistrik1!";

    private MemberCreateDTO memberCreateDTO;
    private MemberUpdateDTO memberUpdateDTO;

    @BeforeEach
    public void setUp() {
        memberCreateDTO = MemberCreateDTO.builder()
                .email(JUNIT_TEST_MEMBER_EMAIL)
                .memberPhone(JUNIT_TEST_MEMBER_PHONE)
                .memberPw(JUNIT_TEST_MEMBER_PW)
                .memberName(JUNIT_TEST_MEMBER_NAME)
                .build();

        memberUpdateDTO = MemberUpdateDTO.builder()
                .email(JUNIT_TEST_MEMBER_EMAIL)
                .memberPhone(JUNIT_TEST_MEMBER_PHONE)
                .memberPw(JUNIT_TEST_MEMBER_PW)
                .memberName(JUNIT_TEST_MEMBER_NAME)
                .createDate(LocalDate.now())
                .build();
    }

    @Test
    @Transactional
    @DisplayName("Proxy: 프록시 테스트")
    public void proxyCheckTest() {
        log.info(memberService.getClass().getName());
    }

    @Test
    @Transactional
    @DisplayName("Service: 회원 가입 테스트")
    public void createMemberTest() {
        // GIVEN
        log.info("=== Start Create Member Service Test ===");
        // WHEN
        memberService.createMember(memberCreateDTO);
        // THEN
        Assertions.assertNotNull(memberCreateDTO.getEmail());
        Assertions.assertNotNull(memberCreateDTO.getMemberName());
        Assertions.assertNotNull(memberCreateDTO.getMemberPhone());
        Assertions.assertNotNull(memberCreateDTO.getMemberPw());
        log.info("=== End Create Member Service Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 회원 조회 테스트")
    public void readMemberTest() {
        // GIVEN
        log.info("=== Start Read Member Service Test ===");
        // WHEN
        MemberConvertDTO readMember = memberService.readMember(JUNIT_TEST_MEMBER_EMAIL);
        // THEN
        log.info("readMember : " + readMember);
        Assertions.assertNotNull(readMember.getEmail());
        Assertions.assertNotNull(readMember.getMemberPhone());
        Assertions.assertNotNull(readMember.getMemberPw());
        Assertions.assertNotNull(readMember.getIsVerified());
        Assertions.assertNotNull(readMember.getCreateDate());
        Assertions.assertNotNull(readMember.getUpdateDate());
        Assertions.assertNotNull(readMember.getRolenames());
        log.info("=== End Read Member Service Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 회원 업데이트 테스트")
    public void updateMemberTest() {
        // GIVEN
        log.info("=== Start Update Member Service Test ===");
        // WHEN
        memberService.updateMember(memberUpdateDTO);
        // THEN
        Assertions.assertNotNull(memberUpdateDTO.getEmail());
        Assertions.assertNotNull(memberUpdateDTO.getMemberName());
        Assertions.assertNotNull(memberUpdateDTO.getMemberPhone());
        Assertions.assertNotNull(memberUpdateDTO.getMemberPw());
        Assertions.assertNotNull(memberUpdateDTO.getCreateDate());
        log.info("=== End Update Member Service Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 회원 리스트, 통합검색, 날짜검색 테스트")
    public void listMemberTest() {
        log.info("=== Start List Member Service Test ===");
        // GIVEN
        String searchType = "epn";
        String keyword = "권성준";
        String startDate = "2023-09-27";
        String endDate = "2023-10-05";
        // WHEN
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type(searchType)
                .keyword(keyword)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .build();
        PageResponseDTO<MemberListDTO> listMember = memberService.listMember(pageRequestDTO);
        // THEN
        log.info("ListMember: " + listMember.getList());
        Assertions.assertNotNull(listMember);
        log.info("=== End List Member Service Test ===");
    }
}
