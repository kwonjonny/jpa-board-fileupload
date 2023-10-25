package board.jpa.querydsl.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.domain.board.BoardEntity;
import board.jpa.querydsl.domain.like.LikeEntity;
import board.jpa.querydsl.domain.member.MemberEntity;
import board.jpa.querydsl.dto.like.LikeToggleDTO;
import board.jpa.querydsl.exception.BoardNumberNotFoundException;
import board.jpa.querydsl.exception.DataNotFoundException;
import board.jpa.querydsl.exception.LikeToggleNotFoundException;
import board.jpa.querydsl.exception.MemberNotFoundException;
import board.jpa.querydsl.exception.errorcode.BoardErrorMessage;
import board.jpa.querydsl.exception.errorcode.LikeErroreMessage;
import board.jpa.querydsl.exception.errorcode.MemberErrorMessage;
import board.jpa.querydsl.repository.BoardRepository;
import board.jpa.querydsl.repository.LikeRepository;
import board.jpa.querydsl.repository.MemberRepository;
import board.jpa.querydsl.service.LikeService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public LikeServiceImpl(final LikeRepository likeRepository, final BoardRepository boardRepository,
            final MemberRepository memberRepository) {
        log.info("Inject LikeRepository");
        this.likeRepository = likeRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public Long toggleLike(final Long bno, final String email) {
        log.info("Is Running Toggle Like ServiceImpl");
        validationCreateData(bno, email);
        
        final BoardEntity boardEntity = getBoardEntityById(bno);
        final MemberEntity memberEntity = getMemberEntityByEmail(email);

        LikeToggleDTO likeToggleDTO = LikeToggleDTO.builder()
                .bno(bno)
                .email(email)
                .build();

        LikeEntity likeEntity = likeRepository
                .findByEmailAndBno(likeToggleDTO.getBno(), likeToggleDTO.getEmail())
                .orElse(null);
        if (likeEntity == null) {
            likeEntity = LikeEntity.createLike(likeToggleDTO.getEmail(), likeToggleDTO.getBno(), LocalDate.now());
            likeRepository.save(likeEntity);
            boardEntity.incrementLikeCount();
        } else {
            likeRepository.deleteByEmailAndBno(likeToggleDTO.getBno(), likeToggleDTO.getEmail());
            boardEntity.decrementLikeCount();
        }
        boardRepository.save(boardEntity);
        return likeToggleDTO.getBno();
    }

    @Transactional(readOnly = true)
    private void validationCreateData(final Long bno, final String email) {
        if (bno == null || email == null) {
            throw new DataNotFoundException(
                    LikeErroreMessage.DATA_NOT_FOUND.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Integer countLike(final Long bno) {
        log.info("Is Running CountLike serviceImpl");
        final BoardEntity boardEntity = getBoardEntityById(bno);
        final LikeEntity likeEntity = getLikeEntityByBno(bno);
        Integer likeCount = likeRepository.countByEmailAndBno(bno);
        return likeCount;
    }

    @Override
    @Transactional
    public LikeToggleDTO checkToggleMember(final Long bno, final String email) {
        log.info("Is Running Check Toggle Member ServiceImpl");
        final BoardEntity boardEntity = getBoardEntityById(bno);
        final MemberEntity memberEntity = getMemberEntityByEmail(email);

        LikeToggleDTO likeToggleDTO = LikeToggleDTO
                .builder()
                .email(email)
                .bno(bno)
                .build();

        final LikeEntity likeEntity = likeRepository
                .checkToggleLikeMember(likeToggleDTO.getBno(), likeToggleDTO.getEmail()).orElse(null);
        if (likeEntity != null) {
            likeToggleDTO.setLiked(true);
        } else {
            likeToggleDTO.setLiked(false);
        }
        return likeToggleDTO;
    }

    @Transactional(readOnly = true)
    private BoardEntity getBoardEntityById(final Long bno) {
        return boardRepository.findById(bno)
                .orElseThrow(() -> new BoardNumberNotFoundException(
                        String.format(
                                BoardErrorMessage.BOARD_NUMBER_NOT_FOUND
                                        .getFormattedMessage(String.valueOf(bno)))));
    }

    @Transactional(readOnly = true)
    private MemberEntity getMemberEntityByEmail(final String email) {
        return memberRepository.findById(email)
                .orElseThrow(() -> new MemberNotFoundException(
                        String.format(
                                MemberErrorMessage.MEMBER_NOT_FOUND
                                        .getFormattedMessage(email))));
    }

    @Transactional(readOnly = true)
    private LikeEntity getLikeEntityByBno(final Long bno) {
        return likeRepository.findByLikeBno(bno)
                .orElseThrow(() -> new LikeToggleNotFoundException(
                        String.format(BoardErrorMessage.BOARD_NUMBER_NOT_FOUND
                                .getFormattedMessage(String.valueOf(bno)))));
    }
}