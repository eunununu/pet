package com.example.pet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CartItemDTO {

    @NotNull(message = "상품 아이디는 필수 입력값입니다.")
    private Long itemid;

    @Min(value = 1, message = "최소 수량은 1개입니다.")
    private int count;
}
