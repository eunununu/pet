package com.example.pet.entity;

import com.example.pet.constant.*;
import com.example.pet.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "item")
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(nullable = false)
    private int itemSq;

    @Lob
    @Column(nullable = false)
    private String itemDt;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @Enumerated(EnumType.STRING)
    private ItemCategory1 itemCategory1;

    @Enumerated(EnumType.STRING)
    private ItemCategory2 itemCategory2;

    @Enumerated(EnumType.STRING)
    private ItemCategory3 itemCategory3;

    @Enumerated(EnumType.STRING)
    private ItemCategory4 itemCategory4;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemImg> itemImgList;
}
