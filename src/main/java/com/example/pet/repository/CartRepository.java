package com.example.pet.repository;

import com.example.pet.entity.Cart;
import com.example.pet.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {


    public Cart findByMemberId (Long memberId);

    public Cart findByMemberIdentity (String identity);


}
