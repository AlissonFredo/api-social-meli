package br.com.meli.apisocialmeli.dto;

import java.util.List;

public class SellerFollowersResponseDto {
    private Long userId;
    private String userName;
    private List<UserDto> followers;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UserDto> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UserDto> followers) {
        this.followers = followers;
    }
}
