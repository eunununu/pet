package com.example.pet.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.pet.dto.MemberDTO;
import com.example.pet.dto.PageRequestDTO;
import com.example.pet.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String memberJoin(Model model, Principal principal) {

        if (principal != null) {
            return "redirect:/member/mypage";
        }

        model.addAttribute("memberDTO", new MemberDTO());
        return "member/join";
    }

    @PostMapping("/join")
    public String memberJoinPost(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        try {
            memberService.saveMember(memberDTO);

            return "redirect:/member/login";

        } catch (IllegalStateException e) {
            model.addAttribute("msg", e.getMessage());
            return "member/join";
        }
    }

    @GetMapping("/login")
    public String loginMember(Principal principal) {
        if (principal != null) {
            return "redirect:/member/mypage";
        }

        return "member/login";
    }


    @PostMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        request.getSession().invalidate();
        model.addAttribute("msg", "로그아웃 되었습니다.");
        return "redirect:/";
    }

    @GetMapping("/findid")
    public String memberFindid() {

        return "member/findid";
    }

    @GetMapping("/findpwd")
    public String memberFindpwd() {

        return "member/findpwd";
    }


    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String memberList(PageRequestDTO pageRequestDTO, Model model, Principal principal) {

        return "member/list";
    }

    @GetMapping("/mypage")
    @PreAuthorize("isAuthenticated()")
    public String memberRead(Principal principal, Model model) {

        if (principal == null) {
            return "redirect:/member/login";
        }
        return "member/mypage";
    }

    @GetMapping("/modify")
    public String memberModify(@Valid MemberDTO memberDTO, BindingResult bindingResult,
                               Principal principal, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/modify";
        }

        String email = principal.getName();

        if (!memberDTO.getEmail().equals(email)) {
            model.addAttribute("msg", "회원 정보가 일치하지 않습니다.");
            return "member/modify";
        }

        try {
            memberService.modifyMember(memberDTO);
            model.addAttribute("msg", "회원 정보가 수정되었습니다.");
            return "redirect:/member/mypage";

        } catch (Exception e) {
            model.addAttribute("msg", e.getMessage());
            return "member/modify";
        }

    }


}

