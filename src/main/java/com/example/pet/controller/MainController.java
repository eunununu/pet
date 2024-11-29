package com.example.pet.controller;

import com.example.pet.dto.ItemDTO;
import com.example.pet.dto.PageRequestDTO;
import com.example.pet.dto.PageResponseDTO;
import com.example.pet.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(PageRequestDTO pageRequestDTO, Model model) {


        pageRequestDTO.setSize(6);

        PageResponseDTO<ItemDTO> pageResponseDTO = itemService.mainlist(pageRequestDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "main";
    }

    @GetMapping(value = "/dog")
    public String mainDog(PageRequestDTO pageRequestDTO, Model model) {

        pageRequestDTO.setSize(6);
        pageRequestDTO.setCategory1("DOG");
        pageRequestDTO.setCategory2("FEED");

        PageResponseDTO<ItemDTO> pageResponseDTO = itemService.maindog(pageRequestDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "main/dog";
    }

    @GetMapping(value = "/cat")
    public String mainCat(PageRequestDTO pageRequestDTO, Model model) {

        pageRequestDTO.setSize(6);
        pageRequestDTO.setCategory1("CAT");
        pageRequestDTO.setCategory2("FEED");

        PageResponseDTO<ItemDTO> pageResponseDTO = itemService.maincat(pageRequestDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "main/cat";
    }

}
