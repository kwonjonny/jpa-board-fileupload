package board.jpa.querydsl.domain.member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`tbl_member`")
public class MemberEntity {
    @Id
    @Comment("회원 이메일")
    @Column(name = "email")
    private String email;

    @Comment("회원 비밀번호")
    @Column(name = "memberPw", nullable = false)
    private String memberPw;

    @Comment("회원 이름")
    @Column(name = "memberName", nullable = false)
    private String memberName;

    @Comment("회원 전화번호")
    @Column(name = "memberPhone", nullable = false)
    private String memberPhone;

    @Comment("회원 가입 날짜")
    @CreationTimestamp
    @Column(name = "createDate", nullable = false)
    private LocalDate createDate;

    @Comment("회원 수정 날짜")
    @UpdateTimestamp
    @Column(name = "updateDate", nullable = false)
    private LocalDate updateDate;

    @Comment("회원 인증 여부")
    @Column(name = "isVerified", nullable = false)
    private char isVerified;

    @Builder.Default
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MemberRoleEntity> memberRoleEntities = new ArrayList<>();

    public static MemberEntity createMember(String email, String memberPw, String memberName, String memberPhone,
            LocalDate createDate, LocalDate updateDate, char isVerified) {
        return MemberEntity.builder()
                .email(email)
                .memberPw(memberPw)
                .memberName(memberName)
                .memberPhone(memberPhone)
                .isVerified(isVerified)
                .createDate(createDate)
                .updateDate(updateDate)
                .build();
    }

    public void updateMember(String email, String memberPw, String memberName, String memberPhone) {
        this.email = email;
        this.memberPw = memberPw;
        this.memberPhone = memberPhone;
        this.updateDate = LocalDate.now();
    }

    public void addMemberRole(MemberRoleEntity memberRoleEntity) {
        memberRoleEntities.add(memberRoleEntity);
    }

    public void deleteMemberRole() {
        memberRoleEntities.clear();
    }
}
