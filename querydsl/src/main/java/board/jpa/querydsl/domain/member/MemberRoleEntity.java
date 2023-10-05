package board.jpa.querydsl.domain.member;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "`tbl_member_role`")
public class MemberRoleEntity {

    @Id
    @Comment("회원 Role ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", insertable = false, updatable = false)
    private MemberEntity memberEntity;

    @Comment("회원 이메일")
    @Column(name = "email")
    private String email;

    @Comment("회원 권한")
    @Column(name = "roleName")
    private String roleName;

    public void setMemberEntity(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
        this.email = memberEntity.getEmail();
    }
}
