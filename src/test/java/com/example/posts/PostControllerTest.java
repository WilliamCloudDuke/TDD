package com.example.posts;

import com.example.posts.controller.PostController;
import com.example.posts.model.Post;
import com.example.posts.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;//we are going to use this to create all those Mocks

    @MockBean
    PostService postService;

    List<Post> posts = new ArrayList<>();

    @BeforeEach
        //init method
    void setup() {
        posts = List.of(
                new Post(1, 1, "Hello World", "this is my first post", null),
                new Post(2, 1, "Second World", "this is my second post", null));
    }

    //I need to write the REST API
    @Test
    public void shouldFindAllPosts() throws Exception{
        mockMvc.perform(get(new URI("/api/posts"))).andExpect(status().isOk());
    }


}
