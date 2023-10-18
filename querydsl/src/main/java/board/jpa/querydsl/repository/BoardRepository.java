package board.jpa.querydsl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import board.jpa.querydsl.domain.board.BoardEntity;
import board.jpa.querydsl.repository.serach.BoardSearch;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, BoardSearch {

    @Modifying
    @Query("UPDATE BoardEntity b SET b.viewCount = b.viewCount + 1 WHERE b.bno = :bno")
    Integer incrementViewCount(@Param("bno") Long bno);

    Optional<BoardEntity> findByBno(@Param("bno") Long bno);
}
