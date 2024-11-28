package com.example.pet.dto;

import com.example.pet.constant.*;
import com.example.pet.entity.ItemImg;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 50, message = "상품명은 2자 이상 50자 이하로 작성해주세요.")
    private String itemNm;

    @NotNull
    @PositiveOrZero
    private int itemPr;

    @NotNull
    @PositiveOrZero
    private int itemSq;

    @NotBlank
    private String itemDt;

    private ItemSellStatus itemSellStatus;

    private ItemCategory1 itemCategory1;
    private ItemCategory2 itemCategory2;
    private ItemCategory3 itemCategory3;
    private ItemCategory4 itemCategory4;

    private List<ItemImgDTO> itemImgDTOList;

    private LocalDateTime regiTime;

    private LocalDateTime updateTime;

    public ItemDTO setItemImgDTOList(List<ItemImg> itemImgList) {

        ModelMapper modelMapper = new ModelMapper();

        List<ItemImgDTO> itemImgDTOS =
                itemImgList.stream().map(
                        itemImg -> modelMapper.map(itemImg, ItemImgDTO.class)
                ).collect(Collectors.toList());

        this.itemImgDTOList = itemImgDTOS;

        return this;
    }
}
