package com.example.pet.service;

import com.example.pet.constant.OrderStatus;
import com.example.pet.dto.OrderDTO;
import com.example.pet.entity.Item;
import com.example.pet.entity.Member;
import com.example.pet.entity.Order;
import com.example.pet.entity.OrderItem;
import com.example.pet.repository.ItemRepository;
import com.example.pet.repository.MemberRepository;
import com.example.pet.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public Long order(OrderDTO orderDTO, String identity) {

        Item item = itemRepository.findById(orderDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByIdentity(identity);

        if (item.getItemSq() >= orderDTO.getOrderQt()) {

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrderQt(orderDTO.getOrderQt());
            orderItem.setOrderPr(item.getItemPr());

            item.setItemSq(item.getItemSq() - orderDTO.getOrderQt());

            Order order = new Order();
            order.setMember(member);

            order.setOrderItemList(orderItem);

            order.setOrderStatus(OrderStatus.ORDER);
            order.setOrderDate(LocalDateTime.now());
            orderItem.setOrder(order);

            order = orderRepository.save(order);

            return order.getId();

        } else {
            return null;
        }
    }

}
