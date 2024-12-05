package com.example.pet.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailDTO {

    private Long cartItemId;

    private String itemNm;

    private int price;

    private int count;

    private String imgUrl;
}
