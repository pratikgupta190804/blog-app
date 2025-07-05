package com.backend.blogApp.service;

import com.backend.blogApp.dto.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId);
    CommentDto updateComment(Integer commentId, CommentDto commentDto);
    void deleteComment(Integer commentId);
}
