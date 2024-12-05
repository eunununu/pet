package com.example.pet.service;

import com.example.pet.dto.CartDetailDTO;
import com.example.pet.dto.CartItemDTO;
import com.example.pet.entity.Cart;
import com.example.pet.entity.CartItem;
import com.example.pet.entity.Item;
import com.example.pet.entity.Member;
import com.example.pet.repository.CartItemRepository;
import com.example.pet.repository.CartRepository;
import com.example.pet.repository.ItemRepository;
import com.example.pet.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDTO cartItemDTO, String identity){

        Member member = memberRepository.findByIdentity(identity);

        Item item = itemRepository.findById(cartItemDTO.getItemid())
                .orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        CartItem savedCartItem =
                cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (savedCartItem != null){
            savedCartItem.addCount(cartItemDTO.getCount());

            return savedCartItem.getId();

        }else {
            CartItem cartItem =
                    CartItem.createCartItem(cart, item, cartItemDTO.getCount());

            cartItemRepository.save(cartItem);

            return cartItem.getId();
        }
    }

    public List<CartDetailDTO> getCartList(String identity){

        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();

        Member member =
                memberRepository.findByIdentity(identity);

        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null){

            return cartDetailDTOList;
        }

        cartDetailDTOList = cartItemRepository.findCartDetailDTOList(cart.getId());

        return cartDetailDTOList;

    }


}
