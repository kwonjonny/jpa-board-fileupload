package board.jpa.querydsl.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
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
public class BoardDTO {
    private Long bno;

    @NotBlank
    private String writer;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private LocalDate createDate;

    @NotBlank
    private LocalDate updateDate;
}
