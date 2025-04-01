package com.example.BookSelling.dto.response;

import com.example.BookSelling.model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookResponse {
    public List<Book> books;
    public int totalNumber;
}
