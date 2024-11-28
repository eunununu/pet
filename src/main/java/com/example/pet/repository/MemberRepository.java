package com.example.pet.repository;

import com.example.pet.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Member findByEmail(String email);

    Member findByIdentity(String identity);

}
