package board.jpa.querydsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.dto.BoardCreateDTO;
import board.jpa.querydsl.dto.BoardDTO;
import board.jpa.querydsl.dto.BoardListDTO;
import board.jpa.querydsl.dto.BoardUpdateDTO;
import board.jpa.querydsl.entity.BoardEntity;
import board.jpa.querydsl.exception.BoardNumberNotFoundException;
import board.jpa.querydsl.exception.DataNotFoundException;
import board.jpa.querydsl.repository.BoardRepository;
import board.jpa.querydsl.service.BoardService;
import board.jpa.querydsl.util.PageRequestDTO;
import board.jpa.querydsl.util.PageResponseDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardServiceImpl(final BoardRepository boardRepository) {
        log.info("Inject BoardRepository");
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional
    public Long createBoard(final BoardCreateDTO boardCreateDTO) {
        log.info("Is Running Create Board ServiceImpl");
        if (boardCreateDTO.getWriter() == null || boardCreateDTO.getContent() == null
                || boardCreateDTO.getTitle() == null) {
            throw new DataNotFoundException("작성자, 제목, 내용은 필수 사항입니다.");
        }
        BoardEntity boardEntity = BoardEntity.createBoard(boardCreateDTO.getTitle(), boardCreateDTO.getContent(),
                boardCreateDTO.getWriter());
        return boardRepository.save(boardEntity).getBno();
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDTO readBoard(final Long bno) {
        log.info("Is Running Read Board ServiceImpl");
        BoardEntity boardEntity = boardRepository.findById(bno)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물의 번호가 없습니다." + bno));
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(boardEntity.getBno())
                .writer(boardEntity.getWriter())
                .content(boardEntity.getContent())
                .updateDate(boardEntity.getUpdateDate())
                .createDate(boardEntity.getCreateDate())
                .build();
        return boardDTO;
    }

    @Override
    @Transactional
    public Long updateBoard(final BoardUpdateDTO boardUpdateDTO) {
        log.info("Is Running Update Board ServiceImpl");
        BoardEntity boardEntity = boardRepository.findById(boardUpdateDTO.getBno())
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물의 번호가 없습니다." + boardUpdateDTO.getBno()));
        if (boardUpdateDTO.getBno() == null || boardUpdateDTO.getContent() == null || boardUpdateDTO.getWriter() == null
                || boardUpdateDTO.getTitle() == null) {
            throw new DataNotFoundException("게시물 번호, 작성자, 제목, 내용은 필수 사항입니다.");
        }
        boardEntity.updateBoard(boardUpdateDTO.getWriter(), boardUpdateDTO.getContent(), boardUpdateDTO.getTitle());
        return boardRepository.save(boardEntity).getBno();
    }

    @Override
    @Transactional
    public Long deleteBoard(final Long bno) {
        log.info("Is Running Delete Board ServiceImpl");
        BoardEntity boardEntity = boardRepository.findById(bno)
                .orElseThrow(() -> new BoardNumberNotFoundException("해당하는 게시물의 번호가 없습니다." + bno));
        boardRepository.deleteById(bno);
        return boardEntity.getBno();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<BoardListDTO> listBoard(final PageRequestDTO pageRequest) {
       log.info("Is Running List Board ServiceImpl");
       return boardRepository.listBoard(pageRequest);
    }
}
