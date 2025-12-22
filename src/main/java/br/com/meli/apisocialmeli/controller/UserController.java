package br.com.meli.apisocialmeli.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public String seguirVendedor(@PathVariable Long userId, @PathVariable Long userIdToFollow) {
        return "seguirVendedor | userId: " + userId + " userIdToFollow: " + userIdToFollow;
    }

    @GetMapping("/{userId}/followers/count")
    public String obterTotalSeguidoresDoVendedor(@PathVariable Long userId) {
        return "obterTotalSeguidoresDoVendedor | userId: " + userId;
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