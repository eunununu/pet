package com.example.pet.repository;

import com.example.pet.entity.Item;
import com.example.pet.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    Optional<ItemImg> findByItemId(Long id);

    public ItemImg findByItemIdAndRepImgYn(Long id, String val);
}
