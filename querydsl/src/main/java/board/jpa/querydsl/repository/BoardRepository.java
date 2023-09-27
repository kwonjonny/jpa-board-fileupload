package board.jpa.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import board.jpa.querydsl.entity.BoardEntity;
import board.jpa.querydsl.repository.serach.BoardSearch;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, BoardSearch {

}
