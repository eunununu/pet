package com.example.pet.dto;


import com.example.pet.constant.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderHistDTO {

    private Long orderId;

    private String orderDate;

    private OrderStatus orderStatus;

    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

    public void addOrderItemDTO(OrderItemDTO orderItemDTO){
        orderItemDTOList.add(orderItemDTO);
    }

    public OrderHistDTO setOrderItemDTOList(List<OrderItemDTO> orderItemDTOList){

        this.orderItemDTOList = orderItemDTOList;

        return this;
    }

}
