package board.jpa.querydsl.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import board.jpa.querydsl.dto.reply.ReplyCreateDTO;
import board.jpa.querydsl.dto.reply.ReplyListDTO;
import board.jpa.querydsl.dto.reply.ReplyUpdateDTO;
import board.jpa.querydsl.service.ReplyService;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("spring/reply/")
public class ReplyController {

    private final ReplyService replyService;

    @Autowired
    public ReplyController(final ReplyService replyService) {
        log.info("Inject ReplyService");
        this.replyService = replyService;
    }

    @GetMapping("list/{bno}")
    public ResponseEntity<Map<String,Object>> listReply(@PathVariable("bno") final Long bno,
            final PageRequestDTO pageRequestDTO) {
        log.info("GET | Reply List Controller ");
        final PageResponseDTO<ReplyListDTO> listReply = replyService.listReply(pageRequestDTO, bno);
        return new ResponseEntity<>(Map.of("listReply", listReply), HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<Map<String,Object>> createReply(final ReplyCreateDTO replyCreateDTO) {
        log.info("POST | Reply Create Controller");
        final Long createReply = replyService.createReply(replyCreateDTO);
        return new ResponseEntity<>(Map.of("createReply", createReply), HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<Map<String,Object>> updateReply(final ReplyUpdateDTO replyUpdateDTO) {
        log.info("PUT | Reply Update Controller");
        final Long updateReply = replyService.updateReply(replyUpdateDTO);
        return new ResponseEntity<>(Map.of("updateReply", updateReply), HttpStatus.OK);
    }

    @DeleteMapping("delete/{rno}")
    public ResponseEntity<Map<String,Object>> deleteReply(@PathVariable("rno") Long rno) {
        log.info("DELETE | Reply Delete Controller");
        final Long deleteReply = replyService.deleteReply(rno);
        return new ResponseEntity<>(Map.of("deleteReply", deleteReply), HttpStatus.OK);
    }
}