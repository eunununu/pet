package com.example.pet.controller;

import com.example.pet.dto.CartDetailDTO;
import com.example.pet.dto.CartItemDTO;
import com.example.pet.dto.CartOrderDTO;
import com.example.pet.service.CartItemService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;


    @PostMapping("/cart")
    public ResponseEntity order(@Valid CartItemDTO cartItemDTO, BindingResult bindingResult, Principal principal){

        log.info("브라우저에서 넘어온 값 " + cartItemDTO);
        log.info("로그인이 되어 있다면 " + principal);

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

    @PostMapping("/cartItem")
    public ResponseEntity modifyCartItem(@Valid CartItemDTO cartItemDTO, BindingResult bindingResult, Principal principal){

        String identity = principal.getName();

        if (bindingResult.hasErrors()){

            StringBuffer sb = new StringBuffer();

            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrorList){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            cartItemService.modifyCartItemId(cartItemDTO, identity);
        }catch (Exception e) {
            return new ResponseEntity<String>("장바구니 수량 변경이 잘못되었습니다. 고객센터에 문의하세요.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemDTO.getItemid(), HttpStatus.OK);
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public ResponseEntity deleteCartItem(@PathVariable("cartItemId")Long cartItemId, Principal principal){

        if (!cartService.validateCartItem(cartItemId, principal.getName())){

            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @PostMapping("/cart/orders")
    public ResponseEntity orderCartItem(@RequestBody CartOrderDTO cartOrderDTO, Principal principal){

        log.info(cartOrderDTO);

        List<CartOrderDTO> cartOrderDTOList = cartOrderDTO.getOrderDTOList();

        if (cartOrderDTOList == null || cartOrderDTOList.size() == 0){

            return new ResponseEntity<String>("주문할 상품을 선택해주세요.", HttpStatus.FORBIDDEN);
        }

        for (CartOrderDTO cartOrder : cartOrderDTOList){

            if (!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())){

                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        Long orderId = cartService.orderCartItem(cartOrderDTOList, principal.getName());

        return new ResponseEntity<Long>(1L, HttpStatus.OK);
    }

}
