package com.example.BookSelling.service.impl;

import com.example.BookSelling.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public ResponseEntity<byte[]> showImageAsByte(String imageName) throws IOException {
        Path imagePath = Paths.get("uploads", imageName);
        byte[] imageBytes = Files.readAllBytes(imagePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
//anh em có trường hợp khác thì tạo function uploadImage vs param mới rồi đổi cái set model nhé
    @Override
    public String uploadImage(MultipartFile file,String filename) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return "success";
        } catch (IOException e) {
            return "Failed to upload image";
        }
    }






}

