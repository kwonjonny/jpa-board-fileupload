package board.jpa.querydsl.controller.board;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.controller.BoardController;
import board.jpa.querydsl.dto.board.BoardCreateDTO;
import board.jpa.querydsl.dto.board.BoardDTO;
import board.jpa.querydsl.dto.board.BoardListDTO;
import board.jpa.querydsl.dto.board.BoardUpdateDTO;
import board.jpa.querydsl.service.BoardService;
import board.jpa.querydsl.util.cookie.ManagementCookie;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTests {

    // MockMVC 의존성 주입
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    @Mock
    private ManagementCookie managementCookie;

    // 테스트 시작 전 메모리 선 참조
    private static final Long JUNIT_TEST_BNO = 2L;
    private static final String JUNIT_TEST_TITLE = "Junit_Test_Title";
    private static final String JUNIT_TEST_WRITER = "Junit_Test_Writer";
    private static final String JUNIT_TEST_CONTENT = "Junit_Test_Content";
    private static final LocalDate JUNIT_TEST_NOW = LocalDate.now();
    private static final String JUNIT_TEST_FILE_NAME = "Junit_Test_File.jpg";

    private AutoCloseable autoCloseable;
    private String uuid;

    @BeforeEach
    public void setup() throws Exception {
        autoCloseable = MockitoAnnotations.openMocks(this);
        Mockito.reset(boardService, managementCookie);
        uuid = UUID.randomUUID().toString();
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @Transactional
    @DisplayName("Controller: 게시판 컨트롤러 조회 테스트")
    void getReadBoardTest() throws Exception {
        log.info("=== Start Get Read Board Controller Test ===");
        // GIVEN
        BoardDTO list = BoardDTO.builder()
                .bno(JUNIT_TEST_BNO)
                .title(JUNIT_TEST_TITLE)
                .content(JUNIT_TEST_CONTENT)
                .writer(JUNIT_TEST_WRITER)
                .createDate(JUNIT_TEST_NOW)
                .updateDate(JUNIT_TEST_NOW)
                .build();
        // WHEN
        given(boardService.readBoard(JUNIT_TEST_BNO)).willReturn(list);
        given(managementCookie.createCookie(any(HttpServletRequest.class), any(HttpServletResponse.class), anyLong()))
                .willReturn(true);
        // THEN
        mockMvc.perform(get("/spring/board/read/{bno}", JUNIT_TEST_BNO))
                .andExpect(status().isOk())
                .andExpect(view().name("spring/board/read"))
                .andExpect(model().attributeExists("list"));
        log.info("==== End Get Rad Board Controller Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Controller: Board 컨트롤러 업데이트 조회 테스트")
    public void getUpdateBoardTest() throws Exception {
        log.info("=== Start GET Update Board Controller Test ===");
        // GIVEN
        BoardDTO list = BoardDTO.builder()
                .bno(JUNIT_TEST_BNO)
                .title(JUNIT_TEST_TITLE)
                .content(JUNIT_TEST_CONTENT)
                .writer(JUNIT_TEST_WRITER)
                .createDate(JUNIT_TEST_NOW)
                .updateDate(JUNIT_TEST_NOW)
                .build();
        // WHEN
        // 테스트 중에 호출될 때 mock BoardService가 반환될 내용을 지정
        given(boardService.readBoard(JUNIT_TEST_BNO)).willReturn(list);
        // THEN
        // GET | 요청 수행 후 응답 검증
        mockMvc.perform(get("/spring/board/update/{bno}", JUNIT_TEST_BNO))
                .andExpect(status().isOk())
                .andExpect(view().name("spring/board/update"))
                .andExpect(model().attributeExists("list"));
        log.info("=== End GET Update Board Controller Test ===");
    }  

    @Test
    @Transactional
    @DisplayName("Controller: Board 컨트롤러 삭제 테스트")
    public void postDeleteBoardTest() throws Exception {
        // GIVEN
        log.info("=== Start POST Delete Board Controller Test ===");
        // 테스트 중에 호출될 때 mock BoardService가 반환할 내용을 지정
        given(boardService.deleteBoard(JUNIT_TEST_BNO)).willReturn(JUNIT_TEST_BNO);
        // WHEN & THEN
        mockMvc.perform(post("/spring/board/delete/" + JUNIT_TEST_BNO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/spring/board/list"))
                .andExpect(flash().attributeExists("message"));
        log.info("=== End POST Delete Board Controller Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Controller: Board 컨트롤러 생성 테스트")
    public void postCreateBoardTest() throws Exception {
        log.info("=== Start POST Create Board Controller Test ===");
        // GIVEN
        BoardCreateDTO boardCreateDTO = BoardCreateDTO.builder()
                .title(JUNIT_TEST_TITLE)
                .content(JUNIT_TEST_CONTENT)
                .writer(JUNIT_TEST_WRITER)
                .fileName(Arrays.asList(uuid + "_" + JUNIT_TEST_FILE_NAME))
                .build();
        // 테스트 중에 호출될 때 mock BoardService가 반환할 내용을 지정
        given(boardService.createBoard(any(BoardCreateDTO.class))).willReturn(JUNIT_TEST_BNO);
        // WHEN & THEN
        mockMvc.perform(post("/spring/board/create")
                .param("title", JUNIT_TEST_TITLE)
                .param("content", JUNIT_TEST_CONTENT)
                .param("writer", JUNIT_TEST_WRITER)
                .param("fileNames", uuid + "_" + JUNIT_TEST_FILE_NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/spring/board/list"))
                .andExpect(flash().attributeExists("message"));
        log.info("=== End POST Create Board Controller Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Controller: Board 컨트롤러 업데이트 테스트")
    public void postUpdateBoardTest() throws Exception {
        log.info("=== Start POST Update Board Controller Test ===");
        // GIVEN
        BoardUpdateDTO boardUpdateDTO = BoardUpdateDTO.builder()
                .bno(JUNIT_TEST_BNO)
                .title(JUNIT_TEST_TITLE)
                .content(JUNIT_TEST_CONTENT)
                .writer(JUNIT_TEST_WRITER)
                .updateDate(JUNIT_TEST_NOW)
                .fileName(Arrays.asList(uuid + "_" + JUNIT_TEST_FILE_NAME))
                .build();
        // 테스트 중에 호출될 때 mock BoardService 반환할 내용을 지정
        given(boardService.updateBoard(any(BoardUpdateDTO.class))).willReturn(JUNIT_TEST_BNO);
        // WHEN & THEN
        mockMvc.perform(post("/spring/board/update")
                .param("bno", String.valueOf(JUNIT_TEST_BNO))
                .param("title", JUNIT_TEST_TITLE)
                .param("content", JUNIT_TEST_CONTENT)
                .param("writer", JUNIT_TEST_WRITER)
                .param("fileNames", uuid + "_" + JUNIT_TEST_FILE_NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/spring/board/read/" + JUNIT_TEST_BNO))
                .andExpect(flash().attributeExists("message"));
        log.info("=== End POST Update Board Controller Test ===");
    }
}
