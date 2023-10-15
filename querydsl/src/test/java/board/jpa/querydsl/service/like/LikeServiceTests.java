package board.jpa.querydsl.service.like;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.dto.like.LikeToggleDTO;
import board.jpa.querydsl.service.LikeService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class LikeServiceTests {

    @Autowired
    private LikeService likeService;

    private static final String JUNIT_MEMBER_EMAIL = "thistrik@naver.com";
    private static final Long JUNIT_TEST_BNO = 7L;

    private LikeToggleDTO likeToggleDTO;

    @BeforeEach
    public void setUp() {
        likeToggleDTO = LikeToggleDTO
                .builder()
                .bno(JUNIT_TEST_BNO)
                .email(JUNIT_MEMBER_EMAIL)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("Service: 토글 라이크 테스트")
    public void testToggleLike() {
        // GIVEN
        log.info("=== Start Toggle Like Service Test ===");
        // WHEN
        final Long toggle = likeService.toggleLike(JUNIT_TEST_BNO, JUNIT_MEMBER_EMAIL);
        // THEN
        log.info("toggleNumber: " + toggle);
        Assertions.assertEquals(likeToggleDTO.getEmail(), JUNIT_MEMBER_EMAIL);
        Assertions.assertEquals(likeToggleDTO.getBno(), JUNIT_TEST_BNO);
        Assertions.assertNotNull(JUNIT_MEMBER_EMAIL, "Member Email Should Be Not Null");
        Assertions.assertNotNull(JUNIT_TEST_BNO, "Bno Shold Be Not Null");
        log.info("=== End Toggle Like Service Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 라이크 카운트 테스트")
    public void testCountLike() {
        // GIVEN
        log.info("=== Start Count Like Service Test ===");
        // WHEN
        final Integer count = likeService.countLike(JUNIT_TEST_BNO);
        // THEN
        log.info("라이크 카운트 개수 : " + count);
        Assertions.assertNotNull(count, "Count Should Be Not Null");
        Assertions.assertEquals(likeToggleDTO.getBno(), JUNIT_TEST_BNO);
        log.info("=== End Toggle Like Service Test ===");
    }

    @Test
    @Transactional
    @DisplayName("Service: 멤버의 라이크 여부 체크 테스트")
    public void checkMemberLikeToggle() {
        // GIVEN
        log.info("=== Start Check Like Member Toggle Service Test ===");
        // WHEN
        final LikeToggleDTO result = likeService.checkToggleMember(JUNIT_TEST_BNO, JUNIT_MEMBER_EMAIL);
        // THEN
        log.info("라이크 여부 체크 Boolean: " + result.isLiked());
        Assertions.assertEquals(result.getEmail(), JUNIT_MEMBER_EMAIL);
        Assertions.assertEquals(result.getBno(), JUNIT_TEST_BNO);
        Assertions.assertNotNull(JUNIT_TEST_BNO, "Bno Should Be Not Null");
        Assertions.assertNotNull(JUNIT_MEMBER_EMAIL, "Member Email Should Be Not Null");
        log.info("=== Start Check Like Member Toggle Service Test ===");
    }
}
