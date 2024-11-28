package com.example.pet.service;

import com.example.pet.entity.Item;
import com.example.pet.entity.ItemImg;
import com.example.pet.repository.ItemImgRepository;
import com.example.pet.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.attoparser.dom.INode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ItemImgService {

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;
    private final ItemRepository itemRepository;

    public void saveImg(Long id, List<MultipartFile> multipartFile) throws IOException {

        if (multipartFile != null) {
            for (MultipartFile img : multipartFile) {
                if (!img.isEmpty()) {
                    String savedFileName = fileService.uploadFile(img);

                    Item item = itemRepository.findById(id).get();

                    String imgUrl = "/images/item/" + savedFileName;

                    ItemImg itemImg = new ItemImg();
                    itemImg.setItem(item);
                    itemImg.setImgName(savedFileName);
                    itemImg.setImgUrl(imgUrl);
                    itemImg.setOriImgName(img.getOriginalFilename());

                    if (multipartFile.indexOf(img) == 0) {
                        itemImg.setRepImgYn("Y");

                    } else {
                        itemImg.setRepImgYn("N");
                    }
                    itemImgRepository.save(itemImg);

                }
            }
        }
    }

    public void modify(Long id, List<MultipartFile> multipartFile, Long mainino) throws IOException {

        if (multipartFile != null) {
            for (MultipartFile img : multipartFile) {
                if (!img.isEmpty()) {
                    String savedFileName = fileService.uploadFile(img);

                    Item item = itemRepository.findById(id).get();

                    String imgUrl = "/images/item/" + savedFileName;

                    ItemImg itemImg = new ItemImg();
                    itemImg.setItem(item);
                    itemImg.setImgName(savedFileName);
                    itemImg.setImgUrl(imgUrl);
                    itemImg.setOriImgName(img.getOriginalFilename());

                    if (mainino == null) {
                        if (multipartFile.indexOf(img) == 0) {
                            itemImg = itemImgRepository.findByItemIdAndRepImgYn(id, "Y");
                            itemImg.setItem(item);
                            itemImg.setImgName(savedFileName);
                            itemImg.setImgUrl(imgUrl);
                            itemImg.setOriImgName(img.getOriginalFilename());

                        } else {
                            itemImg.setRepImgYn("N");
                        }
                    } else {
                        itemImg.setRepImgYn("N");
                    }

                    itemImgRepository.save(itemImg);
                }
            }
        }
    }


    public void removeimg(Long id) {

        ItemImg itemImg = itemImgRepository.findById(id).get();

        fileService.removefile(itemImg.getImgName());

        itemImgRepository.deleteById(id);
    }

}
