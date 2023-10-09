package board.jpa.querydsl.domain.like;

import java.time.LocalDate;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`tbl_like`", uniqueConstraints = @UniqueConstraint(columnNames = { "bno", "email" }))
public class LikeEntity {
    @Id
    @Comment("라이크 번호")
    @Column(name = "`likeId`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @NotBlank
    @Comment("회원 이메일")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Comment("게시물 번호")
    @Column(name = "bno", nullable = false)
    private Long bno;

    @CreationTimestamp
    @Column(name = "createDate")
    private LocalDate createDate;

    public static LikeEntity createLike(String email, Long bno, LocalDate createDate) {
        return LikeEntity.builder()
        .email(email)
        .bno(bno)
        .createDate(createDate)
        .build();
    }
}
