package board.jpa.querydsl.entity;

import java.time.LocalDate;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_board")
public class BoardEntity {
    @Id
    @Comment("게시물 번호")
    @Column(name = "bno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @NotBlank
    @Size(max = 100)
    @Comment("게시물 제목")
    @Column(name = "title")
    private String title;

    @NotBlank
    @Size(max = 50)
    @Comment("게시물 작성자")
    @Column(name = "writer")
    private String writer;

    @NotBlank
    @Size(max = 1000)
    @Comment("게시물 내용")
    @Column(name = "content")
    private String content;

    @CreationTimestamp
    @Comment("게시물 생성 시간")
    @Column(name = "createDate")
    private LocalDate createDate;

    @UpdateTimestamp
    @Comment("게시물 수정 시간")
    @Column(name = "updateDate")
    private LocalDate updateDate;

    public static BoardEntity createBoard(String title, String writer, String content) {
        return BoardEntity.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .build();
    }

    public void updateBoard(String title, String writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.updateDate = LocalDate.now();
    }
}
