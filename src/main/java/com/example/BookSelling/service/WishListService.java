package com.example.BookSelling.service;

import com.example.BookSelling.model.WishList;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WishListService {
    public ResponseEntity<List<WishList>> getBookInWishList();
    public ResponseEntity<String> addToWishList(int bookId);
    public ResponseEntity<String> deleteWishList(int bookId);
}
