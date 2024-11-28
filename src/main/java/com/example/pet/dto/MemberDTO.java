package com.example.pet.dto;

import com.example.pet.constant.Role;
import com.example.pet.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Long id;

    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 2, max = 12, message = "이름은 2자 이상 12자 이하로 작성해주세요")
    private String name;

    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 4, max = 12, message = "아이디는 4자 이상 12자 이하로 작성해주세요")
    private String identity;

    @Email(message = "이메일 형식으로 작성해주세요")
    @NotBlank(message = "이메일을 입력해주세요")
    @Size(max = 50, message = "이메일은 50자 이하로 작성해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하로 작성해주세요")
    private String password;

    private String address;

    private Role role;

    public Member dtoToEntity(MemberDTO memberDTO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setAddress(memberDTO.getAddress());
        member.setIdentity(memberDTO.getIdentity());
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        member.setRole(Role.ADMIN);

        return member;
    }
}
