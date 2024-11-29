package com.example.pet.service;

import com.example.pet.dto.ItemDTO;
import com.example.pet.dto.PageRequestDTO;
import com.example.pet.dto.PageResponseDTO;
import com.example.pet.entity.Item;
import com.example.pet.entity.ItemImg;
import com.example.pet.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    private final ItemImgService itemImgService;

    public Long saveItem(ItemDTO itemDTO, List<MultipartFile> multipartFile) throws IOException {

        Item item = modelMapper.map(itemDTO, Item.class);

        item.setItemNm(itemDTO.getItemNm());
        item.setItemPr(itemDTO.getItemPr());
        item.setItemDt(itemDTO.getItemDt());
        item.setItemSellStatus(itemDTO.getItemSellStatus());
        item.setItemSq(itemDTO.getItemSq());
        item.setItemCategory1(item.getItemCategory1());
        item.setItemCategory2(item.getItemCategory2());
        item.setItemCategory3(item.getItemCategory3());
        item.setItemCategory4(item.getItemCategory4());

        item = itemRepository.save(item);

        itemImgService.saveImg(item.getId(), multipartFile);

        return item.getId();
    }

    public ItemDTO read(Long id) {

        Item item =
                itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class)
                .setItemImgDTOList(item.getItemImgList());

        return itemDTO;
    }

    public ItemDTO read(Long id, String email) {

        Item item =
                itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class)
                .setItemImgDTOList(item.getItemImgList());

        return itemDTO;

    }

    public PageResponseDTO<ItemDTO> list(PageRequestDTO pageRequestDTO, String email) {

        Pageable pageable = pageRequestDTO.getPageable("id");
        Page<Item> items =
                itemRepository.getAdminItemPage(pageRequestDTO, pageable, email);
        List<ItemDTO> itemDTOPage =
                items.getContent().stream().map(item -> modelMapper.map(item, ItemDTO.class))
                        .collect(Collectors.toList());

        PageResponseDTO<ItemDTO> itemDTOPageResponseDTO
                = PageResponseDTO.<ItemDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(itemDTOPage)
                .total((int) items.getTotalElements())
                .build();
        return itemDTOPageResponseDTO;
    }

    public ItemDTO modify(ItemDTO itemDTO, Long id, List<MultipartFile> multipartFile, Integer[] delino, Long mainino) {

        Item item =
                itemRepository.findById(itemDTO.getId())
                        .orElseThrow(EntityNotFoundException::new);

        item.setItemNm(itemDTO.getItemNm());
        item.setItemPr(itemDTO.getItemPr());
        item.setItemDt(itemDTO.getItemDt());
        item.setItemSellStatus(itemDTO.getItemSellStatus());
        item.setItemSq(itemDTO.getItemSq());
        item.setItemCategory1(item.getItemCategory1());
        item.setItemCategory2(item.getItemCategory2());
        item.setItemCategory3(item.getItemCategory3());
        item.setItemCategory4(item.getItemCategory4());

        if (delino != null) {

            for (Integer ino : delino) {

                if (ino != null && !ino.equals("")) {
                    log.info("삭제할 번호는 ino" + ino);
                    itemImgService.removeimg(ino.longValue());
                }
            }
        }
        try {
            itemImgService.modify(id, multipartFile, mainino);

        } catch (IOException e) {

        }

        return null;
    }

    public void remove(Long id) {

        itemRepository.deleteById(id);
    }

    public PageResponseDTO<ItemDTO> mainlist(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Item> items  = itemRepository.getAdminItemPageMain(pageRequestDTO, pageable);


        List<ItemDTO> itemDTOPage =
                items.getContent().stream().map(item -> modelMapper.map(item, ItemDTO.class))
                        .collect(Collectors.toList());

        PageResponseDTO<ItemDTO> itemDTOPageResponseDTO
                = PageResponseDTO.<ItemDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(itemDTOPage)
                .total((int) items.getTotalElements())
                .build();

        return itemDTOPageResponseDTO;
    }

    public PageResponseDTO<ItemDTO> maindog(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Item> items  = itemRepository.getAdminItemPage(pageRequestDTO, pageable);


        List<ItemDTO> itemDTOPage =
                items.getContent().stream().map(item -> modelMapper.map(item, ItemDTO.class))
                        .collect(Collectors.toList());

        PageResponseDTO<ItemDTO> itemDTOPageResponseDTO
                = PageResponseDTO.<ItemDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(itemDTOPage)
                .total((int) items.getTotalElements())
                .build();

        return itemDTOPageResponseDTO;
    }

    public PageResponseDTO<ItemDTO> maincat(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable("id");

        Page<Item> items  = itemRepository.getAdminItemPage(pageRequestDTO, pageable);


        List<ItemDTO> itemDTOPage =
                items.getContent().stream().map(item -> modelMapper.map(item, ItemDTO.class))
                        .collect(Collectors.toList());

        PageResponseDTO<ItemDTO> itemDTOPageResponseDTO
                = PageResponseDTO.<ItemDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(itemDTOPage)
                .total((int) items.getTotalElements())
                .build();

        return itemDTOPageResponseDTO;
    }
}
