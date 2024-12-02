package com.example.pet.service;

import com.example.pet.constant.Role;
import com.example.pet.dto.MemberDTO;
import com.example.pet.entity.Member;
import com.example.pet.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String identity) throws UsernameNotFoundException {

        Member member = memberRepository.findByIdentity(identity);

        if (member == null) {
            throw new UsernameNotFoundException(identity);
        }

        return User.builder()
                .username(member.getIdentity())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
    }

    public Member saveMember(MemberDTO memberDTO) {

        validateDuplicateMember(memberDTO.getEmail(), memberDTO.getIdentity());

        Member member = memberDTO.dtoToEntity(memberDTO);

        member = memberRepository.save(member);

        return member;
    }
    public Member saveMember1(MemberDTO memberDTO) {

        validateDuplicateMember(memberDTO.getEmail(), memberDTO.getIdentity());

        Member member = memberDTO.dtoToEntity(memberDTO);

        member.setRole(Role.ADMIN);
        member = memberRepository.save(member);


        return member;
    }

    private void validateDuplicateMember(String email, String identity) {

        Member member =
                memberRepository.findByEmail(email);

        Member member1 =
                memberRepository.findByIdentity(identity);

        if (member != null) {
            throw new IllegalStateException("이미 회원가입된 이메일입니다.");
        }

        if (member1 != null) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    public Member modifyMember(MemberDTO memberDTO) {

        Member member = memberRepository.findById(memberDTO.getId())
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));

        member.setName(member.getName());
        member.setEmail(memberDTO.getEmail());
        member.setPassword(memberDTO.getPassword());
        member.setAddress(member.getAddress());
        member.setIdentity(memberDTO.getIdentity());

        return memberRepository.save(member);
    }

    public String adsfas(String email, String name){

        return  memberRepository.findEmail(email, name);
    }

}
