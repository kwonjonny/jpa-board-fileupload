package board.jpa.querydsl.repository.serach;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;

import board.jpa.querydsl.domain.member.MemberEntity;
import board.jpa.querydsl.domain.member.QMemberEntity;
import board.jpa.querydsl.domain.member.QMemberRoleEntity;
import board.jpa.querydsl.dto.member.MemberListDTO;
import board.jpa.querydsl.util.page.PageRequestDTO;
import board.jpa.querydsl.util.page.PageResponseDTO;

public class MemberSearchImpl extends QuerydslRepositorySupport implements MemberSearch {

    public MemberSearchImpl() {
        super(MemberEntity.class);
    }

    @Override
    public PageResponseDTO<MemberListDTO> listMember(PageRequestDTO pageRequestDTO) {
        QMemberEntity qMemberEntity = QMemberEntity.memberEntity;
        QMemberRoleEntity qMemberRoleEntity = QMemberRoleEntity.memberRoleEntity;

        JPQLQuery<MemberEntity> query = from(qMemberEntity)
                .leftJoin(qMemberRoleEntity)
                .on(qMemberEntity.email.eq(qMemberRoleEntity.memberEntity.email))
                .orderBy(qMemberEntity.createDate.desc());

        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNoneBlank(pageRequestDTO.getKeyword(), pageRequestDTO.getType())) {
            String[] searchArr = pageRequestDTO.getType().split("");
            BooleanBuilder searchBuilder = new BooleanBuilder();
            for (String type : searchArr) {
                switch (type) {
                    case "e" -> searchBuilder.or(qMemberEntity.email.contains(pageRequestDTO.getKeyword()));
                    case "n" -> searchBuilder.or(qMemberEntity.memberName.contains(pageRequestDTO.getKeyword()));
                    case "p" -> searchBuilder.or(qMemberEntity.memberPhone.contains(pageRequestDTO.getKeyword()));
                }
            }
        }
        if (pageRequestDTO.getStartDate() != null && pageRequestDTO.getEndDate() != null) {
            builder.and(qMemberEntity.createDate.between(
                    pageRequestDTO.getStartDate(),
                    pageRequestDTO.getEndDate()));
        }
        query.where(builder);
        int pageNum = pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1;

        Pageable pageable = PageRequest.of(pageNum, pageRequestDTO.getSize(), Sort.by("createDate").descending());

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<MemberListDTO> list = query.select(
                Projections.bean(MemberListDTO.class,
                        qMemberEntity.email,
                        qMemberEntity.memberPw,
                        qMemberEntity.memberPhone,
                        qMemberEntity.createDate,
                        qMemberEntity.updateDate,
                        qMemberEntity.memberName,
                        qMemberRoleEntity.roleName.as("rolenames")));
        List<MemberListDTO> dtoList = list.fetch();
        Long totalCount = list.fetchCount();
        return new PageResponseDTO<>(dtoList, totalCount, pageRequestDTO);
    }
}