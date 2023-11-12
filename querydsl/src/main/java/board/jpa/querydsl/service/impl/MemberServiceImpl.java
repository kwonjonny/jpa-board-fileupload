package board.jpa.querydsl.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.domain.member.MemberEntity;
import board.jpa.querydsl.domain.member.MemberRole;
import board.jpa.querydsl.domain.member.MemberRoleEntity;
import board.jpa.querydsl.dto.member.MemberConvertDTO;
import board.jpa.querydsl.dto.member.MemberCreateDTO;
import board.jpa.querydsl.dto.member.MemberListDTO;
import board.jpa.querydsl.dto.member.MemberUpdateDTO;
import board.jpa.querydsl.repository.MemberRepository;
import board.jpa.querydsl.service.MemberService;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;
import board.jpa.querydsl.util.validator.MemberValidator;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final MemberValidator memberValidator;

    @Autowired
    public MemberServiceImpl(final MemberRepository memberRepository, final PasswordEncoder passwordEncoder, final MemberValidator memberValidator) {
        log.info("Inject MemberRepository");
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberValidator = memberValidator;
    }

    @Override
    @Transactional
    public void createMember(final MemberCreateDTO memberCreateDTO) {
        log.info("Is Running Create Member ServiceImpl");
        memberValidator.validationMemberCreateData(memberCreateDTO);
        memberValidator.duplicateMemberEmail(memberCreateDTO.getEmail());

        final MemberEntity memberEntity = MemberEntity.createMember(
                memberCreateDTO.getEmail(),
                memberCreateDTO.getMemberPhone(),
                passwordEncoder.encode(memberCreateDTO.getMemberPw()),
                memberCreateDTO.getMemberName(),
                LocalDate.now(),
                LocalDate.now(),
                'N');
        final MemberRoleEntity memberRoleEntity = MemberRoleEntity
                .builder()
                .roleName(String.valueOf(MemberRole.ROLE_USER))
                .build();
        memberRoleEntity.setMemberEntity(memberEntity);
        memberEntity.addMemberRole(memberRoleEntity);
        memberRepository.save(memberEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberConvertDTO readMember(final String email) {
        log.info("Is Running Read Member ServiceImpl");
        memberValidator.findMemberEmail(email);

        final MemberEntity memberEntity = memberRepository.findById(email)
                .orElse(null);
        final List<String> roleNames = memberEntity.getMemberRoleEntities()
                .stream()
                .map(MemberRoleEntity::getRoleName)
                .collect(Collectors.toList());
        final MemberConvertDTO memberConvertDTO = MemberConvertDTO
                .builder()
                .email(memberEntity.getEmail())
                .memberPhone(memberEntity.getMemberPhone())
                .memberName(memberEntity.getMemberName())
                .isVerified(String.valueOf(memberEntity.getIsVerified()))
                .memberPw(memberEntity.getMemberPw())
                .createDate(memberEntity.getCreateDate())
                .updateDate(memberEntity.getUpdateDate())
                .rolenames(roleNames)
                .build();
        return memberConvertDTO;
    }

    @Override
    @Transactional
    public void updateMember(final MemberUpdateDTO memberUpdateDTO) {
        log.info("IS Running Update Member ServiceImpl");
        memberValidator.validationMemberUpdateData(memberUpdateDTO);
        memberValidator.findMemberEmail(memberUpdateDTO.getEmail());

        final MemberEntity memberEntity = memberRepository.findById(memberUpdateDTO.getEmail())
                .orElse(null);
        memberEntity.updateMember(
                memberUpdateDTO.getEmail(),
                passwordEncoder.encode(memberUpdateDTO.getMemberPw()),
                memberUpdateDTO.getMemberName(),
                memberUpdateDTO.getMemberPhone());
        memberRepository.save(memberEntity);
    }

    @Override
    @Transactional
    public void deleteMember(final String email) {
        log.info("Is Running Delete Member ServiceImpl");
        memberValidator.findMemberEmail(email);

        final MemberEntity memberEntity = memberRepository.findById(email)
                .orElse(null);
        memberEntity.deleteMemberRole();
        memberRepository.deleteById(email);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<MemberListDTO> listMember(final PageRequestDTO pageRequestDTO) {
        log.info("Is Running List Member ServiceImpl");
        return memberRepository.listMember(pageRequestDTO);
    }
}
