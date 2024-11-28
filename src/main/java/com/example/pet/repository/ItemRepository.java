package com.example.pet.repository;

import com.example.pet.dto.PageRequestDTO;
import com.example.pet.entity.Item;
import com.example.pet.repository.search.ItemsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemsearchRepository {

    Optional<Item> findById(Long id);

    public List<Item> findByItemNm(String itemNm);

    @Query("select i from Item i where i.itemNm like concat('%',:itemNm,'%')")
    public List<Item> selectwhereItemNm(String itemNm);

    public List<Item> findByItemDtContaining(String itemDt);

    public List<Item> findByItemPrLessThan(Integer itemPr);

    public List<Item> findByItemPrGreaterThan(Integer itemPr);

    public List<Item> findByItemPrGreaterThanOrderByItemPrAsc(Integer itemPr);

    public List<Item> findByItemPrGreaterThanEqual(Integer itemPr, Pageable pageable);

    List<Item> findByItemPrLessThanOrderByItemPrDesc(Integer itemPr);

    @Query(value = "select * from Item i where i.item_nm = :itemNm", nativeQuery = true)
    List<Item> nativeQuerySelectwhereNamelike(String itemNm, Pageable pageable);


}
