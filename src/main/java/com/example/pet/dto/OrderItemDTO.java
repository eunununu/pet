package com.example.pet.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderItemDTO {

    private Long id;

    private String itemNm;

    private int orderPr;

    private int orderQt;

    private String imgUrl;

}
