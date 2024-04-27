package com.example.posts.service;

import com.example.posts.dto.PostDto;
import com.example.posts.model.Post;
import com.example.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
