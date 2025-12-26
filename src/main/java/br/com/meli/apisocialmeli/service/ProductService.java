package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.dto.ProductRequestDto;
import br.com.meli.apisocialmeli.dto.ProductResponseDto;
import br.com.meli.apisocialmeli.model.ProductModel;
import br.com.meli.apisocialmeli.model.UserModel;
import br.com.meli.apisocialmeli.model.UserTipo;
import br.com.meli.apisocialmeli.repository.ProductRepository;
import br.com.meli.apisocialmeli.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public ProductResponseDto cadastrarProduto(ProductRequestDto product) {
        UserModel userModel = userRepository.findById(product.getUserId())
                .orElseThrow(() -> new NoSuchElementException("Usuário " + product.getUserId() + " não encontrado"));

        if (userModel.getTipo() == UserTipo.BUYER) {
            throw new IllegalStateException("O usuário " + product.getUserId() + " não é um vendedor");
        }

        ProductModel productModel = new ProductModel();
        productModel.setName(product.getProduct().getProductName());
        productModel.setSeller(userModel);
        productModel.setType(product.getProduct().getType());
        productModel.setBrand(product.getProduct().getBrand());
        productModel.setColor(product.getProduct().getColor());
        productModel.setNotes(product.getProduct().getNotes());
        productModel.setCategory(product.getCategory());
        productModel.setPrice(product.getPrice());

        ProductModel productSalved = productRepository.save(productModel);

        return new ProductResponseDto(productSalved);
    }
}
