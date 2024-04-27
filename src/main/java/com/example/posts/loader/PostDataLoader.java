package com.example.posts.loader;

import com.example.posts.dto.Posts;
import com.example.posts.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.asm.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class PostDataLoader implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final PostRepository postRepository;


    public PostDataLoader(ObjectMapper objectMapper, PostRepository postRepository) {
        this.objectMapper = objectMapper;
        this.postRepository = postRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (postRepository.count() == 0) {
            String POST_JSON = "/data/posts.json";
            log.info("Loading posts into database from JSON {} ", POST_JSON);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(POST_JSON)) {
                Posts response = objectMapper.readValue(inputStream, Posts.class);
                postRepository.saveAll(response.posts());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data ", e);
            }
        }

    }


}
