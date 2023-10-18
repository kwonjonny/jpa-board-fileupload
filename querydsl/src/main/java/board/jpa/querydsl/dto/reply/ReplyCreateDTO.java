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
public class ReplyCreateDTO {
    private Long bno;
    private Long rno;
    private String reply;
    private String replyer;

    @Builder.Default
    private Long gno = 0L;

    private LocalDate createDate;
    private LocalDate updateDate;

    @Builder.Default
    private Long isDeleted = 0L;
}
