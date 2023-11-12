package board.jpa.querydsl.util.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import board.jpa.querydsl.domain.board.BoardEntity;
import board.jpa.querydsl.dto.board.BoardCreateDTO;
import board.jpa.querydsl.dto.board.BoardUpdateDTO;
import board.jpa.querydsl.exception.DataNotFoundException;
import board.jpa.querydsl.exception.errorcode.BoardErrorMessage;
import board.jpa.querydsl.repository.BoardRepository;

@Component
public class BoardValidator {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardValidator(final BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public void findBoardNumber(final Long bno) {
        final BoardEntity boardEntity = boardRepository.findById(bno)
                .orElseThrow(
                        () -> new DataNotFoundException(
                                BoardErrorMessage.BOARD_NUMBER_NOT_FOUND.getFormattedMessage(String.valueOf(bno))));
    }

    public void validationBoardCreateData(final BoardCreateDTO boardCreateDTO) {
        ValidationUtil.validateNotEmnty(boardCreateDTO.getTitle(), "제목은 필수 사항입니다.");
        ValidationUtil.validateNotEmnty(boardCreateDTO.getWriter(), "작성자는 필수 사항입니다.");
        ValidationUtil.validateNotEmnty(boardCreateDTO.getContent(), "내용은 필수 사항입니다.");
    }

    public void validationBoardUpdateData(final BoardUpdateDTO boardUpdateDTO) {
        ValidationUtil.validateNotEmnty(boardUpdateDTO.getTitle(), "제목은 필수 사항입니다.");
        ValidationUtil.validateNotEmnty(boardUpdateDTO.getWriter(), "작성자는 필수 사항입니다.");
        ValidationUtil.validateNotEmnty(boardUpdateDTO.getContent(), "내용은 필수 사항입니다.");
    }
}