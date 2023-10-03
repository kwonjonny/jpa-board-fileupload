package board.jpa.querydsl.dto.board;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardListDTO {
    private Long bno;
    private String writer;
    private String title;
    private String content;
    private LocalDate createDate;
    private LocalDate updateDate;
    private Long viewCount;
    private String fileName;
}
