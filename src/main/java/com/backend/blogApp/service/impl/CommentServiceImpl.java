package com.backend.blogApp.service.impl;

import com.backend.blogApp.entity.Post;
import com.backend.blogApp.entity.User;
import com.backend.blogApp.entity.Comment;
import com.backend.blogApp.exception.ResourceNotFoundException;
import com.backend.blogApp.dto.CommentDto;
import com.backend.blogApp.repository.CommentRepo;
import com.backend.blogApp.repository.PostRepo;
import com.backend.blogApp.repository.UserRepo;
import com.backend.blogApp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepo commentRepo;
    private PostRepo postRepo;
    private UserRepo userRepo;
    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepo commentRepo, PostRepo postRepo, UserRepo userRepo, ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Comment comment = modelMapper.map(commentDto, Comment.class);

        comment.setLastUpdatedAt(new Date());
        comment.setUser(user);
        comment.setPost(post);

        Comment savedComment = commentRepo.save(comment);

        CommentDto createdDto = modelMapper.map(savedComment, CommentDto.class);
        createdDto.setPostId(postId);
        return createdDto;
    }

    @Override
    public CommentDto updateComment(Integer commentId, CommentDto commentDto) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        comment.setContent(commentDto.getContent());
        comment.setLastUpdatedAt(new Date());

        Comment updatedComment = commentRepo.save(comment);

        CommentDto savedComment = modelMapper.map(updatedComment, CommentDto.class);
        savedComment.setPostId(comment.getPost().getPostId());

        return savedComment;
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        commentRepo.delete(comment);
    }
}
