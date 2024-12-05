package com.example.pet.repository;

import com.example.pet.dto.CartDetailDTO;
import com.example.pet.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    public CartItem findByCartIdAndItemId (Long cartId, Long itemId);

    public List<CartItem> findByCartId (Long cartId);

    @Query("select new com.example.pet.dto.CartDetailDTO(ci.id, i.itemNm , i.price , ci.count , im.imgUrl ) " +
            " from CartItem  ci , ItemImg  im " +
            "join ci.item i  where ci.cart.id = :cartId " +
            " and im.item.id = ci.item.id " +
            " and im.repImgYn = 'Y'  " +
            " order by ci.id desc ")
    public List<CartDetailDTO> findCartDetailDTOList(Long cartId);

}
