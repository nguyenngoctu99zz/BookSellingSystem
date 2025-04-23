package com.example.BookSelling.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    ResponseEntity<byte[]> showImageAsByte(String imageName) throws IOException;
    String uploadImage(MultipartFile file,String filename);

}
