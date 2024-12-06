package com.example.pet.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CartOrderDTO {

    private Long cartItemId;

    private List<CartOrderDTO> orderDTOList;

}
