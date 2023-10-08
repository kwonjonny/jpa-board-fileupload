package board.jpa.querydsl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import board.jpa.querydsl.dto.member.MemberConvertDTO;
import board.jpa.querydsl.dto.member.MemberCreateDTO;
import board.jpa.querydsl.dto.member.MemberListDTO;
import board.jpa.querydsl.dto.member.MemberUpdateDTO;
import board.jpa.querydsl.response.ResponseEntity;
import board.jpa.querydsl.service.MemberService;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("spring/member/")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(final MemberService memberService) {
        log.info("Inject MemberService");
        this.memberService = memberService;
    }

    // GET | Create Member
    @GetMapping("create")
    public String getCreateMember(final Model model) {
        log.info("GET | Create Member Controller");
        model.addAttribute("response", ResponseEntity.success("회원 생성 페이지입니다."));
        return "spring/member/create";
    }

    // POST | Create Member
    @PostMapping("create")
    public String postCreateMember(@Valid final MemberCreateDTO memberCreateDTO,
            final RedirectAttributes redirectAttributes) {
        log.info("POST | Create Member Controller");
        memberService.createMember(memberCreateDTO);
        redirectAttributes.addFlashAttribute("response", ResponseEntity.success("회원 생성 완료."));
        return "redirect:/spring/index";
    }

    // GET | Read Member
    @GetMapping("read/{email}")
    public String getReadMember(@PathVariable("email") final String email, final Model model) {
        log.info("GET | Read Member Controller");
        final MemberConvertDTO list = memberService.readMember(email);
        model.addAttribute("response", ResponseEntity.success(list));
        return "spring/member/read";
    }

    // GET | List Member
    @GetMapping("list")
    public String getListMember(final PageRequestDTO pageRequestDTO, final Model model) {
        log.info("GET | List Member Controller");
        final PageResponseDTO<MemberListDTO> list = memberService.listMember(pageRequestDTO);
        model.addAttribute("response", ResponseEntity.success(list));
        return "spring/member/list";
    }

    // GET | Update Member
    @GetMapping("update/{email}")
    public String getUpdateMember(@PathVariable("email") final String email, final Model model) {
        log.info("GET | Update Member Controller");
        final MemberConvertDTO list = memberService.readMember(email);
        model.addAttribute("response", ResponseEntity.success(list));
        return "spring/member/update";
    }

    // POST | Update Member
    @PostMapping("update")
    public String postUpdateMember(@Valid final MemberUpdateDTO memberUpdateDTO,
            final RedirectAttributes redirectAttributes) {
        log.info("POST | Update Member Controller");
        memberService.updateMember(memberUpdateDTO);
        redirectAttributes.addFlashAttribute("response", ResponseEntity.success("회원 정보 수정 완료."));
        return "redirect:/spring/member/read/" + memberUpdateDTO.getEmail();
    }

    // POST | Delete Member
    @PostMapping("delete/{email}")
    public String postDeleteMember(@PathVariable("email") final String email,
            final RedirectAttributes redirectAttributes) {
        log.info("POST | Delete Member Controller");
        memberService.deleteMember(email);
        redirectAttributes.addFlashAttribute("response", ResponseEntity.success("회원 탈퇴 완료."));
        return "redirect:/spring/index";
    }
}