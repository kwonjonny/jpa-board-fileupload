package board.jpa.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import board.jpa.querydsl.domain.reply.ReplyEntity;
import board.jpa.querydsl.repository.serach.ReplySearch;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>, ReplySearch {

    @Modifying
    @Query("UPDATE ReplyEntity r SET r.gno = :rno WHERE r.rno = :rno")
    void updateGno(@Param("rno") Long rno);

}
