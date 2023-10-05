package board.jpa.querydsl.dto.member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
public class MemberListDTO {
   // tbl_member
   private String email;
   private String memberName;
   private String memberPw;
   private String memberPhone;
   private LocalDate createDate;
   private LocalDate updateDate;
   private String rolenames;
}
