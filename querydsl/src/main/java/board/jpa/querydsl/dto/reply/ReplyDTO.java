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
public class ReplyDTO {
    private Long bno;
    private Long rno;
    private String reply;
    private String replyer;

    private Long gno;

    private LocalDate createDate;
    private LocalDate updateDate;

    private Long isDeleted;
}
