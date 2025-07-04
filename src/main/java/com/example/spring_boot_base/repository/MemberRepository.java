package com.example.spring_boot_base.repository;

import com.example.spring_boot_base.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByEmail(String email);
}