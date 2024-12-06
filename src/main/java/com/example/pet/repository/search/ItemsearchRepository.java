package com.example.pet.repository.search;

import com.example.pet.dto.PageRequestDTO;
import com.example.pet.dto.PageResponseDTO;
import com.example.pet.entity.Item;
import com.example.pet.dto.PageRequestDTO;
import com.example.pet.entity.Item;
import com.example.pet.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemsearchRepository {

    Page<Item> getAdminItemPage(PageRequestDTO pageRequestDTO, Pageable pageable, String identity);

    Page<Item> getAdminItemPage(PageRequestDTO pageRequestDTO, Pageable pageable);

    Page<Item> getAdminItemPageMain(PageRequestDTO pageRequestDTO, Pageable pageable);


}
