package board.jpa.querydsl.util.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.domain.member.MemberEntity;
import board.jpa.querydsl.dto.member.MemberCreateDTO;
import board.jpa.querydsl.dto.member.MemberUpdateDTO;
import board.jpa.querydsl.exception.DuplicateEmailException;
import board.jpa.querydsl.exception.MemberNotFoundException;
import board.jpa.querydsl.exception.errorcode.MemberErrorMessage;
import board.jpa.querydsl.repository.MemberRepository;

@Component
public class MemberValidator {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberValidator(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public void findMemberEmail(final String email) {
        final MemberEntity memberEntity = memberRepository.findById(email)
                .orElseThrow(
                        () -> new MemberNotFoundException(
                                String.format(MemberErrorMessage.MEMBER_NOT_FOUND.getFormattedMessage(email))));
    }

    @Transactional(readOnly = true)
    public void duplicateMemberEmail(final String email) {
        boolean isDuplicate = memberRepository.findById(email)
                .isPresent();
        if (isDuplicate) {
            throw new DuplicateEmailException(
                    MemberErrorMessage.DUPLICATE_EMAIL.getMessage());
        }
    }

    public void validationMemberCreateData(final MemberCreateDTO memberCreateDTO) {
        ValidationUtil.validateNotEmnty(memberCreateDTO.getEmail(), "이메일은 필수 사항입니다.");
        ValidationUtil.validateNotEmnty(memberCreateDTO.getMemberName(), "이름은 필수 사항입니다.");
        ValidationUtil.validateNotEmnty(memberCreateDTO.getMemberPhone(), "전화번호는 필수 사항입니다.");
        ValidationUtil.validateNotEmnty(memberCreateDTO.getMemberPw(), "패스워드는 필수 사항입니다.");
    }

    public void validationMemberUpdateData(final MemberUpdateDTO memberUpdateDTO) {
        ValidationUtil.validateNotEmnty(memberUpdateDTO.getEmail(), "이메일은 필수 사항입니다.");
        ValidationUtil.validateNotEmnty(memberUpdateDTO.getMemberName(), "이름은 필수 사항입니다.");
        ValidationUtil.validateNotEmnty(memberUpdateDTO.getMemberPhone(), "전화번호는 필수 사항입니다.");
        ValidationUtil.validateNotEmnty(memberUpdateDTO.getMemberPw(), "패스워드는 필수 사항입니다.");
    }

}