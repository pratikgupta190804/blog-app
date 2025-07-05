package com.backend.blogApp.service;

import com.backend.blogApp.dto.CategoryDto;
import com.backend.blogApp.payloads.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);

    void deleteCategory(Integer categoryId);

    CategoryDto getCategoryById(Integer categoryId);

    CategoryResponse getAllCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    List<CategoryDto> getCategoryByTitle(String title);
}
