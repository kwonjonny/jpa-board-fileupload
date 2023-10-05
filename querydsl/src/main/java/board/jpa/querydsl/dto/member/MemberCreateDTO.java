package board.jpa.querydsl.dto.member;

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
public class MemberCreateDTO {
    // tbl_member 
    @NotBlank(message = "email Should Be Not Null")
    private String email;

    @NotBlank(message = "memberName Should Be Not Null")
    private String memberName;

    @NotBlank(message = "memberPw Should Be Not Null")
    private String memberPw;

    @NotBlank(message = "memberPhone Should Be Not Null")
    private String memberPhone;
}