package board.jpa.querydsl.dto.member;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class MemberUpdateDTO {
    // tbl_member
    @NotBlank(message = "email Should Be Not NotBlank")
    private String email;

    @NotBlank(message = "memberName Should Be Not NotBlank")
    private String memberName;

    @NotBlank(message = "memberPw Should Be Not NotBlank")
    private String memberPw;

    @NotNull(message = "memberPhone Should Be Not NotBlank")
    private String memberPhone;

    @NotBlank(message = "createDate Should Be Not Blank")
    private LocalDate createDate;
}
