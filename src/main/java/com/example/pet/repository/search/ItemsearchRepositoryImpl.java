package com.example.pet.repository.search;

import com.example.pet.constant.ItemCategory1;
import com.example.pet.constant.ItemCategory2;
import com.example.pet.dto.PageRequestDTO;
import com.example.pet.entity.Item;
import com.example.pet.entity.Member;
import com.example.pet.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;

public class ItemsearchRepositoryImpl extends QuerydslRepositorySupport implements ItemsearchRepository {
    public ItemsearchRepositoryImpl() {
        super(Item.class);
    }

    @Override
    public Page<Item> getAdminItemPage(PageRequestDTO pageRequestDTO, Pageable pageable, String email) {

        QItem item = QItem.item;
        JPQLQuery<Item> query = from(item);
        System.out.println(query);
        System.out.println("-------------------------");

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        String searchDateType = pageRequestDTO.getSearchDateType();
        LocalDateTime localDateTime = LocalDateTime.now();

        if (pageRequestDTO.getSearchDateType() == null || pageRequestDTO.getSearchDateType().equals("all") || pageRequestDTO.getSearchDateType().equals("")) {

        } else if (searchDateType.equals("1d")) {
            booleanBuilder.and(item.regiTime.after(localDateTime.minusDays(1)));
        } else if (searchDateType.equals("1w")) {
            booleanBuilder.and(item.regiTime.after(localDateTime.minusWeeks(1)));
        } else if (searchDateType.equals("1m")) {
            booleanBuilder.and(item.regiTime.after(localDateTime.minusMonths(1)));
        } else if (searchDateType.equals("6m")) {
            booleanBuilder.and(item.regiTime.after(localDateTime.minusMonths(6)));
        }

        System.out.println("----------------------");
        System.out.println(query);

        if (types != null && types.length > 0 && keyword != null) {
            for (String str : types) {
                switch (str) {
                    case "n":
                        booleanBuilder.or(item.itemNm.contains(keyword));
                        break;
                    case "d":
                        booleanBuilder.or(item.itemDt.contains(keyword));
                        break;
                }
            }

        }
        query.where(booleanBuilder);
        System.out.println(query);
        System.out.println("----------------------------");

        query.where(item.id.gt(0L));

        System.out.println(query);
        System.out.println("----------------------------");

        this.getQuerydsl().applyPagination(pageable, query);

        List<Item> boardList = query.fetch();

        long count =
                query.fetchCount();

        return new PageImpl<>(boardList, pageable, count);

    }

    public Page<Item> getAdminItemPageMain(PageRequestDTO pageRequestDTO, Pageable pageable) {

        QItem item = QItem.item;

        JPQLQuery<Item> query = from(item);

        System.out.println(query);
        System.out.println("-------------------------");

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        String[] types = pageRequestDTO.getTypes();
        String keyword =pageRequestDTO.getKeyword();
        String searchDateType = pageRequestDTO.getSearchDateType();
        LocalDateTime localDateTime = LocalDateTime.now();

        if (pageRequestDTO.getSearchDateType() == null || pageRequestDTO.getSearchDateType().equals("all") || pageRequestDTO.getSearchDateType().equals("")) {

        }else if(searchDateType.equals("1d")){
            booleanBuilder.and(item.regiTime.after(localDateTime.minusDays(1)));
        }else if(searchDateType.equals("1w")){
            booleanBuilder.and(item.regiTime.after(localDateTime.minusWeeks(1)));
        }else if(searchDateType.equals("1m")){
            booleanBuilder.and(item.regiTime.after(localDateTime.minusMonths(1)));
        }else if(searchDateType.equals("6m")){
            booleanBuilder.and(item.regiTime.after(localDateTime.minusMonths(6)));
        }

        System.out.println("----------------------");
        System.out.println(query);

        if( types != null && types.length > 0 && keyword != null){
            for(String str  : types){
                switch (str){
                    case "n" :
                        booleanBuilder.or(item.itemNm.contains(keyword));
                        break;
                    case "d" :
                        booleanBuilder.or(item.itemDt.contains(keyword));
                        break;
                }
            }
        }
        query.where(booleanBuilder);
        System.out.println(query);
        System.out.println("----------------------------");

        query.where(item.id.gt(0L));   // select * from board //   // board.bno > 0

        System.out.println(query);
        System.out.println("----------------------------");

        this.getQuerydsl().applyPagination(pageable, query);

        List<Item> boardList = query.fetch();

        long count =
                query.fetchCount();

        return new PageImpl<>(boardList, pageable, count);

    }



    public Page<Item> getAdminItemPage (PageRequestDTO pageRequestDTO, Pageable pageable){

        QItem item = QItem.item;
        JPQLQuery<Item> query = from(item);
        System.out.println(query);
        System.out.println("-------------------------");

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        System.out.println("----------------------");
        System.out.println(query);

        if (pageRequestDTO.getCategory1().equals("DOG")) {
            booleanBuilder.and(item.itemCategory1.eq(ItemCategory1.DOG));

        } else if (pageRequestDTO.getCategory1().equals("CAT")) {
            booleanBuilder.and(item.itemCategory1.eq(ItemCategory1.CAT));
        }

        if (pageRequestDTO.getCategory2().equals("FEED")) {
            booleanBuilder.and(item.itemCategory2.eq(ItemCategory2.FEED));

        } else if (pageRequestDTO.getCategory2().equals("TREAT")) {
            booleanBuilder.and(item.itemCategory2.eq(ItemCategory2.TREAT));

        } else if (pageRequestDTO.getCategory2().equals("PRODUCT")) {
            booleanBuilder.and(item.itemCategory2.eq(ItemCategory2.PRODUCT));
        }

        query.where(booleanBuilder);
        System.out.println(query);
        System.out.println("----------------------------");

        query.where(item.id.gt(0L));

        System.out.println(query);
        System.out.println("----------------------------");

        this.getQuerydsl().applyPagination(pageable, query);

        List<Item> boardList = query.fetch();

        long count =
                query.fetchCount();

        return new PageImpl<>(boardList, pageable, count);
    }

}



