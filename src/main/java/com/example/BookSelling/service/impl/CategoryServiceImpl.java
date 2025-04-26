package com.example.BookSelling.service.impl;

import com.example.BookSelling.dto.request.CategoryRequest;
import com.example.BookSelling.dto.response.CategoryResponse;
import com.example.BookSelling.model.Category;
import com.example.BookSelling.repository.CategoryRepository;
import com.example.BookSelling.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                .build();
        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(savedCategory);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse getCategoryById(Integer id) {
        return null;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryID())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }
}
