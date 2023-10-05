package board.jpa.querydsl.security.service.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

/**
 * {@code CustomAccessDeniedHandler}는 Spring Security의 접근 거부 핸들러 클래스입니다.
 */
@Log4j2
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 접근이 거부될 때 실행되는 메서드입니다.
     *
     * @param request               HttpServletRequest 객체
     * @param response              HttpServletResponse 객체
     * @param accessDeniedException 발생한 접근 거부 예외 객체
     * @throws IOException      입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("Is Running AccessDenied Handler");
        log.info(accessDeniedException);
    }
}
