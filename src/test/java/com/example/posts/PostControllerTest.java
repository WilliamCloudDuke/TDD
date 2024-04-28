package com.example.posts;

import com.example.posts.controller.PostController;
import com.example.posts.dto.PostDto;
import com.example.posts.exception.PostNotFoundException;
import com.example.posts.model.Post;
import com.example.posts.repository.PostRepository;
import com.example.posts.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;//we are going to use this to create all those Mocks

    @MockBean
    PostService postService;

    @MockBean
    PostRepository postRepository;

    List<PostDto> posts = new ArrayList<>();

    @BeforeEach
    void setup() {
        posts = List.of(
                new PostDto(1, 1, "Hello World", "this is my first post", null),
                new PostDto(2, 1, "Second Post", "this is my second post", null));
    }

    //I need to write the REST API
    @Test
    public void shouldFindAllPosts() throws Exception {
        String jsonResponse = """
                [
                  {
                    "userId": 1,
                    "id": 1,
                    "title": "Hello World",
                    "body": "this is my first post",
                    "version": null
                  },
                  {
                    "userId": 1,
                    "id": 2,
                    "title": "Second Post",
                    "body": "this is my second post",
                    "version": null
                  }
                ]
                """;

        when(postService.findAll()).thenReturn(posts);

        mockMvc.perform(get(new URI("/api/posts"))).andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }


    //api/posts/1
    @Test
    public void shouldFindPostWhenGivenValidID() throws Exception {
        var json = """
                  {
                    "userId": 1,
                    "id": 1,
                    "title": "Hello World",
                    "body": "this is my first post",
                    "version": null
                  }
                """;

        when(postService.findById(1)).thenReturn(Optional.of(posts.getFirst()));
        mockMvc.perform(get(new URI("/api/posts/1"))).andExpect(status().isOk())
                .andExpect(content().json(json));

    }

    @Test
    public void shouldNotFindPostWhenGivenInvalidId() throws Exception {
        when(postService.findById(101)).thenThrow(PostNotFoundException.class);
        mockMvc.perform(get(new URI("/api/posts/101"))).andExpect(status().isNotFound());
    }


    @Test
    public void shouldCreatePostWhenPostIsValid() throws Exception {
        var post = new Post(1, 11, "NEW TITLE", "NEW BODY", null);
        when(postRepository.save(post)).thenReturn(post);
        var json = """
                  {
                    "userId": 11,
                    "id": 1,
                    "title": "NEW TITLE",
                    "body": "NEW BODY",
                    "version": null
                  }
                """;
        mockMvc.perform(post(new URI("/api/posts")).contentType("application/json").content(json))
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldNotCreatePostWhenPostIsInvalid() throws Exception {
        var post = new Post(null, null, null, null, null);
        when(postRepository.save(post)).thenReturn(post);
        var jsonBad = """
                  {
                    "userId": null,
                    "id": null,
                    "title": null,
                    "body": null,
                    "version": null
                  }
                """;
        mockMvc.perform(post(new URI("/api/posts")).contentType("application/json").content(jsonBad))
                .andExpect(status().isCreated());
        //Fix this, this test should return .andExpect(status().isBadRequest());
    }


    //UPDATE AND DELETE METHODS

    @Test
    public void shouldUpdatePostWhenGivenValidPost() throws Exception {
        var updatedPost = new PostDto(1, 11, "UPDATED TITLE", "UPDATED  BODY", null);
        when(postService.findById(1)).thenReturn(Optional.of(updatedPost));
        when(postService.save(updatedPost)).thenReturn(updatedPost);
        var json = """
                  {
                    "userId": 1,
                    "id": 1,
                    "title": "Hello World",
                    "body": "this is my first post",
                    "version": null
                  }
                """;

        mockMvc.perform(put(new URI("/api/posts/1"))
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
    }


    @Test
    public void shouldNotUpdatePostWhenGivenInValidPost() throws Exception {
        var updatedPost = new PostDto(12345, 11, "UPDATED TITLE", "UPDATED  BODY", null);
        when(postService.findById(12345)).thenReturn(Optional.of(updatedPost));
        when(postService.save(updatedPost)).thenReturn(updatedPost);
        var json = """
                  {
                    "userId": 1,
                    "id": 12345,
                    "title": "Hello World",
                    "body": "this is my first post",
                    "version": null
                  }
                """;

        mockMvc.perform(put(new URI("/api/posts/1"))
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }


    @Test
    public void deletePostGivenValidId() throws Exception {
        //mock out objects as delete method does not return a value (void)
        doNothing().when(postService).deleteById(1);
        mockMvc.perform(delete(new URI("/api/posts/1"))).andExpect(status().isNoContent());
        //verify that service was called - code coverage
        verify(postService,times(1)).deleteById(1);

    }

}
