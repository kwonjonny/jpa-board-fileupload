package board.jpa.querydsl.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.domain.board.BoardEntity;
import board.jpa.querydsl.domain.board.BoardFileEntity;
import board.jpa.querydsl.dto.board.BoardCreateDTO;
import board.jpa.querydsl.dto.board.BoardDTO;
import board.jpa.querydsl.dto.board.BoardListDTO;
import board.jpa.querydsl.dto.board.BoardUpdateDTO;
import board.jpa.querydsl.exception.BoardNumberNotFoundException;
import board.jpa.querydsl.exception.DataNotFoundException;
import board.jpa.querydsl.exception.errorcode.BoardErrorMessage;
import board.jpa.querydsl.repository.BoardRepository;
import board.jpa.querydsl.service.BoardService;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;
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
        validationCreateData(boardCreateDTO);
        final BoardEntity boardEntity = BoardEntity.createBoard(
                boardCreateDTO.getTitle(),
                boardCreateDTO.getContent(),
                boardCreateDTO.getWriter());
        final BoardEntity saveBoard = boardRepository.save(boardEntity);

        List<String> fileNames = boardCreateDTO.getFileName();
        if (boardCreateDTO.getFileName() != null && !boardCreateDTO.getFileName().isEmpty()) {
            final List<BoardFileEntity> list = fileNames.stream().map(str -> {
                String uuid = str.substring(0, 36);
                String fileName = str.substring(37);
                BoardFileEntity fileEntity = BoardFileEntity.builder()
                        .uuid(uuid)
                        .fileName(fileName)
                        .build();
                fileEntity.setBoardEntity(saveBoard);
                return fileEntity;
            }).collect(Collectors.toList());
            list.forEach(boardEntity::addImage);
            boardRepository.save(saveBoard);
        }
        return saveBoard.getBno();
    }

    @Transactional(readOnly = true)
    private void validationCreateData(final BoardCreateDTO boardCreateDTO) {
        if (boardCreateDTO.getWriter() == null ||
                boardCreateDTO.getContent() == null ||
                boardCreateDTO.getTitle() == null) {
            throw new DataNotFoundException(BoardErrorMessage.DATA_NOT_FOUND.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDTO readBoard(final Long bno) {
        log.info("Is Running Read Board ServiceImpl");
        findBoardNumber(bno);
        final BoardEntity boardEntity = boardRepository.findById(bno)
                .orElse(null);
        final List<String> fileNames = boardEntity.getFileNames()
                .stream()
                .map(BoardFileEntity::getFileName)
                .collect(Collectors.toList());
        final BoardDTO boardDTO = BoardDTO.builder()
                .bno(boardEntity.getBno())
                .title(boardEntity.getTitle())
                .writer(boardEntity.getWriter())
                .content(boardEntity.getContent())
                .updateDate(boardEntity.getUpdateDate())
                .createDate(boardEntity.getCreateDate())
                .viewCount(boardEntity.getViewCount())
                .fileName(fileNames)
                .build();
        return boardDTO;
    }

    @Transactional(readOnly = true)
    private void findBoardNumber(final Long bno) {
        log.info("Is Running Find Board Number ServiceImpl");
        final BoardEntity boardEntity = boardRepository.findById(bno)
                .orElseThrow(
                        () -> new DataNotFoundException(
                                BoardErrorMessage.BOARD_NUMBER_NOT_FOUND.getFormattedMessage(String.valueOf(bno))));
    }

    @Override
    @Transactional
    public Long updateBoard(final BoardUpdateDTO boardUpdateDTO) {
        log.info("Is Running Update Board ServiceImpl");
        findBoardNumber(boardUpdateDTO.getBno());
        validationUpdateData(boardUpdateDTO);

        final BoardEntity boardEntity = boardRepository.findById(boardUpdateDTO.getBno())
                .orElse(null);

        boardEntity.updateBoard(boardUpdateDTO.getWriter(), boardUpdateDTO.getContent(), boardUpdateDTO.getTitle());
        final BoardEntity updateBoard = boardRepository.save(boardEntity);
        List<String> fileNames = boardUpdateDTO.getFileName();
        if (boardUpdateDTO.getFileName() != null && !boardUpdateDTO.getFileName().isEmpty()) {
            boardEntity.clearImage();
            boardRepository.save(boardEntity);
            final List<BoardFileEntity> list = fileNames.stream().map(str -> {
                String uuid = str.substring(0, 36);
                String fileName = str.substring(37);
                BoardFileEntity fileEntity = BoardFileEntity.builder()
                        .uuid(uuid)
                        .fileName(fileName)
                        .build();
                fileEntity.setBoardEntity(boardEntity);
                return fileEntity;
            }).collect(Collectors.toList());
            list.forEach(boardEntity::addImage);
            boardRepository.save(boardEntity);
        }
        return updateBoard.getBno();
    }

    @Transactional(readOnly = true)
    private void validationUpdateData(final BoardUpdateDTO boardUpdateDTO) {
        log.info("Is Running Validation Update Data ServiceImpl");
        if (boardUpdateDTO.getBno() == null ||
                boardUpdateDTO.getContent() == null ||
                boardUpdateDTO.getWriter() == null ||
                boardUpdateDTO.getTitle() == null) {
            throw new DataNotFoundException(
                    BoardErrorMessage.DATA_NOT_FOUND.getMessage());
        }
    }

    @Override
    @Transactional
    public Long deleteBoard(final Long bno) {
        log.info("Is Running Delete Board ServiceImpl");
        findBoardNumber(bno);
        final BoardEntity boardEntity = boardRepository.findById(bno)
                .orElse(null);
        boardRepository.deleteById(bno);
        boardEntity.clearImage();
        return boardEntity.getBno();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<BoardListDTO> listBoard(final PageRequestDTO pageRequest) {
        log.info("Is Running List Board ServiceImpl");
        return boardRepository.listBoard(pageRequest);
    }

    @Override
    @Transactional
    public Integer incrementViewCount(final Long bno) {
        log.info("Is Running Increment View Count Board ServiceImpl");
        findBoardNumber(bno);
        final BoardEntity boardEntity = boardRepository.findById(bno)
                .orElse(null);
        return boardRepository.incrementViewCount(bno);
    }
}