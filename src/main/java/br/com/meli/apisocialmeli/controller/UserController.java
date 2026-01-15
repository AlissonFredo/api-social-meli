package br.com.meli.apisocialmeli.controller;

import br.com.meli.apisocialmeli.dto.UserFollowersCountDto;
import br.com.meli.apisocialmeli.service.FollowService;
import br.com.meli.apisocialmeli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController implements UserControllerDocs {
    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    @Override
    public ResponseEntity<?> seguirVendedor(@PathVariable Long userId, @PathVariable Long userIdToFollow) {
        followService.seguirVendedor(userId, userIdToFollow);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/{userId}/followers/count")
    @Override
    public ResponseEntity<?> obterTotalSeguidoresDoVendedor(@PathVariable Long userId) {
        UserFollowersCountDto response = userService.obterTotalSeguidoresDoVendedor(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}/followers/list")
    @Override
    public ResponseEntity<?> listarSeguidoresDoVendedor(@PathVariable Long userId, @RequestParam(name = "order", required = false, defaultValue = "name_asc") String order) {
        return new ResponseEntity<>(userService.listarSeguidoresDoVendedor(userId, order), HttpStatus.OK);
    }

    @GetMapping("/{userId}/followed/list")
    @Override
    public ResponseEntity<?> listarVendedoresSeguidosPorUsuario(@PathVariable Long userId, @RequestParam(name = "order", required = false, defaultValue = "name_asc") String order) {
        return new ResponseEntity<>(userService.listarVendedoresSeguidosPorUsuario(userId, order), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/unfollow/{userIdTounfollow}")
    @Override
    public ResponseEntity<?> unfollowSeller(@PathVariable Long userId, @PathVariable Long userIdTounfollow) {
        followService.unfollowSeller(userId, userIdTounfollow);
        return new ResponseEntity<>("sucesso", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> listaUsuarios() {
        return new ResponseEntity<>(userService.listaUsuarios(), HttpStatus.OK);
    }
}