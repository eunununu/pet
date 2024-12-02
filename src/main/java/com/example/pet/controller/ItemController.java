package com.example.pet.controller;

import com.example.pet.dto.ItemDTO;
import com.example.pet.dto.PageRequestDTO;
import com.example.pet.dto.PageResponseDTO;
import com.example.pet.entity.Item;
import com.example.pet.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/admin/item/register")
    public String itemRegister(Model model, Principal principal) {

        if (principal == null) {

        }
        if (principal != null) {
            log.info("현재 로그인한 사람");
            log.info(principal.getName());
        }
        model.addAttribute("itemDTO", new ItemDTO());

        return "/item/register";
    }

    @PostMapping("/admin/item/register")
    public String itemRegisterPost(@Valid ItemDTO itemDTO, BindingResult bindingResult,
                                   List<MultipartFile> multipartFile, Model model) {

        log.info("들어오는 값 확인 : " + itemDTO);

        if (multipartFile.get(0).isEmpty()) {
            model.addAttribute("msg", "대표 이미지는 꼭 등록해주세요");
            return "/item/register";

        }

        if (multipartFile != null) {
            for (MultipartFile img : multipartFile) {
                if (!img.getOriginalFilename().equals("")) {
                    log.info(img.getOriginalFilename());
                }
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("유효성검사 에러");
            log.info(bindingResult.getAllErrors());


            return "/item/register";
        }
        try {
            Long savedItemid =
                    itemService.saveItem(itemDTO, multipartFile);

            log.info("상품이 등록되었습니다.");

            return "redirect:/admin/item/adminread?id=" + savedItemid;

        } catch (Exception e) {
            e.printStackTrace();
            log.info("파일 등록 중 문제가 발생했습니다.");
            model.addAttribute("msg", "파일 등록을 다시 해주세요");
            return "/item/register";
        }

    }


    @GetMapping("/admin/item/adminread")
    public String adminread(Long id, Model model, RedirectAttributes redirectAttributes) {

        try {
            ItemDTO itemDTO =
                    itemService.read(id);

            model.addAttribute("itemDTO", itemDTO);

            return "item/adminread";

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("msg", "존재하지 않는 상품입니다.");

            return "redirect:/admin/item/adminlist";
        }
    }

    @GetMapping("/admin/item/adminlist")
    public String itemAdminList(PageRequestDTO pageRequestDTO,
                                Model model, Principal principal) {

        PageResponseDTO<ItemDTO> pageResponseDTO =
                itemService.list(pageRequestDTO, principal.getName());

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "item/adminlist";
    }

    @GetMapping("/item/list")
    public String itemList(PageRequestDTO pageRequestDTO, Model model, Principal principal) {

        String identity = (principal != null) ? principal.getName() : null;

        PageResponseDTO<ItemDTO> pageResponseDTO =
                itemService.list(pageRequestDTO, principal.getName());

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "item/list";
    }

    @GetMapping("/admin/item/modify")
    public String itemModify(Long id, PageRequestDTO pageRequestDTO,
                             Model model, Principal principal) {

        ItemDTO itemDTO = itemService.read(id, principal.getName());
        if (itemDTO != null) {
            model.addAttribute("itemDTO", itemDTO);
            return "item/modify";
        } else {
            return "redirect:/admin/item/adminlist";
        }
    }

    @PostMapping("/admin/item/modify")
    public String itemModifyPost(@Valid ItemDTO itemDTO, BindingResult bindingResult,
                                 List<MultipartFile> multipartFile, Integer[] delino, Long mainino) {

        if (bindingResult.hasErrors()) {

            return "/item/modify";
        }

        itemService.modify(itemDTO, itemDTO.getId(), multipartFile, delino, mainino);

        return null;
    }

    @PostMapping("/admin/item/remove")
    public String itemRemove(Long id) {

        itemService.remove(id);

        return "redirect:/admin/item/adminlist";
    }

    @GetMapping("/item/read")
    public String itemRead(Long id, Model model, RedirectAttributes redirectAttributes) {

        try {
            ItemDTO itemDTO = itemService.read(id);

            model.addAttribute("itemDTO", itemDTO);

            return "item/read";

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("msg", "존재하지 않는 상품입니다.");

            return "redirect:/";
        }
    }


}
