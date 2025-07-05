package com.backend.blogApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDto {

    private Integer categoryId;

    @NotBlank
    @Size(min = 3, message = "Title should be atleast 3 character long!!")
    private String categoryTitle;

    @NotBlank
    @Size(min = 8, message = "Description should be atleast 8 characters long!!")
    private String categoryDescription;

    public CategoryDto(){
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
