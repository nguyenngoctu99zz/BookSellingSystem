package com.example.BookSelling.service.impl;

import com.example.BookSelling.model.WishList;
import com.example.BookSelling.repository.BookRepository;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.repository.WishListRepository;
import com.example.BookSelling.service.UserService;
import com.example.BookSelling.service.WishListService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishListServiceImpl implements WishListService {

    WishListRepository wishListRepository;
    UserRepository userRepository;
    BookRepository bookRepository;
    UserService userService;

    @Override
    public ResponseEntity<List<WishList>> getBookInWishList() {
        int userId = userService.getCurrentUserId();
       List<WishList> wishLists = wishListRepository.findWishListByUserId(userId);
        return ResponseEntity.ok().body(wishLists) ;
    }

    @Override
    public ResponseEntity<String> addToWishList(int bookId) {
        int userId = userService.getCurrentUserId();
        try {
            wishListRepository.saveAndFlush(WishList.builder()
                    .user(userRepository.findById(userId).get()).book(bookRepository.findById(bookId).get())
                    .build());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("save fail");
        }
        return ResponseEntity.ok().body("add success");
    }


    @Override
    public ResponseEntity<String> deleteWishList(int bookId) {
        int userId = userService.getCurrentUserId();
            wishListRepository.deleteByBookAndUser(bookId,userId);
        return ResponseEntity.ok().body("success");
    }

}
