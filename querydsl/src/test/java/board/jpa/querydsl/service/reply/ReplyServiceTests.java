package board.jpa.querydsl.service.reply;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.dto.reply.ReplyCreateDTO;
import board.jpa.querydsl.dto.reply.ReplyDTO;
import board.jpa.querydsl.dto.reply.ReplyListDTO;
import board.jpa.querydsl.dto.reply.ReplyUpdateDTO;
import board.jpa.querydsl.service.ReplyService;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

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
                .rno(JUNIT_TEST_REPLY_RNO_NUMBER)
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
    @DisplayName("Service: 댓글 리스트 테스트")
    public void listReplyTest() {
        // GIVEN
        log.info("=== Start List Reply Test ===");
        // WHEN
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        PageResponseDTO<ReplyListDTO> listReply = replyService.listReply(pageRequestDTO, JUNIT_TEST_BOARD_NUMBER);
        // THEN
        log.info("listReply: " + listReply.getList());
        Assertions.assertNotNull(listReply);
        log.info("=== End List Reply Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 댓글 생성 테스트")
    public void creatReplyTest() {
        // GIVEN
        log.info("=== Start Create Reply Test ===");
        // WHEN
        replyService.createReply(replyCreateDTO);
        // THEN
        Assertions.assertAll(
                () -> assertNotNull(replyCreateDTO.getReply()),
                () -> assertNotNull(replyCreateDTO.getReplyer()),
                () -> assertNotNull(replyCreateDTO.getBno()));
        log.info("=== End Create Reply Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 대댓글 생성 테스트")
    public void createReplyChildTest() {
        // GIVEN
        log.info("=== Start Create Reply Child Test ===");
        // WHEN
        replyService.createReply(replyChildCreateDTO);
        // THEN
        Assertions.assertAll(
                () -> assertNotNull(replyChildCreateDTO.getReply()),
                () -> assertNotNull(replyChildCreateDTO.getReplyer()),
                () -> assertNotNull(replyChildCreateDTO.getBno()));
        log.info("=== End Create Reply Child Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 댓글 조회 테스트")
    public void readReplyTest() {
        // GIVEN
        log.info("=== Start Read Reply Test ===");
        // WHEN
        ReplyDTO replyDTO = replyService.readReply(JUNIT_TEST_REPLY_RNO_NUMBER);
        // THEN
        log.info("replyDTO: " + replyDTO);
        Assertions.assertAll(
                () -> assertNotNull(replyDTO.getBno()),
                () -> assertNotNull(replyDTO.getCreateDate()),
                () -> assertNotNull(replyDTO.getUpdateDate()),
                () -> assertNotNull(replyDTO.getReply()),
                () -> assertNotNull(replyDTO.getReplyer()),
                () -> assertNotNull(replyDTO.getIsDeleted()),
                () -> assertNotNull(replyDTO.getBno()),

                () -> assertEquals(replyDTO.getIsDeleted(), 0));
        log.info("=== End Read Reply Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 댓글 수정 테스트")
    public void updateReplyTest() {
        // GIVEN
        log.info("=== Start Update Reply Test ===");
        // WHEN
        replyService.updateReply(replyUpdateDTO);
        // THEN
        Assertions.assertAll(
                () -> assertNotNull(replyUpdateDTO.getBno()),
                () -> assertNotNull(replyUpdateDTO.getReply()),
                () -> assertNotNull(replyUpdateDTO.getReplyer()),
                () -> assertNotNull(replyUpdateDTO.getUpdateDate()),
                () -> assertNotNull(replyUpdateDTO.getIsDeleted()));
        log.info("=== End Update Reply Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 댓글 삭제 테스트")
    public void deleteReplyTest() {
        // GIVEN
        log.info("=== Start Delete Reply Test ===");
        // WHEN
        replyService.deleteReply(JUNIT_TEST_REPLY_RNO_NUMBER);
        // THEN
        ReplyDTO replyDTO = replyService.readReply(JUNIT_TEST_REPLY_RNO_NUMBER);
        Assertions.assertAll(
                () -> assertEquals(replyDTO.getReply(), "삭제된 댓글입니다."),
                () -> assertEquals(replyDTO.getReplyer(), "삭제된 게시자입니다."));
    }
}
