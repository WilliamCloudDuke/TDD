package com.example.posts.controller;

import com.example.posts.dto.PostDto;
import com.example.posts.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.posts.exception.PostNotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("")
    public List<PostDto> findAll() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<PostDto> findById(@PathVariable Integer id) {
        return Optional.ofNullable(postService.findById(id).orElseThrow(PostNotFoundException::new));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto create(@RequestBody @Valid PostDto postDto) {
        return postService.save(postDto);
    }


    @PutMapping("/{id}")
    //@ResponseStatus(HttpStatus.OK)
    public PostDto update(@PathVariable Integer id, @RequestBody @Valid PostDto postDto) {
        //fetch the record from the DB
        Optional<PostDto> optExisting = postService.findById(id);
        if (optExisting.isPresent()) {
            PostDto existing = optExisting.get();
            PostDto toUpdate = PostDto.builder()
                    .id(existing.id())
                    .userId(existing.userId())
                    .body(postDto.body())
                    .title(postDto.title())
                    .version(existing.version())
                    .build();
            return postService.save(toUpdate);
        } else {
            throw new PostNotFoundException("Post with id " + id + " not found ");
        }
    }

}
