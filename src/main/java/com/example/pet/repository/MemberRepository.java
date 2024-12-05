package com.example.pet.repository;

import com.example.pet.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {


    @Query("select m.email from Member  m  where  m.email = :add and m.name = :name")
    public String findEmail (String add, String name);

    @Query("select m.identity from Member m where  m.identity = :email and m.name = :name")
    public String findIdentity (String email, String name);

    Member findByEmail(String email);

    Member findByIdentity(String identity);

}
