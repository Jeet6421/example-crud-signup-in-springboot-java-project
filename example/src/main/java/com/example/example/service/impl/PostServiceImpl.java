package com.example.example.service.impl;

import com.example.example.entity.Post;
import com.example.example.exceptions.ResourceNotFound;
import com.example.example.payload.PostDto;
import com.example.example.repository.PostRepository;
import com.example.example.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepo;
    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepo, ModelMapper mapper) {
        this.postRepo = postRepo;
        this.mapper = mapper;

    }



    @Override
    public PostDto createPost(PostDto postDto) {

        // dto to entity
       Post post = mapToEntity(postDto);
      Post savePost = postRepo.save(post);
        // entity to dto
       PostDto dto = mapToDto(savePost);

        return dto;
    }

    @Override
    public PostDto getById(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                ()->new ResourceNotFound(id)
        );

       PostDto dto = mapToDto(post);

        return dto;
    }


    @Override
    public List<PostDto> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

       Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo , pageSize  , sort);

       Page<Post> posts =  postRepo.findAll(pageable);

     List<Post> content =  posts.getContent();

        List<PostDto> postDtos = content.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        return postDtos;
    }


    @Override
    public void deletePost(long id) {
       Post post = postRepo.findById(id).orElseThrow(
                ()-> new ResourceNotFound(id)
        );
       postRepo.deleteById(id);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepo.findById(id).orElseThrow(
        ()->new ResourceNotFound(id)
        );

        Post updateContent = mapToEntity(postDto);
        updateContent.setId(post.getId());
        Post updatedPostInfo  = postRepo.save(updateContent);

        return mapToDto(updatedPostInfo);

    }


    PostDto mapToDto(Post post){
        PostDto dto = mapper.map(post, PostDto.class);
        return dto;
    }

    Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
        return post;
    }
}
