package board.jpa.querydsl.security.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import board.jpa.querydsl.domain.member.MemberEntity;
import board.jpa.querydsl.domain.member.MemberRoleEntity;
import board.jpa.querydsl.dto.member.MemberConvertDTO;
import board.jpa.querydsl.dto.member.MemberCreateDTO;
import board.jpa.querydsl.dto.member.MemberDTO;
import board.jpa.querydsl.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * {@code CustomOAuth2UserService}는 카카오 로그인 사용자 정보를 처리하는 서비스 클래스입니다.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * OAuth2User 정보를 로드하여 처리합니다.
     *
     * @param userRequest OAuth2UserRequest 객체
     * @return OAuth2User 객체
     * @throws OAuth2AuthenticationException OAuth2 인증 예외
     */
    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("Is Running Load By User Request");
        log.info(userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;

        switch (clientName) {
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }
        log.info("Email: " + email);

        // DB에 해당 사용자가있으면
        MemberEntity memberEntity = memberRepository.selectOne(email);
        if (memberEntity == null) {
            // 사용자 정보와 권한을 설정합니다.
            MemberEntity newMember = MemberEntity.createMember(
                    email,
                    passwordEncoder.encode("1111"), // 비밀번호를 암호화합니다.
                    "카카오 사용자",
                    "010-1111-1111",
                    LocalDate.now(),
                    LocalDate.now(),
                    'N' // 이메일 인증이 필요하다면 'N'으로 설정합니다.
            );

            // 사용자 권한을 설정합니다.
            MemberRoleEntity memberRoleEntity = MemberRoleEntity.builder()
                    .roleName("ROLE_USER") // 롤 이름 설정
                    .memberEntity(newMember) // 사용자 정보 설정
                    .build();
            newMember.addMemberRole(memberRoleEntity); // 사용자 정보에 권한을 추가합니다.

            // DB에 사용자 정보를 저장합니다.
            memberRepository.save(newMember);

            // Spring Security에 사용자 정보를 반환합니다.
            MemberDTO memberDTO = new MemberDTO(
                    email,
                    "", // OAuth 로그인이므로 비밀번호는 빈 문자열로 설정합니다.
                    "카카오 사용자",
                    List.of("ROLE_USER"));
            return memberDTO;

        } else {
            // DB에 사용자 정보가 이미 있으면 해당 정보를 반환합니다.
            MemberDTO memberDTO = new MemberDTO(
                    memberEntity.getEmail(),
                    memberEntity.getMemberPw(),
                    memberEntity.getMemberName(),
                    memberEntity.getMemberRoleEntities()
                            .stream()
                            .map(role -> role.getRoleName())
                            .collect(Collectors.toList()));
            return memberDTO;
        }

    }

    private String getKakaoEmail(final Map<String, Object> paramMap) {
        Object value = paramMap.get("kakao_account");
        log.info("value: " + value);
        LinkedHashMap accountMap = (LinkedHashMap) value;
        String email = (String) accountMap.get("email");
        log.info("email: " + email);
        return email;
    }
}