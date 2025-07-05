package com.backend.blogApp.service;

import com.backend.blogApp.dto.PostDto;
import com.backend.blogApp.payloads.PostResponse;

import java.util.List;

public interface PostService {

    //create
    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);
    //update
    PostDto updatePost(PostDto postDto, Integer postId);
    //delete
    void deletePost(Integer postId);
    //get post
    PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    //get post by post id
    PostDto getPostById(Integer postId);
    //get post by category
    List<PostDto> getPostsByCategory(Integer categoryId);
    //get Post by user id
    List<PostDto> getPostsByUser(Integer userId);

    //get post by search
    List<PostDto> searchPosts(String keyword);

}
