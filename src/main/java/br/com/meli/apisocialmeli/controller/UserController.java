package br.com.meli.apisocialmeli.controller;

import br.com.meli.apisocialmeli.dto.UserFollowersCountDto;
import br.com.meli.apisocialmeli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        UserFollowersCountDto response = userService.obterTotalSeguidoresDoVendedor(userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
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