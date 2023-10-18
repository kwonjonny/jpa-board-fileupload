package board.jpa.querydsl.repository.reply;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.domain.board.BoardEntity;
import board.jpa.querydsl.domain.reply.ReplyEntity;
import board.jpa.querydsl.dto.reply.ReplyCreateDTO;
import board.jpa.querydsl.dto.reply.ReplyUpdateDTO;
import board.jpa.querydsl.exception.BoardNumberNotFoundException;
import board.jpa.querydsl.exception.DataNotFoundException;
import board.jpa.querydsl.exception.ReplyNumberNotFoundException;
import board.jpa.querydsl.repository.BoardRepository;
import board.jpa.querydsl.repository.ReplyRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired(required = false)
    private ReplyRepository replyRepository;

    @Autowired(required = false)
    private BoardRepository boardRepository;

    private static final Long JUNIT_TEST_BOARD_NUMBER = 7L;
    private static final String JUNIT_TEST_REPLYER = "thistrik@naver.com";
    private static final String JUNIT_TEST_REPLY = "Junit_Test_Reply";

    private static final Long JUNIT_TEST_REPLY_GNO_NUMBER = 5L;
    private static final Long JUNIT_TEST_REPLY_RNO_NUMBER = 5L;

    private ReplyCreateDTO replyCreateDTO;
    private ReplyCreateDTO replyChildCreateDTO;

    private ReplyUpdateDTO replyUpdateDTO;

    @BeforeEach
    public void setUp() {
        replyCreateDTO = ReplyCreateDTO.builder()
                .bno(JUNIT_TEST_BOARD_NUMBER)
                .reply(JUNIT_TEST_REPLY)
                .replyer(JUNIT_TEST_REPLYER)
                .createDate(LocalDate.now())
                .updateDate(LocalDate.now())
                .isDeleted(0L)
                .gno(0L)
                .build();

        replyChildCreateDTO = ReplyCreateDTO.builder()
                .bno(JUNIT_TEST_BOARD_NUMBER)
                .reply(JUNIT_TEST_REPLY)
                .replyer(JUNIT_TEST_REPLYER)
                .createDate(LocalDate.now())
                .updateDate(LocalDate.now())
                .isDeleted(0L)
                .gno(JUNIT_TEST_REPLY_GNO_NUMBER)
                .build();

        replyUpdateDTO = ReplyUpdateDTO.builder()
                .bno(JUNIT_TEST_BOARD_NUMBER)
                .reply(JUNIT_TEST_REPLY)
                .replyer(JUNIT_TEST_REPLYER)
                .updateDate(LocalDate.now())
                .rno(JUNIT_TEST_REPLY_RNO_NUMBER)
                .gno(JUNIT_TEST_REPLY_GNO_NUMBER)
                .isDeleted(0L)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("Repository: 댓글 생성 테스트")
    public void createReplyTest() {
        // GIVEN
        log.info("=== Start Create Reply Repository Test ===");
        // WHEN
        if (replyCreateDTO.getBno() == null || replyCreateDTO.getReply() == null
                || replyCreateDTO.getReplyer() == null) {
            throw new DataNotFoundException("댓글작성자, 댓글내용, 게시물 번호는 필수 사항입니다.");
        }
        BoardEntity boardEntity = boardRepository.findByBno(JUNIT_TEST_BOARD_NUMBER)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물의 번호가 없습니다."));

        if (replyCreateDTO.getGno() == 0L || replyCreateDTO.getGno() == null) {
            ReplyEntity replyEntity = ReplyEntity.createReply(
                    replyCreateDTO.getBno(),
                    replyCreateDTO.getReplyer(),
                    replyCreateDTO.getReply(),
                    replyCreateDTO.getGno(),
                    replyCreateDTO.getCreateDate(),
                    replyCreateDTO.getUpdateDate(),
                    replyCreateDTO.getIsDeleted());
            boardEntity.incremnetReplyCount();
            boardRepository.save(boardEntity);
            replyRepository.save(replyEntity);
            replyRepository.updateGno(replyEntity.getRno());
            // THEN
            Assertions.assertNotNull(replyEntity);
            Assertions.assertNotNull(boardEntity);
        }
        log.info("=== End Create Reply Repository Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Repository: 대댓글 생성 테스트")
    public void createReplyChildTest() {
        // GIVEN
        log.info("=== Start Create Reply Child Repository Test ===");
        // WHEN
        if (replyChildCreateDTO.getBno() == null || replyChildCreateDTO.getReply() == null
                || replyChildCreateDTO.getReplyer() == null) {
            throw new DataNotFoundException("댓글작성자, 댓글내용, 게시물 번호는 필수 사항입니다.");
        }
        BoardEntity boardEntity = boardRepository.findByBno(JUNIT_TEST_BOARD_NUMBER)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물의 번호가 없습니다."));
        ReplyEntity replyEntity = ReplyEntity.createReply(
                replyChildCreateDTO.getBno(),
                replyChildCreateDTO.getReplyer(),
                replyChildCreateDTO.getReply(),
                replyChildCreateDTO.getGno(),
                replyChildCreateDTO.getCreateDate(),
                replyChildCreateDTO.getUpdateDate(),
                replyChildCreateDTO.getIsDeleted());
        boardEntity.incrementLikeCount();
        boardRepository.save(boardEntity);
        replyRepository.save(replyEntity);
        // THEN
        Assertions.assertNotNull(replyEntity);
        Assertions.assertNotNull(boardEntity);
    }

    @Test
    @Transactional
    @DisplayName("Repository: 댓글 조회 테스트")
    public void readReplyTest() {
        // GIVEN
        log.info("=== Start Read Reply Repository Test ===");
        // WHEN
        ReplyEntity replyEntity = replyRepository.findById(JUNIT_TEST_REPLY_RNO_NUMBER)
                .orElseThrow(() -> new ReplyNumberNotFoundException("해당하는 댓글 번호가 없습니다."));
        // THEN
        Assertions.assertAll(
                () -> assertNotNull(replyEntity.getGno()),
                () -> assertNotNull(replyEntity.getBno()),
                () -> assertNotNull(replyEntity.getIsDeleted()),
                () -> assertNotNull(replyEntity.getReply()),
                () -> assertNotNull(replyEntity.getReplyer()),
                () -> assertNotNull(replyEntity.getCreateDate()),
                () -> assertNotNull(replyEntity.getUpdateDate()));
        log.info("replyEntity: " + replyEntity);
        log.info("=== End Read Reply Repository Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Repository: 댓글 업데이트 테스트")
    public void updateReplyTest() {
        // GIVEN
        log.info("=== Start Update Reply Repository Test ===");
        // WHEN
        BoardEntity boardEntity = boardRepository.findByBno(JUNIT_TEST_BOARD_NUMBER)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물 번호가 없습니다."));
        ReplyEntity replyEntity = replyRepository.findById(JUNIT_TEST_REPLY_RNO_NUMBER)
                .orElseThrow(() -> new ReplyNumberNotFoundException("해당하는 댓글 번호가 없습니다."));
        replyEntity.updateReply(
                replyUpdateDTO.getBno(),
                replyUpdateDTO.getRno(),
                replyUpdateDTO.getReply(),
                replyUpdateDTO.getReplyer(),
                replyUpdateDTO.getGno(),
                replyUpdateDTO.getUpdateDate(),
                replyUpdateDTO.getIsDeleted());
        replyRepository.save(replyEntity);
        // THEN
        Assertions.assertAll(
                () -> assertNotNull(replyEntity.getGno()),
                () -> assertNotNull(replyEntity.getBno()),
                () -> assertNotNull(replyEntity.getIsDeleted()),
                () -> assertNotNull(replyEntity.getReply()),
                () -> assertNotNull(replyEntity.getReplyer()),
                () -> assertNotNull(replyEntity.getCreateDate()),
                () -> assertNotNull(replyEntity.getUpdateDate()));
        log.info("=== End Update Reply Repository Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Repository: 댓글 삭제 테스트")
    public void deleteReplyTest() {
        // GIVEN
        log.info("=== Start Delete Reply Repository Test ===");
        // WHEN
        ReplyEntity replyEntity = replyRepository.findById(JUNIT_TEST_REPLY_RNO_NUMBER)
                .orElseThrow(() -> new ReplyNumberNotFoundException("해당하는 게시물의 번호가 없습니다."));

        BoardEntity boardEntity = boardRepository.findById(replyEntity.getBno())
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물의 번호가 없습니다."));
        replyEntity.deleteReply();
        replyRepository.save(replyEntity);
        boardEntity.decremnetReplyCount();
        boardRepository.save(boardEntity);
        // THEN
        Optional<ReplyEntity> afterRead = replyRepository.findById(JUNIT_TEST_REPLY_RNO_NUMBER);
        Assertions.assertAll(
                () -> assertEquals(afterRead.get().getReply(), "삭제된 댓글입니다."),
                () -> assertEquals(afterRead.get().getReplyer(), "삭제된 게시자입니다."));
        log.info("=== End Delete Reply Repository Test ===");
    }

}
