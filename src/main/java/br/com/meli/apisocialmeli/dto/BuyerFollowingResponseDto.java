package br.com.meli.apisocialmeli.dto;

import java.util.List;

public class BuyerFollowingResponseDto {
    private Long userId;
    private String userName;
    private List<UserDto> followed;

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

    public List<UserDto> getFollowed() {
        return followed;
    }

    public void setFollowed(List<UserDto> followed) {
        this.followed = followed;
    }
}
