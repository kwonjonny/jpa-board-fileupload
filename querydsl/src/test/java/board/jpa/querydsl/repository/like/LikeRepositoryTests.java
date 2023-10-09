package board.jpa.querydsl.repository.like;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.domain.board.BoardEntity;
import board.jpa.querydsl.domain.like.LikeEntity;
import board.jpa.querydsl.dto.like.LikeToggleDTO;
import board.jpa.querydsl.exception.BoardNumberNotFoundException;
import board.jpa.querydsl.exception.DataNotFoundException;
import board.jpa.querydsl.exception.LikeToggleNotFoundException;
import board.jpa.querydsl.repository.BoardRepository;
import board.jpa.querydsl.repository.LikeRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class LikeRepositoryTests {

    @Autowired(required = false)
    private LikeRepository likeRepository;

    @Autowired(required = false)
    private BoardRepository boardRepository;

    private static final Long JUNIT_TEST_BNO = 7L;
    private static final String JUNIT_TEST_EMAIL = "thistrik@naver.com";

    private LikeToggleDTO likeToggleDTO;

    @BeforeEach
    public void setUp() {
        likeToggleDTO = LikeToggleDTO.builder()
                .bno(JUNIT_TEST_BNO)
                .email(JUNIT_TEST_EMAIL)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("Repository: 좋아요 생성 및 삭제 테스트")
    public void createLikeTest() {
        // GIVEN
        log.info("=== Start Create Like Repository Test ===");
        // WHEN
        if (likeToggleDTO.getBno() == null || likeToggleDTO.getEmail() == null) {
            throw new DataNotFoundException("게시물 번호, 이메일은 필수 사항입니다.");
        }
        BoardEntity boardEntity = boardRepository.findById(JUNIT_TEST_BNO)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물의 번호가 없습니다."));

        LikeEntity likeEntity = likeRepository.findByEmailAndBno(JUNIT_TEST_BNO, JUNIT_TEST_EMAIL)
                .orElse(null);

        if (likeEntity == null) {
            likeEntity = LikeEntity.createLike(JUNIT_TEST_EMAIL, JUNIT_TEST_BNO, LocalDate.now());
            likeRepository.save(likeEntity);
            boardEntity.incrementLikeCount();
        } else {
            likeRepository.deleteByEmailAndBno(JUNIT_TEST_BNO, JUNIT_TEST_EMAIL);
            boardEntity.decrementLikeCount();
        }
        boardRepository.save(boardEntity);
        // THEN
        Assertions.assertEquals(likeEntity.getBno(), JUNIT_TEST_BNO);
        Assertions.assertEquals(likeEntity.getEmail(), JUNIT_TEST_EMAIL);
        Assertions.assertNotNull(likeEntity.getEmail());
        Assertions.assertNotNull(likeEntity.getBno());
        Assertions.assertNotNull(likeEntity.getLikeId());
        Assertions.assertNotNull(likeEntity.getCreateDate());
        log.info("=== End Create Like Repository Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Repository: 좋아요 카운트 테스트")
    public void countLikeTest() {
        // GIVEN
        log.info("=== Start Count Like Repository Test ===");
        // WHEN
        LikeEntity likeEntity = likeRepository.findByLikeBno(JUNIT_TEST_BNO)
                .orElseThrow(() -> new LikeToggleNotFoundException("해당하는 게시물 번호가 없습니다."));
        Integer count = likeRepository.countByEmailAndBno(JUNIT_TEST_BNO);
        // THEN
        log.info("count 수 : " + count);
        Assertions.assertNotNull(likeEntity.getBno());
        Assertions.assertNotNull(count);
        log.info("=== End Count Like Repository Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Repository: 회원 좋아요 체크 테스트")
    public void checkToggleLikeMemberTest() {
        // GIVEN
        log.info("=== Start Check Toggle Like Member Repository Test ===");
        // WHEN
        LikeEntity likeEntity = likeRepository.checkToggleLikeMember(JUNIT_TEST_BNO, JUNIT_TEST_EMAIL)
                .orElse(null);
        // THEN
        log.info("회원 라이크 좋아요 체크: " + likeEntity);
        log.info("=== End Check Toggle Like Member Repository Test ===");
    }
}
