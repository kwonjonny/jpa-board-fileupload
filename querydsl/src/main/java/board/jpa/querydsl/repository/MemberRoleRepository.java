package board.jpa.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import board.jpa.querydsl.domain.member.MemberRoleEntity;

public interface MemberRoleRepository extends JpaRepository<MemberRoleEntity, Long>{
}
