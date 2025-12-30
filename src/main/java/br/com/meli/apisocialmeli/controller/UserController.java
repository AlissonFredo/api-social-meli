package br.com.meli.apisocialmeli.controller;

import br.com.meli.apisocialmeli.dto.UserFollowersCountDto;
import br.com.meli.apisocialmeli.service.FollowService;
import br.com.meli.apisocialmeli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<?> seguirVendedor(@PathVariable Long userId, @PathVariable Long userIdToFollow) {
        followService.seguirVendedor(userId, userIdToFollow);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<?> obterTotalSeguidoresDoVendedor(@PathVariable Long userId) {
        UserFollowersCountDto response = userService.obterTotalSeguidoresDoVendedor(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<?> listarSeguidoresDoVendedor(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.listarSeguidoresDoVendedor(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<?> listarVendedoresSeguidosPorUsuario(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.listarVendedoresSeguidosPorUsuario(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/unfollow/{userIdTounfollow}")
    public ResponseEntity<?> unfollowSeller(@PathVariable Long userId, @PathVariable Long userIdTounfollow) {
        followService.unfollowSeller(userId, userIdTounfollow);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}