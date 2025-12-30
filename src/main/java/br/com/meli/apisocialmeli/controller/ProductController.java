package br.com.meli.apisocialmeli.controller;

import br.com.meli.apisocialmeli.dto.PostRequestDto;
import br.com.meli.apisocialmeli.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("publish")
    public ResponseEntity<?> cadastrarProduto(@RequestBody PostRequestDto product) {
        return new ResponseEntity<>(productService.cadastrarProduto(product), HttpStatus.OK);
    }

    @GetMapping("followed/{userId}/list")
    public ResponseEntity<?> getFollowedSuppliersRecentProducts(@PathVariable Long userId) {
        return new ResponseEntity<>(productService.getFollowedSuppliersRecentProducts(userId), HttpStatus.OK);
    }
}
