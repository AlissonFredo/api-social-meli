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
        try {
            followService.seguirVendedor(userId, userIdToFollow);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<?> obterTotalSeguidoresDoVendedor(@PathVariable Long userId) {
        try {
            UserFollowersCountDto response = userService.obterTotalSeguidoresDoVendedor(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<?> listarSeguidoresDoVendedor(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(userService.listarSeguidoresDoVendedor(userId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<?> listarVendedoresSeguidosPorUsuario(@PathVariable Long userId) {
        try {
            return new ResponseEntity<>(userService.listarVendedoresSeguidosPorUsuario(userId), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}/unfollow/{userIdTounfollow}")
    public ResponseEntity<?> unfollowSeller(@PathVariable Long userId, @PathVariable Long userIdTounfollow) {
        try {
            followService.unfollowSeller(userId, userIdTounfollow);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}