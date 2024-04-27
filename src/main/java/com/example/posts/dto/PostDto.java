package com.example.posts.dto;

import lombok.Builder;

@Builder
public record PostDto(
        Integer id,
        Integer userId,
        String title,
        String body,
        Integer version
) {
}
