package board.jpa.querydsl.dto.like;

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
public class LikeToggleDTO {
    private String email;
    private Long bno;
    private LocalDate createDate;
    private boolean liked;
}
