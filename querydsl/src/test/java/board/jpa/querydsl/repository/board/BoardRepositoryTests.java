package board.jpa.querydsl.repository.board;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import board.jpa.querydsl.dto.BoardCreateDTO;
import board.jpa.querydsl.dto.BoardUpdateDTO;
import board.jpa.querydsl.entity.BoardEntity;
import board.jpa.querydsl.exception.BoardNumberNotFoundException;
import board.jpa.querydsl.exception.DataNotFoundException;
import board.jpa.querydsl.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class BoardRepositoryTests {

    @Autowired(required = false)
    private BoardRepository boardRepository;

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
    @DisplayName("Repository: 게시물 생성 테스트")
    public void createBoardRepositoryTest() {
        // GIVEN
        log.info("=== Start Create Board Repsotiroy Test ===");
        // WHEN
        if (boardCreateDTO.getTitle() == null || boardCreateDTO.getWriter() == null
                || boardCreateDTO.getTitle() == null) {
            throw new DataNotFoundException("게시물 작성자, 내용, 제목은 필수사항입니다.");
        }
        BoardEntity boardEntity = BoardEntity.createBoard(boardCreateDTO.getTitle(), boardCreateDTO.getWriter(),
                boardCreateDTO.getContent());
        BoardEntity saveBoard = boardRepository.save(boardEntity);
        // THEN
        Assertions.assertNotNull(saveBoard);
        Assertions.assertEquals(saveBoard.getContent(), boardCreateDTO.getContent());
        Assertions.assertEquals(saveBoard.getWriter(), boardCreateDTO.getWriter());
        Assertions.assertEquals(saveBoard.getTitle(), boardCreateDTO.getTitle());
        log.info("=== End Create Board Repository Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Repository: 게시물 업데이트 테스트")
    public void updateBoardRepositoryTest() {
        // GIVEN
        log.info("=== Start Update Board Repository Test ===");
        // WHEN
        if (boardUpdateDTO.getBno() == null || boardUpdateDTO.getWriter() == null || boardUpdateDTO.getTitle() == null
                || boardUpdateDTO.getContent() == null) {
            throw new DataNotFoundException("게시물 번호, 작성자, 내용, 제목은 필수 사항입니다.");
        }
        BoardEntity findBoard = boardRepository.findById(JUNIT_TEST_BNO)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물이 없습니다. " + JUNIT_TEST_BNO));
        findBoard.updateBoard(boardUpdateDTO.getTitle(), boardUpdateDTO.getWriter(), boardUpdateDTO.getContent());
        // THEN
        Assertions.assertNotNull(findBoard);
        Assertions.assertEquals(findBoard.getBno(), boardUpdateDTO.getBno());
        Assertions.assertEquals(findBoard.getWriter(), boardUpdateDTO.getWriter());
        Assertions.assertEquals(findBoard.getTitle(), boardUpdateDTO.getTitle());
        log.info("=== End Update Board Repository Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Repository: 게시물 조회 테스트")
    public void readBoardRepositoryTest() {
        // GIVEN
        log.info("=== Start Read Board Repository Test ===");
        // WHEN
        BoardEntity findBoard = boardRepository.findById(JUNIT_TEST_BNO)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물이 없습니다. " + JUNIT_TEST_BNO));
        // THEN
        log.info("게시물 내용: " + findBoard);
        Assertions.assertEquals(findBoard.getBno(), JUNIT_TEST_BNO);
        Assertions.assertNotNull(findBoard, "findBoard Should Be Not Null");
        log.info("=== End Read Board Repository Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Repository: 게시물 삭제 테스트")
    public void deleteBoardRepositoryTest() {
        // GVIEN
        log.info("=== Start Delete Board Repository Test ===");
        // WHEN
        BoardEntity findBoard = boardRepository.findById(JUNIT_TEST_BNO)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물이 없습니다. " + JUNIT_TEST_BNO));
        boardRepository.deleteById(JUNIT_TEST_BNO);
        // THEN
        Optional<BoardEntity> deletedBoard = boardRepository.findById(JUNIT_TEST_BNO);
        Assertions.assertTrue(deletedBoard.isEmpty());
    }

    @Test
    @Transactional
    @DisplayName("Repository: 게시물 리스트 테스트")
    public void listBoardRepsitoryTest() {
        // GIVEN
        log.info("=== Start List Board Repository Test ===");
        // WHEN
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<BoardEntity> page = boardRepository.findAll(pageable);
        List<BoardEntity> list = page.getContent();
        // THEN
        log.info("리스트: " + list);
        Assertions.assertNotNull(list, "list Should Be Not Null");
        log.info("=== End List Board Repository Test ===");
    }
}
