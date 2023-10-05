package board.jpa.querydsl.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import board.jpa.querydsl.security.service.handler.CustomAccessDeniedHandler;
import board.jpa.querydsl.security.service.handler.CustomOAuthSuccessHandler;
import lombok.extern.log4j.Log4j2;

/**
 * {@code SecurityConfig}는 Spring Security 설정 클래스입니다.
 */
@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(final  DataSource dataSource) {
        log.info("Inject DataSource");
        this.dataSource = dataSource;
    }

    /**
     * TokenRepository에 토큰 값을 저장하는 메서드입니다.
     *
     * @return PersistentTokenRepository 인스턴스
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    /**
     * OAuth 로그인 성공 핸들러를 생성하는 메서드입니다.
     *
     * @return CustomOAuthSuccessHandler 인스턴스
     */
    @Bean
    public CustomOAuthSuccessHandler customOAuthSuccessHandler() {
        return new CustomOAuthSuccessHandler();
    }

    /**
     * 비밀번호 인코더를 생성하는 메서드입니다.
     *
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security Filter Chain을 생성하는 메서드입니다.
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 인스턴스
     * @throws Exception 예외
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        log.info("Spring Seucirty Filter Chain Is Running");

        // 커스텀 로그인 페이지 경로 지정
        http.formLogin(config -> {
            config.loginPage("/spring/index");
            config.successHandler(customOAuthSuccessHandler());
        });

        // 권한이 없는 페이지를 접속했을 시 처리
        http.exceptionHandling(config -> {
            config.accessDeniedHandler(new CustomAccessDeniedHandler());
        });

        http.rememberMe(config -> {
            // 발행한 토큰 값 repository에 저장
            config.tokenRepository(persistentTokenRepository());
            config.tokenValiditySeconds(60 * 60 * 24 * 7); // 7 Days
        });

        // form 안에있는 Hidden으로 포함된 csrf input tag를 없애겠다.
        http.csrf(config -> {
            config.disable();
        });

        // social 로그인 signin페이지에 설정 (카카오)
        http.oauth2Login(config -> {
            config.loginPage("/spring/index");
            config.successHandler(customOAuthSuccessHandler());

        });

        // logout
        http.logout(config -> {
            config.logoutUrl("/spring/member/logout")
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/spring/index");
        });
        return http.build();
    }
}