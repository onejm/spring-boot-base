package com.example.spring_boot_base.entity;

import com.example.spring_boot_base.constant.Role;
import com.example.spring_boot_base.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="t_member")
@Getter @Setter
@ToString
public class Member
{
    @Id
    @Column(name="member_id")

    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String address;
    @Enumerated(EnumType.STRING)

    private Role role;

    // Member 엔터티 생성 메소드.
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword()); // 비밀번호 암호화하여 저장
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }
}