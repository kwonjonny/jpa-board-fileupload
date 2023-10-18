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
public class ReplyUpdateDTO {
    private Long bno;
    private Long rno;
    private String reply;
    private String replyer;
    private Long isDeleted;
    private LocalDate updateDate;
    private Long gno;
}
