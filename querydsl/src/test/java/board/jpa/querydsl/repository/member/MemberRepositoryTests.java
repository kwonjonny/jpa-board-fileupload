package board.jpa.querydsl.repository.member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.domain.member.MemberEntity;
import board.jpa.querydsl.domain.member.MemberRoleEntity;
import board.jpa.querydsl.dto.member.MemberConvertDTO;
import board.jpa.querydsl.dto.member.MemberCreateDTO;
import board.jpa.querydsl.dto.member.MemberListDTO;
import board.jpa.querydsl.dto.member.MemberUpdateDTO;
import board.jpa.querydsl.exception.DataNotFoundException;
import board.jpa.querydsl.exception.MemberNotFoundException;
import board.jpa.querydsl.repository.MemberRepository;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class MemberRepositoryTests {

        @Autowired(required = false)
        private MemberRepository memberRepository;

        @Autowired(required = false)
        private MemberRoleEntity memberRoleEntity;

        @Autowired
        private PasswordEncoder passwordEncoder;

        private static final String JUNIT_TEST_EMAIL = "thistrik@naver.com";
        private static final String JUNIT_TEST_MEMBERPW = "thistrik1!";
        private static final String JUNIT_TEST_MEMBER_PHONE = "010-3099-0648";
        private static final String JUNIT_TEST_MEMBER_NAME = "권성준";
        private static final String JUNIT_TEST_MEMBER_ROLE = "ROLE_USER";

        private MemberCreateDTO memberCreateDTO;
        private MemberUpdateDTO memberUpdateDTO;

        @BeforeEach
        public void setUp() {
                memberCreateDTO = MemberCreateDTO.builder()
                                .email(JUNIT_TEST_EMAIL)
                                .memberPhone(JUNIT_TEST_MEMBER_PHONE)
                                .memberPw(JUNIT_TEST_MEMBERPW)
                                .memberName(JUNIT_TEST_MEMBER_NAME)
                                .build();

                memberUpdateDTO = MemberUpdateDTO.builder()
                                .email(JUNIT_TEST_EMAIL)
                                .memberPhone(JUNIT_TEST_MEMBER_PHONE)
                                .memberPw(JUNIT_TEST_MEMBERPW)
                                .memberName(JUNIT_TEST_MEMBER_NAME)
                                .build();
        }

        @Test
        @Transactional
        @DisplayName("Repository: 회원 가입 테스트")
        public void createMemberTest() {
                // GIVEN
                log.info("=== Start Crate Member Repository Test ===");
                // WHEN
                if (memberCreateDTO.getEmail() == null || memberCreateDTO.getMemberName() == null
                                || memberCreateDTO.getMemberPhone() == null || memberCreateDTO.getMemberPw() == null) {
                        throw new DataNotFoundException("이메일 이름 비밀번호 전화번호는 필수입니다.");
                }
                final MemberEntity memberEntity = MemberEntity.createMember(memberCreateDTO.getEmail(),
                                passwordEncoder.encode(memberCreateDTO.getMemberPw()),
                                memberCreateDTO.getMemberName(), memberCreateDTO.getMemberPhone(), LocalDate.now(),
                                LocalDate.now(),
                                'N');
                final MemberRoleEntity roleEntity = MemberRoleEntity.builder()
                                .roleName(JUNIT_TEST_MEMBER_ROLE)
                                .build();

                roleEntity.setMemberEntity(memberEntity);
                memberEntity.addMemberRole(roleEntity);
                memberRepository.save(memberEntity);
                // THEN
                Assertions.assertNotNull(roleEntity);
                Assertions.assertNotNull(memberEntity);
                Assertions.assertEquals(memberEntity.getEmail(), memberCreateDTO.getEmail());
                Assertions.assertNotNull(memberEntity.getMemberPw(), memberCreateDTO.getMemberPw());
                log.info("=== End Create Member Repository Test ===");
        }

        @Test
        @Transactional
        @DisplayName("Repository: 회원 탈퇴 테스트")
        public void deleteMemberTest() {
                // GVIEN
                log.info("=== Start Delete Member Repository Test ===");
                // WHEN
                final MemberEntity memberEntity = memberRepository.findById(JUNIT_TEST_EMAIL)
                                .orElseThrow(() -> new MemberNotFoundException(
                                                "해당하는 이메일의 회원이 없습니다. " + JUNIT_TEST_EMAIL));
                memberEntity.deleteMemberRole();
                memberRepository.deleteById(JUNIT_TEST_EMAIL);
                // THEN
                final Optional<MemberEntity> deleteMember = memberRepository.findById(JUNIT_TEST_EMAIL);
                Assertions.assertTrue(deleteMember.isEmpty());
                log.info("=== End Delete Member Repository Test ===");
        }

        @Test
        @Transactional
        @DisplayName("Repository: 회원 업데이트 테스트")
        public void updateMemberTest() {
                // GIVEN
                log.info("=== Start Update Member Repository Test ===");
                // WHEN
                if (memberUpdateDTO.getEmail() == null || memberUpdateDTO.getMemberPhone() == null
                                || memberUpdateDTO.getMemberName() == null || memberUpdateDTO.getMemberPw() == null) {
                        throw new DataNotFoundException("이메일, 이름, 패스워드, 전화번호 는 필수입니다.");
                }
                final MemberEntity memberEntity = memberRepository.findById(JUNIT_TEST_EMAIL)
                                .orElseThrow(() -> new MemberNotFoundException(
                                                "해당하는 이메일의 회원이 없습니다. " + JUNIT_TEST_EMAIL));

                memberEntity.updateMember(memberUpdateDTO.getEmail(), memberUpdateDTO.getMemberPhone(),
                                memberUpdateDTO.getMemberName(), passwordEncoder.encode(memberUpdateDTO.getMemberPw()));

                // THEN
                final Optional<MemberEntity> updateMember = memberRepository.findById(JUNIT_TEST_EMAIL);
                Assertions.assertNotNull(updateMember);
                log.info("=== End Update Member Repository Test ===");
        }

        @Test
        @Transactional
        @DisplayName("Repository: 회원 조회 테스트")
        public void readMemberTest() {
                // GIVEN
                log.info("=== Start Read Member Repository Test ===");
                // WHEN
                final MemberEntity memberEntity = memberRepository.findById(JUNIT_TEST_EMAIL)
                                .orElseThrow(() -> new MemberNotFoundException(
                                                "해당하는 이메일의 회원이 없습니다. " + JUNIT_TEST_EMAIL));

                final List<String> roleNames = memberEntity.getMemberRoleEntities().stream()
                                .map(MemberRoleEntity::getRoleName).collect(Collectors.toList());

                final MemberConvertDTO memberConvertDTO = MemberConvertDTO.builder()
                                .email(memberEntity.getEmail())
                                .memberName(memberEntity.getMemberName())
                                .memberPw(memberEntity.getMemberPw())
                                .memberPhone(memberEntity.getMemberPhone())
                                .isVerified(String.valueOf(memberEntity.getIsVerified()))
                                .createDate(memberEntity.getCreateDate())
                                .updateDate(memberEntity.getUpdateDate())
                                .rolenames(roleNames)
                                .build();
                // THEN
                log.info("회원정보: " + memberConvertDTO);
                Assertions.assertNotNull(memberConvertDTO);
                log.info("=== End Read Member Repository Test ===");
        }

        @Test
        @Transactional
        @DisplayName("Repository: 회원 리스트 테스트")
        public void listMemberTest() {
                log.info("=== Start Member List Repository Test ===");
                // GIVEN
                String searchType = "epn";
                String keyword = "권성준";
                String startDate = "2023-09-27";
                String endDate = "2023-10-05";
                // WHEN
                PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                                .type(searchType)
                                .keyword(keyword)
                                .startDate(LocalDate.parse(startDate))
                                .endDate(LocalDate.parse(endDate))
                                .build();
                PageResponseDTO<MemberListDTO> listMember = memberRepository.listMember(pageRequestDTO);
                log.info("리스트: " + listMember.getList());
                log.info(listMember);
                Assertions.assertNotNull(listMember, "listMember Should Be Not Null");
                log.info("=== End Member List Repository Test ===");
        }
}