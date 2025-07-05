package com.backend.blogApp.service.impl;

import com.backend.blogApp.entity.Category;
import com.backend.blogApp.exception.ResourceNotFoundException;
import com.backend.blogApp.dto.CategoryDto;
import com.backend.blogApp.payloads.CategoryResponse;
import com.backend.blogApp.repository.CategoryRepo;
import com.backend.blogApp.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepo categoryRepo;

    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepo categoryRepo, ModelMapper modelMapper){
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = dtoToCategory(categoryDto);
        Category savedCategory = categoryRepo.save(category);
        return categoryToDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        category.setCategoryTitle(categoryDto.getCategoryTitle() == null ? category.getCategoryTitle() : categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription() == null ? category.getCategoryDescription() : categoryDto.getCategoryDescription());
        categoryRepo.save(category);
        return categoryToDto(category);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        categoryRepo.delete(category);
    }

    @Override
    public CategoryDto getCategoryById(Integer categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        return categoryToDto(category);
    }

    @Override
    public CategoryResponse getAllCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortBy.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepo.findAll(p);
        List<Category> category = categoryPage.getContent();
        List<CategoryDto> categoryDtos = category.stream().map((cat -> categoryToDto(cat))).collect(Collectors.toList());

        CategoryResponse response = new CategoryResponse();
        response.setContent(categoryDtos);
        response.setPageNumber(categoryPage.getNumber());
        response.setPageSize(categoryPage.getSize());
        response.setTotalElements(categoryPage.getTotalElements());
        response.setTotalPages(categoryPage.getTotalPages());
        response.setLastPage(categoryPage.isLast());
        return response;
    }

    @Override
    public List<CategoryDto> getCategoryByTitle(String title) {
        List<Category> categoryList = categoryRepo.findByCategoryTitleContaining(title);
        List<CategoryDto> dtos = categoryList.stream().map((category -> categoryToDto(category))).collect(Collectors.toList());
        return dtos;
    }

    public Category dtoToCategory(CategoryDto categoryDto){
        Category category = modelMapper.map(categoryDto, Category.class);
        return category;
    }

    public CategoryDto categoryToDto(Category category){
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        return categoryDto;
    }
}
