package board.jpa.querydsl.repository.serach;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;

import board.jpa.querydsl.domain.board.BoardEntity;
import board.jpa.querydsl.dto.board.BoardListDTO;
import board.jpa.querydsl.entity.QBoardEntity;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(BoardEntity.class);
    }

    @Override
    public PageResponseDTO<BoardListDTO> listBoard(PageRequestDTO pageRequestDTO) {
        QBoardEntity qBoardEntity = QBoardEntity.boardEntity;

        JPQLQuery<BoardEntity> query = from(qBoardEntity);

        BooleanBuilder builder = new BooleanBuilder();

        // 검색 조건
        if (StringUtils.isNoneBlank(pageRequestDTO.getKeyword(), pageRequestDTO.getType())) {
            // tc->[t,c]
            String[] searchArr = pageRequestDTO.getType().split("");
            BooleanBuilder searchBuilder = new BooleanBuilder();
            for (String type : searchArr) {
                switch (type) {
                    case "t" -> searchBuilder.or(qBoardEntity.title.contains(pageRequestDTO.getKeyword()));
                    case "c" -> searchBuilder.or(qBoardEntity.content.contains(pageRequestDTO.getKeyword()));
                    case "w" -> searchBuilder.or(qBoardEntity.writer.contains(pageRequestDTO.getKeyword()));
                }
            }
            builder.and(searchBuilder);
        }

        // 날짜 검색 조건
        if (pageRequestDTO.getStartDate() != null && pageRequestDTO.getEndDate() != null) {
            builder.and(qBoardEntity.createDate.between(
                    pageRequestDTO.getStartDate(),
                    pageRequestDTO.getEndDate()));
        }

        query.where(builder);

        int pageNum = pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1;

        Pageable pageable = PageRequest.of(pageNum, pageRequestDTO.getSize(), Sort.by("bno").descending());

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<BoardListDTO> list = query.select(
                Projections.bean(BoardListDTO.class,
                        qBoardEntity.bno,
                        qBoardEntity.title,
                        qBoardEntity.writer,
                        qBoardEntity.content,
                        qBoardEntity.createDate,
                        qBoardEntity.updateDate));
        List<BoardListDTO> dtoList = list.fetch();
        Long totalCount = list.fetchCount();
        return new PageResponseDTO<>(dtoList, totalCount, pageRequestDTO);
    }
}