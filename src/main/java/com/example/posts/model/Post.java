package com.example.posts.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

@Builder
public record Post(
        @Id
        Integer id,
        Integer userId,
        @NotBlank(message = "Name cannot be blank")
        String title,
        @NotEmpty
        String body,
        @Version
        Integer version
) {
}
