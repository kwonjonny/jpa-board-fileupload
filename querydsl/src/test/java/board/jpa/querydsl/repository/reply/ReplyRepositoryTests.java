package board.jpa.querydsl.repository.reply;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.dto.reply.ReplyCreateDTO;
import board.jpa.querydsl.dto.reply.ReplyUpdateDTO;
import board.jpa.querydsl.repository.ReplyRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired(required = false)
    private ReplyRepository replyRepository;

    private static final Long JUNIT_TEST_BOARD_NUMBER = 7L;
    private static final String JUNIT_TEST_REPLYER = "thistrik@naver.com";
    private static final String JUNIT_TEST_REPLY = "Junit_Test_Reply";

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
                .build();

        replyUpdateDTO = ReplyUpdateDTO.builder()
        .bno(JUNIT_TEST_BOARD_NUMBER)
        .build();
    }

    @Test
    @Transactional
    @DisplayName("Repository: 댓글 생성 테스트")
    public void createReplyTest() {
        
    }
}
