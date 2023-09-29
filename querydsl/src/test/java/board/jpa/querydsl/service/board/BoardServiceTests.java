package board.jpa.querydsl.service.board;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.domain.board.BoardEntity;
import board.jpa.querydsl.dto.board.BoardCreateDTO;
import board.jpa.querydsl.dto.board.BoardDTO;
import board.jpa.querydsl.dto.board.BoardListDTO;
import board.jpa.querydsl.dto.board.BoardUpdateDTO;
import board.jpa.querydsl.service.BoardService;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    private static final String JUNIT_TEST_TITLE = "Junit_Test_Title";
    private static final String JUNIT_TEST_CONTENT = "Junit_Test_Content";
    private static final String JUNIT_TEST_WRITER = "Junit_Test_Writer";
    private static final Long JUNIT_TEST_BNO = 2L;

    private BoardCreateDTO boardCreateDTO;
    private BoardUpdateDTO boardUpdateDTO;

    @BeforeEach
    public void setUp() {
        boardCreateDTO = BoardCreateDTO.builder()
                .title(JUNIT_TEST_TITLE)
                .content(JUNIT_TEST_CONTENT)
                .writer(JUNIT_TEST_WRITER)
                .build();

        boardUpdateDTO = BoardUpdateDTO.builder()
                .bno(JUNIT_TEST_BNO)
                .writer(JUNIT_TEST_WRITER)
                .title(JUNIT_TEST_TITLE)
                .content(JUNIT_TEST_CONTENT)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("Service: 게시물 생성 테스트")
    public void createBoardService() {
        // GIVEN
        log.info("=== Start Create Board Service Test ===");
        // WHEN
        boardService.createBoard(boardCreateDTO);
        // THEN
        Assertions.assertNotNull(boardCreateDTO.getContent());
        Assertions.assertNotNull(boardCreateDTO.getWriter());
        Assertions.assertNotNull(boardCreateDTO.getTitle());
        log.info("=== End Create Board Service Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 게시물 조회 테스트")
    public void readBoardService() {
        // GIVEN
        log.info("=== Start Read Board Service Test ===");
        // WHEN
        BoardDTO readBoard = boardService.readBoard(JUNIT_TEST_BNO);
        // THEN
        Assertions.assertNotNull(readBoard);
        log.info("=== End Read Board Service Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 게시물 업데이트 테스트")
    public void updateBoardService() {
        // GIVEN
        log.info("=== Start Update Board Service Test ===");
        // WHEN
        Long boardUpate = boardService.updateBoard(boardUpdateDTO);
        // THEN
        Assertions.assertEquals(boardUpate, boardUpdateDTO.getBno());
        Assertions.assertNotNull(boardUpdateDTO.getBno());
        Assertions.assertNotNull(boardUpdateDTO.getContent());
        Assertions.assertNotNull(boardUpdateDTO.getWriter());
        Assertions.assertNotNull(boardUpdateDTO.getTitle());
        log.info("=== End Update Board Service Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 게시물 삭제 테스트")
    public void deleteBoardService() {
        // GIVEN
        log.info("=== Start Delete Board Service Test ===");
        // WHEN
        Long deleteBoard = boardService.deleteBoard(JUNIT_TEST_BNO);
        // THEN
        log.info("=== End Delete Board Service Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 게시물 리스트 및 통합검색, 날짜검색 테스트")
    public void listBoardService() {
        // GIVEN
        log.info("=== Start List Board Service Test ===");
        String searchType = "tcw";
        String startDate = "2023-09-27";
        String endDate = "2023-09-27";
        // WHEN
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .keyword(JUNIT_TEST_CONTENT)
                .type(searchType)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .build();
        PageResponseDTO<BoardListDTO> list = boardService.listBoard(pageRequestDTO);
        // THEN
        log.info("리스트: " + list);
        Assertions.assertNotNull(list, "list Should Be Not Null");
        log.info("=== End List Board Service Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 게시물 조회수 테스트")
    public void incrementViewCountTest() {
        // GIVEN 
        log.info("=== Start Increment View Count Board Service Test ===");
        // WHEN 
        boardService.incrementViewCount(JUNIT_TEST_BNO);
        // THEN 
        log.info("=== End Increment View Count Board Service Test ===");
    }
}