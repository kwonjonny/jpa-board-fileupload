package board.jpa.querydsl.controller.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.controller.MemberController;
import board.jpa.querydsl.dto.member.MemberConvertDTO;
import board.jpa.querydsl.dto.member.MemberCreateDTO;
import board.jpa.querydsl.dto.member.MemberUpdateDTO;
import board.jpa.querydsl.response.ResponseEntity;
import board.jpa.querydsl.service.MemberService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTests {

    // MockMvc 의존 주입
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    private static final String JUNIT_TEST_EMAIL = "thistrik@naver.com";
    private static final String JUNIT_TEST_MEMBERPW = "thistrik1!";
    private static final String JUNIT_TEST_MEMBER_PHONE = "010-3099-0648";
    private static final String JUNIT_TEST_MEMBER_NAME = "권성준";
    private static final String JUNIT_TEST_MEMBER_ROLE = "ROLE_USER";

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void setUp() throws Exception {
        autoCloseable = MockitoAnnotations.openMocks(this);
        Mockito.reset(memberService);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @Transactional
    @DisplayName("Controller: 회원 조회 테스트")
    public void getReadMemberTest() throws Exception {
        log.info("=== Start Get Read Member Controller Test ===");
        // GIVEN
        MemberConvertDTO list = MemberConvertDTO
                .builder()
                .email(JUNIT_TEST_EMAIL)
                .memberPhone(JUNIT_TEST_MEMBER_PHONE)
                .memberName(JUNIT_TEST_MEMBER_NAME)
                .rolenames(Arrays.asList(JUNIT_TEST_MEMBER_ROLE))
                .build();
        ResponseEntity<MemberConvertDTO> responseEntity = ResponseEntity.success(list);
        // WHEN
        given(memberService.readMember(JUNIT_TEST_EMAIL)).willReturn(list);
        // THEN
        MvcResult result = mockMvc.perform(get("/spring/member/read/{email}", JUNIT_TEST_EMAIL))
                .andExpect(status().isOk())
                .andExpect(view().name("spring/member/read"))
                .andExpect(model().attributeExists("response"))
                .andReturn();
        Map<String, Object> flashAttributes = result.getModelAndView().getModel();
        Object response = flashAttributes.get("response");
        log.info("Response Flash Attribute: {}", response);
        log.info("=== End Get Read Member Controller Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Controller: 회원 가입 테스트")
    public void postCreateMemberTest() throws Exception {
        log.info("=== Start POST Create Member Controller Test ====");
        // GIVEN
        MemberCreateDTO memberCreateDTO = MemberCreateDTO
                .builder()
                .email(JUNIT_TEST_EMAIL)
                .memberPw(JUNIT_TEST_MEMBERPW)
                .memberName(JUNIT_TEST_MEMBER_NAME)
                .memberPhone(JUNIT_TEST_MEMBER_PHONE)
                .build();
        doAnswer(invocation -> {
            // void 메서드이므로 null을 리턴
            return null;
        }).when(memberService).createMember(any(MemberCreateDTO.class));
        // WHEN & THEN
        MvcResult result = mockMvc.perform(post("/spring/member/create")
                .param("email", JUNIT_TEST_EMAIL)
                .param("memberPw", JUNIT_TEST_MEMBERPW)
                .param("memberName", JUNIT_TEST_MEMBER_NAME)
                .param("memberPhone", JUNIT_TEST_MEMBER_PHONE)).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/spring/index"))
                .andExpect(flash().attributeExists("response"))
                .andReturn();
        Map<String, Object> flashAttributes = result.getModelAndView().getModel();
        Object response = flashAttributes.get("response");
        log.info("Response Flash Attribute: {}", response);
        log.info("=== End POST Create Member Controller Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Controller: 회원 정보 수정 페이지 테스트")
    public void getUpdateMemberTest() throws Exception {
        log.info("=== Start Get Update Member Controller Test ===");
        // GIVEN
        MemberConvertDTO list = MemberConvertDTO
                .builder()
                .email(JUNIT_TEST_EMAIL)
                .memberName(JUNIT_TEST_MEMBERPW)
                .memberPw(JUNIT_TEST_MEMBERPW)
                .isVerified(String.valueOf('N'))
                .createDate(LocalDate.now())
                .updateDate(LocalDate.now())
                .memberPhone(JUNIT_TEST_MEMBER_PHONE)
                .rolenames(Arrays.asList(JUNIT_TEST_MEMBER_ROLE))
                .build();
        ResponseEntity<MemberConvertDTO> responseEntity = ResponseEntity.success(list);
        // WHEN
        given(memberService.readMember(JUNIT_TEST_EMAIL)).willReturn(list);
        // THEN
        MvcResult result = mockMvc.perform(get("/spring/member/update/{email}", JUNIT_TEST_EMAIL))
                .andExpect(status().isOk())
                .andExpect(view().name("spring/member/update"))
                .andExpect(model().attributeExists("response"))
                .andReturn();
        Map<String, Object> flashAttributes = result.getModelAndView().getModel();
        Object response = flashAttributes.get("response");
        log.info("Response Flash Attribute: {}", response);
        log.info("=== End Get Update Member Controller Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Controller: 회원 정보 수정 테스트")
    public void postUpdateMemberTest() throws Exception {
        log.info("=== Start POST Update Member Controller Test ===");
        // GIVEN
        MemberUpdateDTO memberUpdateDTO = MemberUpdateDTO
                .builder()
                .email(JUNIT_TEST_EMAIL)
                .memberPw(JUNIT_TEST_MEMBERPW)
                .memberName(JUNIT_TEST_MEMBER_NAME)
                .memberPhone(JUNIT_TEST_MEMBER_PHONE)
                .createDate(LocalDate.now())
                .updateDate(LocalDate.now())
                .build();
        doAnswer(invocation -> {
            // void 메서드이므로 null을 리턴
            return null;
        }).when(memberService).updateMember(any(MemberUpdateDTO.class));
        // WHEN & THEN
        MvcResult result = mockMvc.perform(post("/spring/member/update")
                .param("email", JUNIT_TEST_EMAIL)
                .param("memberPw", JUNIT_TEST_MEMBERPW)
                .param("memberPhone", JUNIT_TEST_MEMBER_PHONE)
                .param("memberName", JUNIT_TEST_MEMBER_NAME)
                .param("createDate", String.valueOf(LocalDate.now()))
                .param("updateDate", String.valueOf(LocalDate.now())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/spring/member/read/" + JUNIT_TEST_EMAIL))
                .andExpect(flash().attributeExists("response"))
                .andReturn(); // MvcResult를 반환받습니다.
        // Flash attribute 로그 출력
        Map<String, Object> flashAttributes = result.getFlashMap();
        Object response = flashAttributes.get("response");
        log.info("Response Flash Attribute: {}", response);
        log.info("=== End POST Update Member Controller Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Controller: 회원 탈퇴 테스트")
    public void postDeleteMemberTest() throws Exception {
        // GIVEN
        log.info("=== Start POST Delete Member Controller Test ===");
        doAnswer(invocation -> {
            // void 메서드이므로 null을 리턴
            return null;
        }).when(memberService).deleteMember(JUNIT_TEST_EMAIL);
        // WHEN & THEN
        MvcResult result = mockMvc.perform(post("/spring/member/delete/" + JUNIT_TEST_EMAIL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/spring/index"))
                .andExpect(flash().attributeExists("response"))
                .andReturn(); // MvcResult를 반환받습니다.
        // Flash attribute 로그 출력
        Map<String, Object> flashAttributes = result.getFlashMap();
        Object response = flashAttributes.get("response");
        log.info("Response Flash Attribute: {}", response);
        log.info("=== End POST Delete Member Controller Test ===");
    }

}
