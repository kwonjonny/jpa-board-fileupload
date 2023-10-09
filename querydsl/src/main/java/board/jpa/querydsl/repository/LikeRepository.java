package board.jpa.querydsl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import board.jpa.querydsl.domain.like.LikeEntity;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    @Query("SELECT like FROM LikeEntity like WHERE like.bno = :bno AND like.email = :email")
    Optional<LikeEntity> findByEmailAndBno(@Param("bno") Long bno, @Param("email") String email);

    @Modifying
    @Query("DELETE FROM LikeEntity like WHERE like.bno = :bno AND like.email = :email")
    void deleteByEmailAndBno(@Param("bno") Long bno, @Param("email") String email);

    @Query("SELECT COUNT(*) FROM LikeEntity like WHERE like.bno = :bno")
    Integer countByEmailAndBno(@Param("bno") Long bno);

    @Query("SELECT like FROM LikeEntity like WHERE like.bno = :bno")
    Optional<LikeEntity> findByLikeBno(@Param("bno") Long bno);

    @Query("SELECT like FROM LikeEntity like WHERE like.bno = :bno AND like.email = :email")
    Optional<LikeEntity> checkToggleLikeMember(@Param("bno") Long bno, @Param("email") String email);
}
