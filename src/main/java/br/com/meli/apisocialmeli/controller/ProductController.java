package br.com.meli.apisocialmeli.controller;

import br.com.meli.apisocialmeli.dto.PostPromoPubRequestDto;
import br.com.meli.apisocialmeli.dto.PostRequestDto;
import br.com.meli.apisocialmeli.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
public class ProductController implements ProductControllerDocs {
    @Autowired
    private ProductService productService;

    @PostMapping("publish")
    @Override
    public ResponseEntity<?> cadastrarProduto(@Valid @RequestBody PostRequestDto product) {
        return new ResponseEntity<>(productService.cadastrarProduto(product), HttpStatus.OK);
    }

    @GetMapping("followed/{userId}/list")
    @Override
    public ResponseEntity<?> getFollowedSuppliersRecentProducts(@PathVariable Long userId, @RequestParam(name = "order", required = false, defaultValue = "date_asc") String order) {
        return new ResponseEntity<>(productService.getFollowedSuppliersRecentProducts(userId, order), HttpStatus.OK);
    }

    @PostMapping("promo-pub")
    @Override
    public ResponseEntity<?> cadastraProdutoPromocional(@Valid @RequestBody PostPromoPubRequestDto post) {
        return new ResponseEntity<>(productService.cadastraProdutoPromocional(post), HttpStatus.OK);
    }

    @GetMapping("promo-pub/count")
    @Override
    public ResponseEntity<?> obterTotalPordutosPromoVendedor(@RequestParam(name = "userId") Long userId) {
        return new ResponseEntity<>(productService.obterTotalPordutosPromoVendedor(userId), HttpStatus.OK);
    }
}
