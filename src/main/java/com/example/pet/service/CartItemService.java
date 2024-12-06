package com.example.pet.service;

import com.example.pet.dto.CartItemDTO;
import com.example.pet.entity.CartItem;
import com.example.pet.entity.Member;
import com.example.pet.repository.CartItemRepository;
import com.example.pet.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.lang.management.OperatingSystemMXBean;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public Long modifyCartItemId(CartItemDTO cartItemDTO, String identity) throws Exception {

        Member member = memberRepository.findByIdentity(identity);

        Optional<CartItem> optionalCartItem =
                cartItemRepository.findById(cartItemDTO.getItemid());

        CartItem cartItem =
                optionalCartItem.orElseThrow(EntityNotFoundException::new);

        if (cartItem.getCart()!=null && member!=null && cartItem.getCart().getMember().getId()!=member.getId()){
            throw new Exception();
        }

        cartItem.setCount(cartItem.getCount());

        return cartItem.getId();
    }


}
