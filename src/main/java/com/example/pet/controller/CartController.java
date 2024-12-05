package com.example.pet.controller;

import com.example.pet.dto.CartDetailDTO;
import com.example.pet.dto.CartItemDTO;
import com.example.pet.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity order(@Valid CartItemDTO cartItemDTO, BindingResult bindingResult, Principal principal){

        if (bindingResult.hasErrors()){

            StringBuffer sb = new StringBuffer();

            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrorList){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String identity = principal.getName();

        try {

            Long cartItemId = cartService.addCart(cartItemDTO, identity);

            return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);

        }catch (Exception e){

            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/cart")
    public String orderHist(Principal principal, Model model){

        List<CartDetailDTO> cartDetailDTOList =
                cartService.getCartList(principal.getName());

        model.addAttribute("cartDetailDTOList", cartDetailDTOList);

        return "cart/cartList";
    }
}
