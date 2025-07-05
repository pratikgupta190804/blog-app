package com.backend.blogApp.controller;

import com.backend.blogApp.payloads.ApiResponse;
import com.backend.blogApp.dto.CommentDto;
import com.backend.blogApp.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Comment APIs", description = "Create, Read, Update and Delete for Comment")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/user/{userId}/post/{postId}/comment")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Integer userId,
            @PathVariable Integer postId,
            @Valid @RequestBody CommentDto commentDto
    ){
        return ResponseEntity.ok(commentService.createComment(commentDto, postId, userId));
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @RequestBody CommentDto commentDto,
            @PathVariable Integer commentId
    ){
        CommentDto updatedComment = commentService.updateComment(commentId, commentDto);
        return new ResponseEntity<CommentDto>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable Integer commentId
    ){
        commentService.deleteComment(commentId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted Successfully", true), HttpStatus.OK);
    }

}
