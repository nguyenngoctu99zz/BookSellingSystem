package com.example.BookSelling.repository;

import com.example.BookSelling.model.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImageRepository extends JpaRepository<BookImage,Integer> {
}
