package board.jpa.querydsl.repository.serach;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import board.jpa.querydsl.domain.reply.QReplyEntity;
import board.jpa.querydsl.domain.reply.ReplyEntity;
import board.jpa.querydsl.dto.reply.ReplyListDTO;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;

public class ReplySearchImpl extends QuerydslRepositorySupport implements ReplySearch {

    public ReplySearchImpl() {
        super(ReplyEntity.class);
    }

    @Override
    public PageResponseDTO<ReplyListDTO> listReply(PageRequestDTO pageRequestDTO, Long bno) {
        QReplyEntity qReplyEntity = QReplyEntity.replyEntity;

        JPQLQuery<ReplyEntity> query = from(qReplyEntity)
                .orderBy(qReplyEntity.rno.asc())
                .orderBy(qReplyEntity.gno.asc());

        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNoneBlank(pageRequestDTO.getKeyword(), pageRequestDTO.getType())) {
            String[] searchArr = pageRequestDTO.getType().split("");
            BooleanBuilder searchBuilder = new BooleanBuilder();
            for (String type : searchArr) {
                switch (type) {
                    case "r" -> searchBuilder.or(qReplyEntity.reply.contains(pageRequestDTO.getKeyword()));
                    case "ry" -> searchBuilder.or(qReplyEntity.replyer.contains(pageRequestDTO.getKeyword()));
                }
            }
        }
        if (pageRequestDTO.getStartDate() != null && pageRequestDTO.getEndDate() != null) {
            builder.and(qReplyEntity.createDate.between(pageRequestDTO.getStartDate(),
                    pageRequestDTO.getEndDate()));
        }
        query.where(builder.and(qReplyEntity.bno.eq(bno)));

        int total = (int) query.fetchCount();
        int lastPage = (int) Math.ceil(total / (double) pageRequestDTO.getSize());

        Pageable pageable;

        if (!pageRequestDTO.isReplyLast()) {
            int pageNum = lastPage - 1; // 0-based 페이지 번호로 변환
            pageable = PageRequest.of(pageNum, pageRequestDTO.getSize(), Sort.by("createDate").descending());
        } else {
            int pageNum = pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1;
            pageable = PageRequest.of(pageNum, pageRequestDTO.getSize(), Sort.by("createDate").descending());
        }

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<ReplyListDTO> dtoQuery = query.select(Projections.bean(ReplyListDTO.class,
                new CaseBuilder()
                        .when(qReplyEntity.rno
                                .eq(qReplyEntity.gno))
                        .then("0")
                        .otherwise("1")
                        .as("step"),
                qReplyEntity.bno,
                qReplyEntity.rno,
                qReplyEntity.gno,
                qReplyEntity.reply,
                qReplyEntity.replyer,
                qReplyEntity.isDeleted,
                qReplyEntity.createDate,
                qReplyEntity.updateDate));

        List<ReplyListDTO> dtoList = dtoQuery.fetch();
        long totalCount = dtoQuery.fetchCount();

        return new PageResponseDTO<>(dtoList, totalCount, pageRequestDTO);
    }
}
