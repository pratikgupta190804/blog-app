package com.backend.blogApp.service.impl;

import com.backend.blogApp.entity.Category;
import com.backend.blogApp.entity.Post;
import com.backend.blogApp.entity.User;
import com.backend.blogApp.exception.ResourceNotFoundException;
import com.backend.blogApp.dto.PostDto;
import com.backend.blogApp.payloads.PostResponse;
import com.backend.blogApp.repository.CategoryRepo;
import com.backend.blogApp.repository.PostRepo;
import com.backend.blogApp.repository.UserRepo;
import com.backend.blogApp.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepo postRepo;

    private ModelMapper modelMapper;

    private UserRepo userRepo;

    private CategoryRepo categoryRepo;

    @Autowired
    public PostServiceImpl(PostRepo postRepo, ModelMapper modelMapper, UserRepo userRepo, CategoryRepo categoryRepo){
        this.postRepo = postRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
        Post post = dtoToPost(postDto);
        post.setImageName("default.png");
        post.setCreatedAt(new Date());

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
        post.setUser(user);

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
        post.setCategory(category);

        Post savedPost = postRepo.save(post);
        return postToDto(savedPost);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));

        post.setPostTitle(postDto.getPostTitle());
        post.setPostContent(postDto.getPostContent());
        post.setImageName(postDto.getImageName());

        postRepo.save(post);
        return postToDto(post);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));
        postRepo.delete(post);
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortBy.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> post = postRepo.findAll(pageRequest);
        List<Post> posts = post.getContent();
        List<PostDto> postDtos = posts.stream().map((p -> postToDto(p))).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(post.getNumber());
        postResponse.setPageSize(post.getSize());
        postResponse.setTotalPages(post.getTotalPages());
        postResponse.setTotalElements(post.getTotalElements());
        postResponse.setLastPage(post.isLast());
        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        return postToDto(postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId)));
    }

    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Cateogry id", categoryId));
        List<Post> posts = postRepo.findByCategory(category);
        List<PostDto> postDtos = posts.stream().map(post ->
            postToDto(post)
        ).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));
        List<Post> posts = postRepo.findByUser(user);
        List<PostDto> postDtos = posts.stream().map((post -> postToDto(post))).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        List<Post> posts = postRepo.findByPostTitleContaining(keyword);
        List<PostDto> searchResult = posts.stream().map((post -> postToDto(post))).collect(Collectors.toList());
        return searchResult;
    }

    public Post dtoToPost(PostDto postDto){
        Post post = modelMapper.map(postDto, Post.class);
        return post;
    }

    public PostDto postToDto(Post post){
        PostDto postDto = modelMapper.map(post, PostDto.class);
        return postDto;
    }
}
