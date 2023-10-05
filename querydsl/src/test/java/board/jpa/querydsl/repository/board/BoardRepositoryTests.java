package board.jpa.querydsl.repository.board;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

import board.jpa.querydsl.domain.board.BoardEntity;
import board.jpa.querydsl.domain.board.BoardFileEntity;
import board.jpa.querydsl.dto.board.BoardCreateDTO;
import board.jpa.querydsl.dto.board.BoardUpdateDTO;
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
    private static final Long JUNIT_TEST_BNO = 7L;
    private static final String JUNIT_TEST_FILE_NAME = "Junit_Test_File_Name.jpg";

    private BoardCreateDTO boardCreateDTO;
    private BoardUpdateDTO boardUpdateDTO;
    private String uuid;

    @BeforeEach
    public void setUp() {
        uuid = UUID.randomUUID().toString();

        boardCreateDTO = BoardCreateDTO.builder()
                .title(JUNIT_TEST_TITLE)
                .content(JUNIT_TEST_CONTENT)
                .writer(JUNIT_TEST_WRITER)
                .fileName(Arrays.asList(uuid + "_" + JUNIT_TEST_FILE_NAME))
                .build();

        boardUpdateDTO = BoardUpdateDTO.builder()
                .bno(JUNIT_TEST_BNO)
                .writer(JUNIT_TEST_WRITER)
                .title(JUNIT_TEST_TITLE)
                .content(JUNIT_TEST_CONTENT)
                .fileName(Arrays.asList(uuid + "_" + JUNIT_TEST_FILE_NAME))
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
                || boardCreateDTO.getContent() == null) {
            throw new DataNotFoundException("게시물 작성자, 내용, 제목은 필수사항입니다.");
        }
        BoardEntity boardEntity = BoardEntity.createBoard(boardCreateDTO.getTitle(), boardCreateDTO.getWriter(),
                boardCreateDTO.getContent());
        BoardEntity saveBoard = boardRepository.save(boardEntity);

        List<String> fileNames = boardCreateDTO.getFileName();
        AtomicInteger index = new AtomicInteger(0);
        if (!boardCreateDTO.getFileName().isEmpty() && boardCreateDTO.getFileName() != null) {
            List<BoardFileEntity> list = fileNames.stream().map(str -> {
                Long bno = saveBoard.getBno();
                String uuid = str.substring(0, 36);
                String fileName = str.substring(37);
                BoardFileEntity fileEntity = BoardFileEntity.builder()
                        .uuid(uuid)
                        .fileName(fileName)
                        .build();
                fileEntity.setBoardEntity(saveBoard);
                Assertions.assertNotNull(fileEntity);
                return fileEntity;
            }).collect(Collectors.toList());
            list.forEach(boardEntity::addImage);
            boardRepository.save(saveBoard);
        }
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
        if (boardUpdateDTO.getBno() == null || boardUpdateDTO.getWriter() == null 
                || boardUpdateDTO.getTitle() == null || boardUpdateDTO.getContent() == null) {
            throw new DataNotFoundException("게시물 번호, 작성자, 내용, 제목은 필수 사항입니다.");
        }
        
        BoardEntity findBoard = boardRepository.findById(JUNIT_TEST_BNO)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물이 없습니다. " + JUNIT_TEST_BNO));
        findBoard.updateBoard(boardUpdateDTO.getTitle(), boardUpdateDTO.getWriter(), boardUpdateDTO.getContent());
    
        List<String> fileNames = boardCreateDTO.getFileName();
        if (!fileNames.isEmpty()) {
            findBoard.clearImage();
            boardRepository.save(findBoard);
            fileNames.forEach(str -> {
                String uuid = str.substring(0, 36);
                String fileName = str.substring(37);
                BoardFileEntity fileEntity = BoardFileEntity.builder()
                        .uuid(uuid)
                        .fileName(fileName)
                        .build();
                fileEntity.setBoardEntity(findBoard);
                findBoard.addImage(fileEntity);
            });
            boardRepository.save(findBoard);
        }
        
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

        List<BoardFileEntity> boardFileEntities = findBoard.getFileNames();
        log.info("이미지: " + boardFileEntities);
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
        findBoard.clearImage();
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

    @Test
    @Transactional
    @DisplayName("Repository: 게시물 조회수 업데이트 테스트")
    public void incrementViewCountRepositoryTest() {
        // GIVEN
        log.info("=== Start Board View Count Repository Test ====");
        // WHEN
        BoardEntity boardEntity = boardRepository.findById(JUNIT_TEST_BNO)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물 번호가 없습니다." + JUNIT_TEST_BNO));
        boardRepository.incrementViewCount(JUNIT_TEST_BNO);
        // THEN
        BoardEntity findBoardViewCount = boardRepository.findById(JUNIT_TEST_BNO)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물 번호가 없습니다." + JUNIT_TEST_BNO));
        log.info("조회수: " + findBoardViewCount.getViewCount());
        log.info("=== End Board View Count Repository Test ===");
    }
}
