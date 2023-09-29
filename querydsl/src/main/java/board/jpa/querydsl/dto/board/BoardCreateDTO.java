package board.jpa.querydsl.dto.board;

import java.util.ArrayList;
import java.util.List;

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
public class BoardCreateDTO {
    private Long bno;

    @NotBlank
    private String title;

    @NotBlank
    private String writer;

    @NotBlank
    private String content;

    @Builder.Default
    private List<String> fileName = new ArrayList<>();    
}
