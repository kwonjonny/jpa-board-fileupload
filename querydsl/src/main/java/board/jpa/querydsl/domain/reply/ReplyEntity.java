package board.jpa.querydsl.domain.reply;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import board.jpa.querydsl.domain.board.BoardEntity;
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
import lombok.ToString;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`tbl_reply`")
@ToString(exclude = "boardEntity")
public class ReplyEntity {
    @Id
    @Column(name = "rno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @Column(name = "reply", nullable = false, length = 500)
    private String reply;

    @Column(name = "replyer", nullable = false, length = 100)
    private String replyer;

    @Builder.Default
    @Column(name = "gno", nullable = false, columnDefinition = "int default 0")
    private Long gno = 0L;

    @Column(name = "bno", nullable = false)
    private Long bno;

    @CreationTimestamp
    @Column(name = "createDate")
    private LocalDate createDate;

    @UpdateTimestamp
    @Column(name = "updateDate")
    private LocalDate updateDate;

    @Builder.Default
    @Column(name = "isDeleted", nullable = false, columnDefinition = "int default 0")
    private Long isDeleted = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno", nullable = false)
    private BoardEntity boardEntity;

    public static ReplyEntity createReply(Long bno, String reply, String replyer, Long gno, LocalDate createDate,
            LocalDate updateDate, Long isDeleted) {
        return ReplyEntity.builder()
                .bno(bno)
                .reply(reply)
                .replyer(replyer)
                .gno(gno)
                .createDate(createDate)
                .updateDate(updateDate)
                .isDeleted(isDeleted)
                .build();
    }

    public void updateReply(Long bno, Long rno, String reply, String replyer, Long gno, LocalDate updateDate, Long isDeleted) {
        this.bno = bno;
        this.rno = rno;
        this.reply = reply;
        this.replyer = replyer;
        this.isDeleted = isDeleted;
        this.updateDate = updateDate;
    }
}
