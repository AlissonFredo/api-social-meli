package br.com.meli.apisocialmeli.dto;

import java.util.List;

public class PostsFollowingLastTwoWeeksResponseDto {
    private Long userId;
    private List<PostResponseDto> posts;

    public PostsFollowingLastTwoWeeksResponseDto() {
    }

    public PostsFollowingLastTwoWeeksResponseDto(Long userId, List<PostResponseDto> posts) {
        this.userId = userId;
        this.posts = posts;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<PostResponseDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponseDto> posts) {
        this.posts = posts;
    }
}
