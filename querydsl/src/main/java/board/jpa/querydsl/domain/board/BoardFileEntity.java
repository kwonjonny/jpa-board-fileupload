package board.jpa.querydsl.domain.board;

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
@Table(name = "`tbl_board_images`")
public class BoardFileEntity {
    @Id
    @Column(name = "imageId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Comment("이미지 uuid")
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Comment("이미지 이름")
    @Column(name = "fileName", nullable = false)
    private String fileName;

    @Comment("이미지 순서")
    @Column(name = "ord", nullable = false)
    private Integer ord;

    @JoinColumn(name = "bno")
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardEntity boardEntity;

    public void setBoardEntity(BoardEntity boardEntity) {
        this.boardEntity = boardEntity;
    }

    public void changeOrd(Integer ord) {
        this.ord = ord;
    }
}
