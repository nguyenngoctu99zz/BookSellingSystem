package com.example.BookSelling.dto.response;

import com.example.BookSelling.model.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewBookByPageResponse {
    private List<Book> bookList;
    private int numberOfPage;
}
