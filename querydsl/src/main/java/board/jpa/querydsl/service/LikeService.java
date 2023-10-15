package board.jpa.querydsl.service;

import board.jpa.querydsl.dto.like.LikeToggleDTO;

public interface LikeService {
    Long toggleLike(Long bno, String email);

    Integer countLike(Long bno);

    LikeToggleDTO checkToggleMember(Long bno, String email);
}
