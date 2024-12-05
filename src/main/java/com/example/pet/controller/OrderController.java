package com.example.pet.controller;


import com.example.pet.dto.OrderDTO;
import com.example.pet.dto.OrderHistDTO;
import com.example.pet.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity order(@Valid OrderDTO orderDTO, BindingResult bindingResult, Principal principal) {

        log.info("controller : " + orderDTO);

        if (bindingResult.hasErrors()) {
            StringBuffer sb = new StringBuffer();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                log.info("필드 : " + fieldError.getField() + "메시지 : "+ fieldError.getDefaultMessage());
                sb.append(fieldError.getDefaultMessage());
            }
            log.info(sb.toString());
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        Long result = orderService.order(orderDTO, principal.getName());

        if (result == null){
            return new ResponseEntity<String>("주문 수량이 판매 가능 수량을 초과하였습니다.", HttpStatus.OK);
        }

        return new ResponseEntity<String>("주문이 완료되었습니다.", HttpStatus.OK);
    }

    @GetMapping({"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page")Optional<Integer> page,
                            Principal principal, Model model){

        if (principal==null){
            log.info("로그인이 필요합니다.");

            return "redirect:/member/login";
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,4);
        log.info(pageable);

        String identity = principal.getName();
        if (identity == null){
            return "redirect:/member/login";
        }

        Page<OrderHistDTO> orderHistDTOPage =
                orderService.getOrderList(identity, pageable);

        model.addAttribute("orders", orderHistDTOPage);

        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public ResponseEntity cancelOrder(
            @PathVariable("orderId") Long orderId, Principal principal){

        if (!orderService.validateOrder(orderId, principal.getName())){

            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
