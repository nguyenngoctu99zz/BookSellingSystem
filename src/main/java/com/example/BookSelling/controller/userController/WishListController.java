package com.example.BookSelling.controller.userController;

import com.example.BookSelling.model.WishList;
import com.example.BookSelling.service.WishListService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wish-list")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishListController {

    WishListService wishListService;

    @GetMapping("/")
    public ResponseEntity<List<WishList>> showWishList(){
        return wishListService.getBookInWishList();
    }

    @PostMapping("/add-to-wishlist")
    public ResponseEntity<String> addToWishList(@RequestParam int bookId){
        return wishListService.addToWishList(bookId);
    }

    @PostMapping("/delete-wishlist")
    public ResponseEntity<String> deleteWishList(@RequestParam int bookId){
        return wishListService.deleteWishList(bookId);
    }
}
