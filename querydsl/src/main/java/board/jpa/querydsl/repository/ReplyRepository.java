package board.jpa.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import board.jpa.querydsl.domain.reply.ReplyEntity;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>{
    
}
