package com.example.BookSelling.controller.userController;

import com.example.BookSelling.repository.BookImageRepository;
import com.example.BookSelling.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    ImageService imageService;
    @Autowired
    BookImageRepository bookImageRepository;
    //test thoi
    @GetMapping("/show")
    public ResponseEntity<byte[]> getBookImage(@RequestParam String imageName) throws IOException {
        return imageService.showImageAsByte(imageName);
    }

    //test thoi
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file,String filename) {
        return imageService.uploadImage(file,filename);
    }
}
