package board.jpa.querydsl.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import board.jpa.querydsl.domain.member.MemberEntity;
import board.jpa.querydsl.dto.member.MemberConvertDTO;
import board.jpa.querydsl.dto.member.MemberDTO;
import board.jpa.querydsl.exception.VerifyEmailException;
import board.jpa.querydsl.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * {@code CustomUserDetailsService}는 사용자의 인증 정보를 처리하는 서비스 클래스입니다.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 사용자명을 기반으로 UserDetails 정보를 로드하여 반환합니다.
     *
     * @param username 사용자명
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 사용자명을 찾을 수 없을 때 발생하는 예외
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.info("Is Running LoadUserByUserName");
        log.info(username);

        MemberEntity memberEntity = memberRepository.selectOne(username);
        log.info("memberEntity", memberEntity);
        log.info(memberEntity);

        if ("N".equals(String.valueOf(memberEntity.getIsVerified()))) {
            throw new VerifyEmailException("이메일 인증을 완료해주세요.");
        }
        List<String> rolenames = memberEntity.getMemberRoleEntities().stream()
                .map(memberRoleEntity -> memberRoleEntity.getRoleName())
                .collect(Collectors.toList());

        MemberDTO memberDTO = new MemberDTO(username,
                memberEntity.getMemberPw(),
                memberEntity.getMemberName(),
                rolenames);
        return memberDTO;
    }
}