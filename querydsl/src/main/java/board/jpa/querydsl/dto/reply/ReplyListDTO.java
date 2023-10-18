package board.jpa.querydsl.dto.reply;

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
public class ReplyListDTO {

    private Long bno;
    private Long rno;
    private String replyer;
    private String reply;
    private Long gno;
    private LocalDate creatDate;
    private LocalDate updateDate;
    private Long isDeleted;
}
