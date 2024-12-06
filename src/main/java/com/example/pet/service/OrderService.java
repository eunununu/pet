package com.example.pet.service;

import com.example.pet.constant.OrderStatus;
import com.example.pet.dto.OrderDTO;
import com.example.pet.dto.OrderHistDTO;
import com.example.pet.dto.OrderItemDTO;
import com.example.pet.entity.*;
import com.example.pet.repository.ItemRepository;
import com.example.pet.repository.MemberRepository;
import com.example.pet.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public boolean validateOrder(Long orderId, String identity){

        Member member =
                memberRepository.findByIdentity(identity);

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(EntityNotFoundException::new);

        if (!StringUtils.equals(member.getIdentity(), order.getMember().getIdentity())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId){

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(EntityNotFoundException::new);

        order.setOrderStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : order.getOrderItemList()){
            orderItem.getItem().setItemSq(
                    orderItem.getItem().getItemSq() + orderItem.getCount()
            );
        }
    }



    public Long order(OrderDTO orderDTO, String identity) {

        log.info(orderDTO);

        Item item = itemRepository.findById(orderDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByIdentity(identity);

        if (item.getItemSq() >= orderDTO.getCount()) {

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(orderDTO.getCount());
            orderItem.setOrderPr(item.getPrice());

            item.setItemSq(item.getItemSq() - orderDTO.getCount());

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

    public Long orders(List<OrderDTO> orderDTOList, String identity){

        Member member = memberRepository.findByIdentity(identity);
        List<OrderItem> orderItemList = new ArrayList<>();
        Order order = new Order();

        for (OrderDTO orderDTO : orderDTOList){
            Item item = itemRepository.findById(orderDTO.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(orderDTO.getCount());
            orderItem.setOrderPr(item.getPrice());
            orderItem.setOrder(order);

            item.setItemSq(item.getItemSq() - orderDTO.getCount());

            orderItemList.add(orderItem);
        }
        order.setMember(member);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderItemList(orderItemList);

        orderRepository.save(order);

        return order.getId();
    }


    public Page<OrderHistDTO> getOrderList(String identity, Pageable pageable){

        List<Order> orderList = orderRepository.findOrders(identity, pageable);

        Long totalCount = orderRepository.totalCount(identity);

        List<OrderHistDTO> orderHistDTOList = new ArrayList<>();

        for (Order order : orderList){

            OrderHistDTO orderHistDTO = new OrderHistDTO();
            orderHistDTO.setOrderId(order.getId());
            orderHistDTO.setOrderDate(orderHistDTO.getOrderDate());
            orderHistDTO.setOrderStatus(order.getOrderStatus());

            List<OrderItem> orderItemList = order.getOrderItemList();

            for (OrderItem orderItem : orderItemList){

                OrderItemDTO orderItemDTO = new OrderItemDTO();

                orderItemDTO.setId(order.getId());
                orderItemDTO.setItemNm(orderItem.getItem().getItemNm());
                orderItemDTO.setOrderPr(orderItem.getOrderPr());
                orderItemDTO.setCount(orderItem.getCount());

                List<ItemImg> itemImgList = orderItem.getItem().getItemImgList();

                for (ItemImg itemImg :itemImgList){
                    if (itemImg.getRepImgYn().equals("Y")){
                        orderItemDTO.setImgUrl(itemImg.getImgUrl());
                    }
                }
                orderHistDTO.addOrderItemDTO(orderItemDTO);

            }
            orderHistDTOList.add(orderHistDTO);
        }

        return new PageImpl<OrderHistDTO>(orderHistDTOList, pageable, totalCount);

    }


}
