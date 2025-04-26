package com.example.BookSelling.service;

import com.example.BookSelling.dto.request.CategoryRequest;
import com.example.BookSelling.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    void deleteCategory(Integer id);
    CategoryResponse getCategoryById(Integer id);
    List<CategoryResponse> getAllCategories();
}
