package com.example.BookSelling.service;

import com.example.BookSelling.model.BookImage;
import com.example.BookSelling.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    public ResponseEntity<byte[]> showImageAsByte(String imageName) throws IOException;
    public String uploadImage(MultipartFile file,String filename);

}
