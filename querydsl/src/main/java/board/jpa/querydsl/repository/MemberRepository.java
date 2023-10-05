package board.jpa.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import board.jpa.querydsl.domain.member.MemberEntity;
import board.jpa.querydsl.repository.serach.MemberSearch;

public interface MemberRepository extends JpaRepository<MemberEntity, String>, MemberSearch {

    @Query("SELECT tm FROM MemberEntity tm JOIN tm.memberRoleEntities tmr WHERE tm.email = :email")
    MemberEntity selectOne(@Param("email") String email);

    @Query("SELECT tm FROM MemberEntity tm JOIN tm.memberRoleEntities tmr WHERE tm.email = :email")
    MemberEntity readMember(@Param("email") String email);
}
