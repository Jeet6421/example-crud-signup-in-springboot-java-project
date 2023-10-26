package com.example.example.service.impl;

import com.example.example.entity.Comment;
import com.example.example.entity.Post;
import com.example.example.exceptions.ResourceNotFound;
import com.example.example.payload.CommentDto;
import com.example.example.payload.PostDto;
import com.example.example.repository.CommentRepository;
import com.example.example.repository.PostRepository;
import com.example.example.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound(postId));

        // set post to comment entity
        comment.setPost(post);

        // comment entity to DB
        Comment newComment =  commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    private CommentDto mapToDTO(Comment comment){
        CommentDto CommentDto = mapper.map(comment, CommentDto.class);
        return  CommentDto;

    }

    private Comment mapToEntity(CommentDto commentDto){
       Comment comment = mapper.map(commentDto, Comment.class );
        return  comment;
    }

}

