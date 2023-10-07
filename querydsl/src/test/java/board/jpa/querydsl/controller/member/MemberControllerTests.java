package board.jpa.querydsl.controller.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        MemberConvertDTO list = MemberConvertDTO.builder()
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
        Map<String, Object> modelMap = result.getModelAndView().getModel();
        ResponseEntity response = (ResponseEntity) modelMap.get("response");
        log.info("ResponseEntity: " + response.toString());
        log.info("=== End Get Read Member Controller Test ===");
    }

    
}
