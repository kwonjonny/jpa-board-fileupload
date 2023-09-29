package board.jpa.querydsl.util.cookie;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 조회수를 관리하기 위한 쿠키 유틸리티 클래스입니다.
 */
@Component
public class ManagementCookie {

    private static final String COOKIE_NAME = "read_board_";

    /**
     * 쿠키 생성 및 조회수 증가 여부를 결정합니다.
     *
     * @param request  HTTP 요청 객체.
     * @param response HTTP 응답 객체.
     * @param bno      게시글 번호.
     * @return 쿠키가 생성되어 조회수가 증가할 경우 true를 반환하고, 이미 존재하는 쿠키인 경우 false를 반환합니다.
     */
    public boolean createCookie(final HttpServletRequest request, final HttpServletResponse response, final Long bno) {
        String cookieName = COOKIE_NAME + bno;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    // 이미 쿠키가 존재하므로 조회수를 증가시키지 않습니다
                    return false;
                }
            }
        }
        /**
         * 쿠키 생성
         * 24 시간 동안 쿠키 유지 후
         * 쿠키가 없을 시 조회수 증가 하도록 true 반환
         */
        Cookie cookie = new Cookie(cookieName, "true");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
        return true;
    }
}