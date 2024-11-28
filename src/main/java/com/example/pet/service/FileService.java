package com.example.pet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.UUID;

@Service
public class FileService {

    @Value("C:/pet/item")
    String itemImgLocation;

    public String uploadFile(MultipartFile multipartFile) throws IOException {

        Path path = Paths.get(itemImgLocation);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("디렉토리가 생성되었습니다: " + path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("디렉토리 생성 중 오류 발생", e);
            }
        }

        UUID uuid = UUID.randomUUID();

        String extension = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf("."));

        String savedFileName = uuid.toString() + extension;

        String fileUploadFullUrl = itemImgLocation + "/" + savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(multipartFile.getBytes());
        fos.close();

        return savedFileName;
    }

    public void removefile(String imgName) {
        String delFileUrl = itemImgLocation + "/" + imgName;
        System.out.println(delFileUrl);
        File file = new File(delFileUrl);

        if (file.exists()) {
            file.delete();
        }
    }

}
