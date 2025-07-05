package com.backend.blogApp.controller;

import com.backend.blogApp.payloads.ApiResponse;
import com.backend.blogApp.dto.CategoryDto;
import com.backend.blogApp.payloads.CategoryResponse;
import com.backend.blogApp.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category APIs", description = "Create, Read, Update and Delete for Category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    //create category
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<CategoryDto>(createdCategory, HttpStatus.CREATED);
    }

    //update category
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer categoryId){
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        return ResponseEntity.ok(updatedCategory);
    }

    //delete category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Category deleted successfully!!", true), HttpStatus.OK);
    }

    //get category
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer categoryId){
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    // get all category
    @GetMapping("/")
    public ResponseEntity<CategoryResponse> getAllCategory(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "categoryTitle", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return ResponseEntity.ok(categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir));
    }

    // get category by searching title
    @GetMapping("/search/{title}")
    public ResponseEntity<List<CategoryDto>> searchTitle(
            @PathVariable("title") String title
    ){
        return ResponseEntity.ok(categoryService.getCategoryByTitle(title));
    }
}
