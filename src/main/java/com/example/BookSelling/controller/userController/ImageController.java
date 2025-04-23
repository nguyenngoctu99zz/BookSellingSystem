package com.example.BookSelling.controller.userController;

import com.example.BookSelling.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    ImageService imageService;

    @GetMapping("/show")
    public ResponseEntity<byte[]> getBookImage(@RequestParam String imageName) throws IOException {
        return imageService.showImageAsByte(imageName);
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file,String filename) {
        return imageService.uploadImage(file,filename);
    }
}
