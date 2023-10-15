package board.jpa.querydsl.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import board.jpa.querydsl.dto.like.LikeToggleDTO;
import board.jpa.querydsl.service.LikeService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("spring/like/")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(final LikeService likeService) {
        log.info("Inject Likeservice");
        this.likeService = likeService;
    }

    // // POST : ToggleLike
    @PostMapping("toggle/board/{bno}")
    public ResponseEntity<Map<String, Object>> postToggleLike(@PathVariable("bno") final Long bno,
            final Authentication authentication) {
        log.info("POST | Toggle Like Controller");
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String email = userDetails.getUsername();
        final Long toggleLike = likeService.toggleLike(bno, email);
        final Integer likeCount = likeService.countLike(bno);
        return new ResponseEntity<>(Map.of("toggleLike", toggleLike, "likeCount", likeCount), HttpStatus.OK);
    }

    // GET : Like Count
    @GetMapping("count/board/{bno}")
    public ResponseEntity<Map<String, Object>> getLikeCount(@PathVariable("bno") final Long bno) {
        log.info("GET | Like Count Controller");
        final Integer likeCount = likeService.countLike(bno);
        return new ResponseEntity<>(Map.of("likeCount", likeCount), HttpStatus.OK);
    }

    // GET : Check Toggle Member
    @GetMapping("check/board/member/{bno}")
    public ResponseEntity<Map<String, Object>> getCheckToggleLikeMember(@PathVariable("bno") final Long bno,
            final Authentication authentication) {
        log.info("GET | Check Toggle Like Member Controller");
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String email = userDetails.getUsername();
        final LikeToggleDTO resultToggle = likeService.checkToggleMember(bno, email);
        return new ResponseEntity<>(Map.of("boolean", resultToggle.isLiked()), HttpStatus.OK);
    }
}
