package com.backend.blogApp.controller;

import com.backend.blogApp.payloads.ApiResponse;
import com.backend.blogApp.dto.PostDto;
import com.backend.blogApp.payloads.PostResponse;
import com.backend.blogApp.service.FileService;
import com.backend.blogApp.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Post APIs", description = "Create, Read, Update and Delete for Posts")
public class PostController {

    public final String PAGE_NUMBER = "0";
    public final String PAGE_SIZE = "4";
    public final String SORT_BY = "postId";
    public final String SORT_DIR = "asc";

    @Value("${project.name}")
    private String path;

    private PostService postService;
    private FileService fileService;

    @Autowired
    public PostController(PostService postService, FileService fileService){
        this.postService = postService;
        this.fileService = fileService;
    }

    // Create Post
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody PostDto postDto,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId
    ){
        return new ResponseEntity<PostDto>(postService.createPost(postDto, userId, categoryId), HttpStatus.CREATED);
    }

    // Get Post by User id
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostByUser(@PathVariable Integer userId){
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    // Get Post by Category
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable Integer categoryId){
        return ResponseEntity.ok(postService.getPostsByCategory(categoryId));
    }

    // Get post By post id
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId){
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    // Get all Posts
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = SORT_DIR, required = false) String sortDir
    ){
        return ResponseEntity.ok(postService.getAllPost(pageNumber, pageSize, sortBy, sortDir));
    }

    // Delete Post by post id
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId){
        postService.deletePost(postId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Post Deleted Successfully !!", true), HttpStatus.OK);
    }

    // Update POst by id
    @PutMapping("/post/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId){
        return ResponseEntity.ok(postService.updatePost(postDto, postId));
    }

    // search post by title keywords
    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDto>> searchByTitleKeyword(
            @PathVariable("keywords") String keywords
    ){
        return ResponseEntity.ok(postService.searchPosts(keywords));
    }

    @PostMapping("/post/{postId}/upload/image")
    public ResponseEntity<PostDto> uploadImage(
            @RequestParam("image")MultipartFile image,
            @PathVariable Integer postId
            ) throws IOException {
        PostDto postDto = postService.getPostById(postId);
        String fileName = fileService.uploadImage(image, path);
        postDto.setImageName(fileName);

        PostDto updatedPost = postService.updatePost(postDto, postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void imageViewURL(
            @PathVariable String imageName,
            HttpServletResponse response
    ) throws IOException {
        InputStream is = fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(is, response.getOutputStream());
    }
}
