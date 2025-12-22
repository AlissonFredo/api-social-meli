package br.com.meli.apisocialmeli.controller;

import br.com.meli.apisocialmeli.dto.UserFollowersCountDto;
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

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public String seguirVendedor(@PathVariable Long userId, @PathVariable Long userIdToFollow) {
        return "seguirVendedor | userId: " + userId + " userIdToFollow: " + userIdToFollow;
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<?> obterTotalSeguidoresDoVendedor(@PathVariable Long userId) {
        try {
            UserFollowersCountDto response = userService.obterTotalSeguidoresDoVendedor(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/followers/list")
    public String listarSeguidoresDoVendedor(@PathVariable Long userId) {
        return "listarSeguidoresDoVendedor | userId: " + userId;
    }

    @GetMapping("/{userId}/followed/list")
    public String listarVendedoresSeguidosPorUsuario(@PathVariable Long userId) {
        return "listarVendedoresSeguidosPorUsuario | userId: " + userId;
    }
}