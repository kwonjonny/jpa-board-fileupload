package board.jpa.querydsl.service;

import board.jpa.querydsl.dto.like.LikeToggleDTO;

public interface LikeService {
    Long toggleLike(LikeToggleDTO likeToggleDTO);

    Integer countLike(Long bno);

    LikeToggleDTO checkToggleMember(LikeToggleDTO likeToggleDTO);
}
