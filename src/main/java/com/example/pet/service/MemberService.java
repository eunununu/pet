package com.example.pet.service;

import com.example.pet.constant.Role;
import com.example.pet.dto.MemberDTO;
import com.example.pet.dto.PageRequestDTO;
import com.example.pet.dto.PageResponseDTO;
import com.example.pet.entity.Member;
import com.example.pet.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

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

    public String adsfasf(String identity, String name){

        return  memberRepository.findIdentity(identity, name);
    }


    public PageResponseDTO<MemberDTO> list(PageRequestDTO pageRequestDTO, String identity){

        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Member> members =
                memberRepository.getMemberList(pageable);

        List<MemberDTO> memberDTOPage =
                members.getContent().stream().map(member -> modelMapper.map(member, MemberDTO.class))
                        .collect(Collectors.toList());

        PageResponseDTO<MemberDTO> memberDTOPageResponseDTO =
                PageResponseDTO.<MemberDTO>withAll()
                        .pageRequestDTO(pageRequestDTO)
                        .dtoList(memberDTOPage)
                        .total((int) members.getTotalElements())
                        .build();

        return memberDTOPageResponseDTO;
    }
}
