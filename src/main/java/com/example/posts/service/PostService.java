package com.example.posts.service;

import com.example.posts.dto.PostDto;
import com.example.posts.exception.PostInvalidException;
import com.example.posts.model.Post;
import com.example.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public List<PostDto> findAll() {
        log.info("Service class calling repository");
        return postRepository.findAll().stream().map(this::mapToDto).toList();
    }


    private PostDto mapToDto(Post post) {
        return PostDto.builder()
                .id(post.id())
                .userId(post.userId())
                .title(post.title())
                .body(post.body())
                .version(post.version())
                .build();
    }


    public Optional<PostDto> findById(Integer id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.map(this::mapToDto);

    }

    public PostDto save(PostDto postDto) {
        validateDTO(postDto);
        return mapToDto(postRepository.save(convertToModel(postDto)));
    }

    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }


    private void validateDTO(PostDto postDto) {
        if (postDto.id() == null || postDto.userId() == null) {
            log.error("id or userID are empty");
            throw new PostInvalidException("id or userId should not be empty");
        }
        if (postDto.body() == null || postDto.body().isEmpty()) {
            log.error("body is empty");
            throw new PostInvalidException("body should not be empty");
        }
        if (postDto.title() == null || postDto.title().isEmpty()) {
            log.error("title is empty");
            throw new PostInvalidException("tittle should not be empty");
        }
    }

    private Post convertToModel(PostDto postDto) {
        return Post.builder()
                .id(postDto.id())
                .userId(postDto.userId())
                .title(postDto.title())
                .body(postDto.body())
                .version(postDto.version())
                .build();
    }

}
