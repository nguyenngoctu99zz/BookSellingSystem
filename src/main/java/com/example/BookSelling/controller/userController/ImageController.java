package com.example.BookSelling.controller.userController;

import com.example.BookSelling.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/show")
    public ResponseEntity<byte[]> getBookImage(@RequestParam String imageName) throws IOException {
        return imageService.showImageAsByte(imageName);
    }

//    @PostMapping("/upload")
//    public String uploadImage(@RequestParam("file") MultipartFile file,String filename) {
//        return imageService.uploadImage(file,filename);
//    }
}
