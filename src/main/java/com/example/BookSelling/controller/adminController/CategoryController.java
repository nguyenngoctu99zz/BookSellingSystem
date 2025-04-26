package com.example.BookSelling.controller.adminController;

import com.example.BookSelling.dto.request.CategoryRequest;
import com.example.BookSelling.dto.response.CategoryResponse;
import com.example.BookSelling.dto.response.ResponseData;
import com.example.BookSelling.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping("")
    public ResponseData<CategoryResponse> addCategory(@RequestBody CategoryRequest request) {
        return ResponseData.<CategoryResponse>builder()
                .code(200)
                .message("Category added successfully")
                .data(categoryService.createCategory(request))
                .build();
    }

    @GetMapping("")
    public ResponseData<List<CategoryResponse>> getAllCategories() {
        return ResponseData.<List<CategoryResponse>>builder()
                .code(200)
                .message("All categories")
                .data(categoryService.getAllCategories())
                .build();
    }
    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable int categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
