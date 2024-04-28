package com.example.posts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.springframework.data.annotation.Version;

@Builder
public record PostDto(
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
