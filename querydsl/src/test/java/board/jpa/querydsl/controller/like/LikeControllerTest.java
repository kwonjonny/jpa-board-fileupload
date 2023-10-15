package board.jpa.querydsl.controller.like;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import board.jpa.querydsl.controller.LikeController;
import board.jpa.querydsl.dto.like.LikeToggleDTO;
import board.jpa.querydsl.service.LikeService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
public class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private LikeController likeController;

    @Mock
    private LikeService likeService;

    private AutoCloseable autoCloseable;

    private static final String JUNIT_TEST_MEMBER_EMAIL = "thistrik@naver.com";
    private static final Long JUNIT_TEST_BNO = 7L;
    private static final Integer JUNIT_TEST_RETURN_COUNT = 1;
    private static final String JUNIT_TEST_AUTHORITIES = "ROLE_USER";

    @BeforeEach
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @Transactional
    @DisplayName("Controller: 라이크 토글 테스트")
    @WithMockUser(username = JUNIT_TEST_MEMBER_EMAIL, authorities = { JUNIT_TEST_AUTHORITIES })
    public void likeControllerToggle() throws Exception {
        // GIVEN
        log.info("=== Start Toggle Like Controller Test ===");
        // WHEN
        when(likeService.toggleLike(JUNIT_TEST_BNO, JUNIT_TEST_MEMBER_EMAIL)).thenReturn(1L);
        when(likeService.countLike(JUNIT_TEST_BNO)).thenReturn(JUNIT_TEST_RETURN_COUNT);
        // REQUEST
        MockHttpServletResponse response = mockMvc.perform(
                post("/spring/like/toggle/board/" + JUNIT_TEST_BNO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        // CHECK RESPONSE
        String responseBody = response.getContentAsString();
        log.info("Reponse Content: {}", responseBody);
        log.info("=== End Toggle Like Controller Test ===");
    }

    @Test
    @Transactional
    @DisplayName("라이크 카운트 테스트")
    @WithMockUser(username = JUNIT_TEST_MEMBER_EMAIL, authorities = { JUNIT_TEST_AUTHORITIES })
    public void likeCountTest() throws Exception {
        // GIVEN
        log.info("=== Start Check Like Count Controller Test ===");
        // WHEN
        when(likeService.countLike(JUNIT_TEST_BNO)).thenReturn(JUNIT_TEST_RETURN_COUNT);
        // REQUEST
        MockHttpServletResponse response = mockMvc.perform(
                get("/spring/like/count/board/" + JUNIT_TEST_BNO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        // CHECK RESPONSE
        String responseBody = response.getContentAsString();
        log.info("Reponse Content: {}", responseBody);
        log.info("=== End Check Like Count Controller Test ===");
    }

    @Test
    @Transactional
    @DisplayName("라이크 여부 체크 테스트")
    @WithMockUser(username = JUNIT_TEST_MEMBER_EMAIL, authorities = { JUNIT_TEST_AUTHORITIES })
    public void likeToggleBooleanTest() throws Exception {
        log.info("=== Start Check Like Boolean Controller Test ===");
        // GIVEN
        LikeToggleDTO likeToggleDTO = LikeToggleDTO.builder().build();
        // WHEN
        when(likeService.checkToggleMember(JUNIT_TEST_BNO, JUNIT_TEST_MEMBER_EMAIL));
        // REQUEST
        MockHttpServletResponse response = mockMvc.perform(
                get("/spring/like/check/board/member/" +JUNIT_TEST_BNO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        // CHECK RESPONSE
        String responseBody = response.getContentAsString();
        log.info("Reponse Content: {}", responseBody);
        log.info("=== End Check Like Boolean Controller Test ===");
    }
}
