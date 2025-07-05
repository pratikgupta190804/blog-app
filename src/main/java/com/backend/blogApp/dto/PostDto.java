package com.backend.blogApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PostDto {

    private Integer postId;

    @NotBlank
    @Size(min = 3 ,message = "Title should be atleast 3 character long!!")
    private String postTitle;

    @NotBlank
    @Size(min = 10, message = "Content is too small just like your :) !!")
    private String postContent;
    private String imageName;
    private Date createdAt;
    private UserDto user;
    private CategoryDto category;
    private Set<CommentDto> comments = new HashSet<>();

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public Set<CommentDto> getComments() {
        return comments;
    }

    public void setComments(Set<CommentDto> comments) {
        this.comments = comments;
    }
}
